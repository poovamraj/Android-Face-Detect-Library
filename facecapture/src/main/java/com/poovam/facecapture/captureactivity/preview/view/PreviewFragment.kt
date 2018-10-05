package com.poovam.facecapture.captureactivity.preview.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.poovam.facecapture.R
import com.poovam.facecapture.framework.persistence.InMemoryPersistence
import kotlinx.android.synthetic.main.fragment_preview.*

/**
 * Created by poovam-5255 on 9/29/2018.
 */

class PreviewFragment : Fragment() {

    companion object {
        val NAME = "PREVIEW_FRAGMENT"
    }

    private val persistence = InMemoryPersistence

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preview,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPersistenceAndSetImage()

        tryAgainButton.setOnClickListener {
            onTryAgainClicked()
        }

        proceed.setOnClickListener {
            val i = Intent()
            activity?.setResult(RESULT_OK, i)
            activity?.finish()
        }
    }

    private fun checkPersistenceAndSetImage(){
        val result = persistence.image
        if(result != null){
            setImage(result.image)
        }
    }

    private fun setImage(bitmap: Bitmap){
        previewImageView.setImageBitmap(bitmap)
    }

    private fun onTryAgainClicked(){
        activity?.onBackPressed()
    }
}