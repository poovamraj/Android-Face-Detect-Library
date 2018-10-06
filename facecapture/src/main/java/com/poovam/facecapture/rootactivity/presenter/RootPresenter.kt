package com.poovam.facecapture.rootactivity.presenter

import com.poovam.facecapture.framework.persistence.InMemoryPersistence
import com.poovam.facecapture.framework.persistence.Persistence

/**
 * Created by poovam-5255 on 9/30/2018.
 */

internal class RootPresenter {

    private val persistenceService: Persistence = InMemoryPersistence

    fun onImageCapturedSuccess(){

    }
}