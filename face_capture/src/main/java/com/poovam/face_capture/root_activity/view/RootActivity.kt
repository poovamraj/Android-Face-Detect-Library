package com.poovam.face_capture.root_activity.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.poovam.face_capture.R
import com.poovam.face_capture.framework.base_activity.BaseActivity
import com.poovam.face_capture.root_activity.intro.view.IntroFragment
import com.poovam.face_capture.root_activity.presenter.RootPresenter

/**
 * Created by poovam-5255 on 9/30/2018.
 * Using activity and fragment so that in future we could provide support to add custom fragments
 */

class RootActivity : BaseActivity(){

    companion object {
        val CAPTURE_IMAGE_REQUEST_CODE = 20
    }

    private val introFragment = IntroFragment()

    private val presenter = RootPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        setupTheme()
        addIntroFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CAPTURE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            onImageReturnSuccess()
        }
    }

    private fun onImageReturnSuccess(){
        val i = Intent()
        setResult(Activity.RESULT_OK, i)
        finish()
    }

    private fun addIntroFragment(){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragmentContainer, introFragment)
        fragmentTransaction.commit()
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val i = Intent()
        setResult(Activity.RESULT_CANCELED, i)
        finish()
    }
}