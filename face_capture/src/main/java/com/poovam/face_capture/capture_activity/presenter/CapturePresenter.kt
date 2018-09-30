package com.poovam.face_capture.capture_activity.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.poovam.face_capture.framework.camera.handler.FotoApparatCameraHandler
import com.poovam.face_capture.framework.camera.handler.Handler
import com.poovam.face_capture.framework.camera.model.ResultImage
import com.poovam.face_capture.framework.persistence.InMemoryPersistence
import com.poovam.face_capture.framework.persistence.Persistence

/**
 * Created by poovam-5255 on 9/29/2018.
 */

class CapturePresenter(private val cameraHandler: FotoApparatCameraHandler) : LifecycleObserver, Handler.CameraEvents {

    private val persistenceService: Persistence = InMemoryPersistence

    var eventListener: PresenterEvents? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        cameraHandler.setCameraEventsListener(this)
    }

    fun resumeCamera(){
        cameraHandler.resumeCamera()
    }

    fun onPermissionResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray){
        cameraHandler.permissionResult(requestCode,permissions,grantResults)
    }

    override fun onFaceCaptured(image: ResultImage) {
        persistenceService.putResultImage(image)
        eventListener?.onSuccess()
    }

    override fun onError(error: Throwable) {
        cameraHandler.onStop()
        persistenceService.clearPersistence()
        eventListener?.onError(error)
    }

    interface PresenterEvents {
        fun onSuccess()

        fun onError(error:Throwable)
    }
}