package com.poovam.facecapture.captureactivity.previewfragment.view

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.poovam.facecapture.R
import com.poovam.facecapture.captureactivity.previewfragment.presenter.PreviewPresenter
import kotlinx.android.synthetic.main.fragment_preview.*

/**
 * Created by poovam-5255 on 9/29/2018.
 */

class PreviewFragment : Fragment(), PreviewPresenter.PreviewPresenterEvents {

    private val presenter = PreviewPresenter(this)

    companion object {
        val NAME = "PREVIEW_FRAGMENT"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preview,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.addObserver(presenter)

        tryAgainButton.setOnClickListener {
            onTryAgainClicked()
        }

        proceed.setOnClickListener {
            val i = Intent()
            activity?.setResult(RESULT_OK, i)
            activity?.finish()
        }
    }

    override fun onImageSuccess(image: Bitmap) {
        previewImageView.setImageBitmap(image)
    }

    private fun onTryAgainClicked(){
        activity?.onBackPressed()
    }
}