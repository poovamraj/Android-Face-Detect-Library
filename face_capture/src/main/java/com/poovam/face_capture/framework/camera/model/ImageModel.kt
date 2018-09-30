package com.poovam.face_capture.framework.camera.model

import android.graphics.Bitmap
import android.graphics.PointF
import android.support.annotation.IntRange
import java.util.*

/**
 * Created by poovam-5255 on 9/30/2018.
 * The models used to avoid coupling
 */

data class ResultImage(
        val image: Bitmap,
        val data: CapturedImage
)

data class CapturedImage(
        val originalFrame: Frame,
        val face: Face?
)

//We use our own implementation of Face and Frame that way we can keep it decoupled
data class Face(
        /**
         * A confidence factor between 0 and 1
         */
        val confidence: Float,
        /**
         * Distance between the eyes.
         */
        val eyeDistance: Float,
        /**
         * The position of the mid-point between the eyes.
         */
        val midPoint: PointF
){
    companion object {
        /** The minimum confidence factor of good face recognition */
        val CONFIDENCE_THRESHOLD = 0.4f
    }
}

data class Frame(
        /**
         * Resolution of the frame in pixels (before rotation).
         */
        val size: Resolution,
        /**
         * Image in NV21 format.
         */
        val image: ByteArray,
        /**
         * Clockwise rotation of the image in degrees relatively to user.
         */
        val rotation: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as com.poovam.face_capture.framework.camera.model.Frame

        if (size != other.size) return false
        if (!Arrays.equals(image, other.image)) return false
        if (rotation != other.rotation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size.hashCode()
        result = 31 * result + Arrays.hashCode(image)
        result = 31 * result + rotation
        return result
    }
}

data class Resolution(
        @[JvmField IntRange(from = 0L)] val width: Int,
        @[JvmField IntRange(from = 0L)] val height: Int
)