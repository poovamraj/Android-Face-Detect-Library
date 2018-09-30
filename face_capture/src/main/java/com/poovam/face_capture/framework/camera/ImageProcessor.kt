package com.poovam.face_capture.framework.camera

import android.graphics.*
import android.media.FaceDetector
import com.poovam.face_capture.framework.camera.model.CapturedImage
import com.poovam.face_capture.framework.camera.model.Face
import com.poovam.face_capture.framework.camera.model.Frame
import com.poovam.face_capture.framework.utils.flip
import com.poovam.face_capture.framework.utils.rotate
import java.io.ByteArrayOutputStream

/**
 * Created by poovam-5255 on 9/30/2018.
 */


class ImageProcessor {
    companion object {
        fun convertFrameToBitmap(frame: Frame, quality: Int, bitmapOptions: BitmapFactory.Options): Bitmap {
            val os = ByteArrayOutputStream()
            val yuvImage = YuvImage(frame.image, ImageFormat.NV21, frame.size.width, frame.size.height, null)
            yuvImage.compressToJpeg(Rect(0, 0, frame.size.width, frame.size.height), quality, os)

            val array = os.toByteArray()
            var bm = BitmapFactory.decodeByteArray(array, 0, array.size, bitmapOptions)
                    ?.rotate(frame.rotation.toFloat())

            bm = when(frame.rotation){
                90 -> bm?.flip(false, true)
                180 -> bm?.flip(true, false)
                270 -> bm?.flip(true, true)
                else -> bm
            }
            return bm!!
        }

        fun cropToImage(bitmap: Bitmap): Bitmap {
            val rect = CaptureRegion.getCaptureRegionForScreen(bitmap.width, bitmap.height)
            return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height())
        }

        fun mapFrameToCapturedImage(frame: Frame, bitmapOptions: BitmapFactory.Options): CapturedImage {
            var bitmap = convertFrameToBitmap(frame,25,bitmapOptions)
            bitmap = cropToImage(bitmap)
            val face = detectFace(bitmap)
            if(face != null)
                return CapturedImage(frame, face)
            return CapturedImage(frame, null)
        }

        fun detectFace(bitmap: Bitmap): Face? {
            val detector = FaceDetector(bitmap.width,bitmap.height,1)
            val faces = arrayOfNulls<FaceDetector.Face>(1)
            detector.findFaces(bitmap,faces)
            val face = faces[0]
            if(face!=null){
                val point = PointF()
                face.getMidPoint(point)
                return Face(face.confidence(),face.eyesDistance(),point)
            }
            return null
        }

        fun putOverlay(bitmap: Bitmap, face: Face) {
            val canvas = Canvas(bitmap)
            val point = PointF()
            val paint = Paint()
            paint.color = Color.BLUE
            canvas.drawRect(point.x-(face.eyeDistance/2),point.y-10,point.x+(face.eyeDistance/2),point.y+10,paint)
        }
    }
}
