package com.poovam.facecapture.framework.camera

import android.graphics.Rect

/**
 * Created by poovam-5255 on 9/29/2018.
 */

class CaptureRegion {
    companion object {
        fun getCaptureRegionForScreen(width: Int, height: Int): Rect {
            return Rect((width / 2)-((width / 2)*0.75).toInt(),(height / 2)-((height / 2)*0.50).toInt(),(width / 2)+((width / 2)*0.75).toInt(),(height / 2)+((height / 2)*0.50).toInt())
        }
    }
}