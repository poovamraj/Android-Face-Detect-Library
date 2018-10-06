package com.poovam.facecapture.captureactivity.previewfragment.presenter

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.graphics.Bitmap
import com.poovam.facecapture.framework.persistence.InMemoryPersistence

/**
 * Created by poovam-5255 on 10/6/2018.
 */
internal class PreviewPresenter(private val eventListener: PreviewPresenterEvents) : LifecycleObserver{

    private val persistence = InMemoryPersistence

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onViewCreated(){
        checkPersistenceAndSetImage()
    }

    private fun checkPersistenceAndSetImage(){
        val result = persistence.image
        if(result != null){
            eventListener.onImageSuccess(result.image)
        }
    }

    interface PreviewPresenterEvents {
        fun onImageSuccess(image: Bitmap)
    }
}