package com.poovam.facecapture.framework.camera.handler

import android.graphics.Bitmap
import com.poovam.facecapture.framework.camera.model.Face
import com.poovam.facecapture.framework.camera.model.ResultImage

/**
 * Created by poovam-5255 on 9/30/2018.
 */
internal interface CameraHandler {

    companion object {
        val DEFAULT_TIMEOUT = 200L

        val FACE_DETECTION_IMAGE_QUALITY = 25

        val FINAL_IMAGE_QUALITY = 100

        val NO_OF_FACES = 1
    }

    fun initCamera()

    fun startCamera()

    fun stopCamera()

    fun resumeCamera()

    fun detectFace(bitmap: Bitmap): Face?

    fun setPermissionEventsListener(listener: PermissionEvents)

    fun setCameraEventsListener(listener: CameraEvents)

    fun permissionResult(requestCode: Int,
                         permissions: Array<out String>,
                         grantResults: IntArray)


    interface CameraEvents {
        fun onFaceCaptured(image: ResultImage)

        fun onError(error: Throwable)
    }

    interface PermissionEvents {
        fun onPermissionDenied()

        fun onPermissionDeniedDontShowAgain()

        fun onPermissionAccepted()
    }
}