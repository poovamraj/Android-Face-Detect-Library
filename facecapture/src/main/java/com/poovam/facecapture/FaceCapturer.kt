package com.poovam.facecapture

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import com.poovam.facecapture.framework.persistence.InMemoryPersistence
import com.poovam.facecapture.rootactivity.view.RootActivity

/**
 * Created by poovam-5255 on 9/28/2018.
 * The class that will act as entry point
 */

//:Deviate: We wont be using builder pattern here since Kotlin supports named parameters and it is much neater
class FaceCapturer (
        private val baseTheme: BaseTheme = BaseTheme.DARK
) {

    private val persistence = InMemoryPersistence

    enum class BaseTheme{
        LIGHT,DARK
    }

    companion object {
        val BASE_THEME = "BASE_THEME"
    }

    fun startActivityForResult(activity: AppCompatActivity, requestCode: Int){
        val intent = Intent(activity.applicationContext, RootActivity::class.java)
        intent.run {
            putExtra(BASE_THEME,baseTheme.name)
        }
        activity.lifecycle.addObserver(InMemoryPersistence)
        activity.startActivityForResult(intent, requestCode)
    }

    fun handleActivityResult(requestCode: Int, resultCode: Int, resultListener: OnResultListener){
        val result = persistence.image
        if(result != null){
            resultListener.onUserComplete(result.image)
        }else{
            resultListener.onUserExited()
        }
        persistence.clearPersistence()
    }

    interface OnResultListener{

        fun onUserComplete(bitmap: Bitmap)

        fun onUserExited()

    }
}