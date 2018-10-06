package com.poovam.facecapture.framework.persistence

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import com.poovam.facecapture.framework.camera.model.ResultImage


/**
 * Created by poovam-5255 on 9/30/2018.
 * This will be the in memory object that will be used to store data
 */

//:Deviate: It is not ideal maintain the information in memory this will cause coupling
// We can slip in another persistence method without affecting other classes by implementing Persistence
// for another concrete implementation

internal object InMemoryPersistence : Persistence, LifecycleObserver {

    var image: ResultImage? = null

    override fun putResultImage(result: ResultImage) {
        image = result
    }

    override fun getResultImage(): ResultImage? {
        return image
    }

    override fun clearPersistence(){
        image = null
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        clearPersistence()
    }
}