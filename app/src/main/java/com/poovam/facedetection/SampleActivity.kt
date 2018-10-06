package com.poovam.facedetection

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.poovam.facecapture.FaceCapturer
import kotlinx.android.synthetic.main.activity_sample.*

/**
 * Created by poovam-5255 on 9/28/2018.
 * Sample activity showed to use demo of the SDK
 */
class SampleActivity : AppCompatActivity() {

    companion object {
        val REQUEST_CODE = 1
    }

    private val capturer = FaceCapturer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        setupButton()
    }

    private fun setupButton(){
        getStarted.setOnClickListener{
            onStartClicked()
        }
    }

    private fun onStartClicked(){
        capturer.startActivityForResult(this,REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE){
            capturer.handleActivityResult(requestCode,resultCode,object : FaceCapturer.OnResultListener{

                override fun onUserComplete(bitmap: Bitmap) {
                    resultImage.setImageBitmap(bitmap)
                }

                override fun onUserExited() {
                    resultImage.setImageResource(R.drawable.face_placeholder)
                    Toast.makeText(this@SampleActivity,getString(R.string.exit_message),Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}