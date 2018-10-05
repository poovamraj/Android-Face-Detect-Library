package com.poovam.facecapture.framework.baseactivity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.poovam.facecapture.R


/**
 * Created by poovam-5255 on 9/28/2018.
 * The base activity to which fragments are going to be added and [com.poovam.facecapture.captureactivity.CaptureActivity]
 * will be called from
 */
open class BaseActivity : AppCompatActivity() {

    protected fun setupTheme(){
        setTitle()
        setThemeStatusColor()
        setThemeActionBarColor()
        showUpButton()
    }

    protected fun showUpButton(){
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    protected fun setTitle(){
        supportActionBar?.title = getString(R.string.identity_verification)
    }

    protected fun setThemeActionBarColor(){
        supportActionBar?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this,R.color.faceCaptureActionBar)))
    }

    protected fun setThemeStatusColor(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this,R.color.faceCaptureStatusBarColor)
        }
    }

    /**
     * @param color - Hex value passed as string
     * If the sent color cant be parsed, default white is set
     */
    fun changeActionBarColor(color: String?){
        supportActionBar?.setBackgroundDrawable(ColorDrawable(getColor(color)))
    }

    /**
     * @param color - Hex value passed as string
     * If the sent color cant be parsed, default white is set
     */
    fun changeStatusBarColor(color : String?){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = getColor(color)
        }
    }

    /**
     * @param color - Hex value passed as string
     * Util method used to get the color and default white is used if
     * the Hex string cannot be parsed or null
     */
    private @ColorInt
    fun getColor(color: String?): Int{
        val defaultColor = R.color.face_capture_sdk_theme_white
        return try {
            if (color!=null) Color.parseColor(color) else ContextCompat.getColor(this,defaultColor)
        }catch (e: IllegalArgumentException){
            ContextCompat.getColor(this,defaultColor)
        }
    }
}