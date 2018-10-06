package com.poovam.facecapture.framework.camera.handler

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.media.FaceDetector
import com.poovam.facecapture.framework.camera.CaptureRegion
import com.poovam.facecapture.framework.camera.ImageProcessor
import com.poovam.facecapture.framework.camera.model.*
import com.poovam.facecapture.framework.camera.permission.PermissionDelegate
import io.fotoapparat.Fotoapparat
import io.fotoapparat.selector.front
import io.fotoapparat.view.CameraRenderer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


/**
 * Created by poovam-5255 on 9/29/2018.
 */
internal class FotoApparatCameraHandler(
        private val activity: Activity,
        private val cameraView: CameraRenderer,
        private val timeoutInMillis: Long = CameraHandler.DEFAULT_TIMEOUT) : LifecycleObserver, CameraHandler {

    private val bitmapOptions = BitmapFactory.Options()

    private var frameProcessingStream: Observable<Frame>? = null

    /**:Deviate: Not using lateinit as it has the chance of escaping compiler check*/
    private var fotoapparat: Fotoapparat? = null

    private var permissionListener: CameraHandler.PermissionEvents? = null

    private var cameraEventsListener: CameraHandler.CameraEvents? = null

    private val permissionsDelegate = PermissionDelegate(activity)

    init {
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565
    }

    override fun startCamera() {
        fotoapparat?.start()
    }

    override fun stopCamera() {
        fotoapparat?.stop()
    }

    override fun initCamera(){
        frameProcessingStream = setFrameProcessing(
                Observable.create<Frame> {emitter ->
                    fotoapparat = Fotoapparat
                            .with(activity)
                            .into(cameraView)
                            .lensPosition(front())
                            .frameProcessor{ frame ->
                                emitter.onNext(Frame(Resolution(frame.size.width,frame.size.height),frame.image,frame.rotation))
                            }
                            .build()
                    startCamera()
                }
        )
    }

    private fun setFrameProcessing(frameStream: Observable<Frame>): Observable<Frame>{
        frameStream
                .subscribeOn(Schedulers.computation())
                .sample(timeoutInMillis, TimeUnit.MILLISECONDS)
                .map<CapturedImage> {
                    frame ->
                    var bitmap = ImageProcessor.convertFrameToBitmap(frame, CameraHandler.FACE_DETECTION_IMAGE_QUALITY, bitmapOptions)
                    bitmap = ImageProcessor.cropToImage(bitmap, CaptureRegion.getCaptureRegionForScreen(bitmap.width, bitmap.height))
                    return@map CapturedImage(frame, detectFace(bitmap))
                }
                .filter{
                    capturedImage -> return@filter capturedImage.face != null && capturedImage.face.confidence >= Face.CONFIDENCE_THRESHOLD
                }
                .takeUntil{
                    capturedImage -> return@takeUntil capturedImage.face != null && capturedImage.face.confidence >= Face.CONFIDENCE_THRESHOLD
                }
                .observeOn(
                        AndroidSchedulers.mainThread()
                )
                .subscribe(
                        { capturedImage ->
                            var bitmap = ImageProcessor.convertFrameToBitmap(capturedImage.originalFrame, CameraHandler.FINAL_IMAGE_QUALITY,bitmapOptions)
                            bitmap = ImageProcessor.cropToImage(bitmap,CaptureRegion.getCaptureRegionForScreen(bitmap.width, bitmap.height))
                            cameraEventsListener?.onFaceCaptured(ResultImage(bitmap,capturedImage))
                        },
                        { throwable ->
                            throwable.printStackTrace()
                            cameraEventsListener?.onError(throwable)
                        }
                )
        return frameStream
    }

    //TODO provide abstraction for Face Detection
    override fun detectFace(bitmap: Bitmap): Face? {
        val detector = FaceDetector(bitmap.width,bitmap.height, CameraHandler.NO_OF_FACES)
        val faces = arrayOfNulls<FaceDetector.Face>(CameraHandler.NO_OF_FACES)
        detector.findFaces(bitmap,faces)
        val face = faces[0]
        if(face!=null){
            val point = PointF()
            face.getMidPoint(point)
            return Face(face.confidence(),face.eyesDistance(),point)
        }
        return null
    }

    override fun resumeCamera(){
        val stream = frameProcessingStream
        if(stream != null){
            setFrameProcessing(stream)
        }
    }

    override fun permissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(permissionsDelegate.resultGranted(requestCode,permissions,grantResults)){
            permissionListener?.onPermissionAccepted()
            initCamera()
        }else{
            if(permissionsDelegate.hasTickedDontAskAgain()){
                permissionListener?.onPermissionDeniedDontShowAgain()
            }else{
                permissionListener?.onPermissionDenied()
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        if(permissionsDelegate.hasCameraPermission()){
            initCamera()
        }else{
            permissionsDelegate.requestPermission()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        startCamera()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        stopCamera()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        checkIfPermissionGivenFromSettings()
    }

    private fun checkIfPermissionGivenFromSettings(){
        if(permissionsDelegate.hasCameraPermission() && fotoapparat == null){
            permissionListener?.onPermissionAccepted()
            initCamera()
        }
    }

    override fun setPermissionEventsListener(listener: CameraHandler.PermissionEvents) {
        permissionListener = listener
    }

    override fun setCameraEventsListener(listener: CameraHandler.CameraEvents) {
        cameraEventsListener = listener
    }
}