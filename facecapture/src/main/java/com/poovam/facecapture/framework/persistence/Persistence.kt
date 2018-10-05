package com.poovam.facecapture.framework.persistence

import com.poovam.facecapture.framework.camera.model.ResultImage

/**
 * Created by poovam-5255 on 9/30/2018.
 */
interface Persistence {

    fun clearPersistence()

    fun putResultImage(result: ResultImage)

    fun getResultImage(): ResultImage?
}