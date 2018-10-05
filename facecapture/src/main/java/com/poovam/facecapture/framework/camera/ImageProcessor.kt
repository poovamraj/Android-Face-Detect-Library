package com.poovam.facecapture.framework.camera

import android.graphics.*
import com.poovam.facecapture.framework.camera.model.Face
import com.poovam.facecapture.framework.camera.model.Frame
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

        fun cropToImage(bitmap: Bitmap, rect: Rect): Bitmap {
            return Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height())
        }

        fun putOverlay(bitmap: Bitmap, face: Face) {
            val canvas = Canvas(bitmap)
            val point = PointF()
            val paint = Paint()
            paint.color = Color.BLUE
            canvas.drawRect(point.x-(face.eyeDistance/2),point.y-10,point.x+(face.eyeDistance/2),point.y+10,paint)
        }

        fun Bitmap.flip(horizontal: Boolean, vertical: Boolean): Bitmap {
            val matrix = Matrix()
            matrix.preScale((if (horizontal) -1 else 1).toFloat(), (if (vertical) -1 else 1).toFloat())
            return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
        }

        fun Bitmap.rotate(degrees: Float): Bitmap {
            val matrix = Matrix()
            matrix.postRotate(degrees)
            val scaledBitmap = Bitmap.createScaledBitmap(this, width, height, true);
            return Bitmap.createBitmap(
                    scaledBitmap,
                    0,
                    0,
                    scaledBitmap.width,
                    scaledBitmap.height,
                    matrix,
                    true
            )
        }
    }
}
