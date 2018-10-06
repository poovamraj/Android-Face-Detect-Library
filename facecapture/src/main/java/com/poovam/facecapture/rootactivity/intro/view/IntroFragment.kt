package com.poovam.facecapture.rootactivity.intro.view

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.poovam.facecapture.R
import com.poovam.facecapture.captureactivity.view.CaptureActivity
import com.poovam.facecapture.rootactivity.view.RootActivity
import kotlinx.android.synthetic.main.fragment_intro.*

/**
 * Created by poovam-5255 on 9/29/2018.
 */

internal class IntroFragment : Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_intro,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startVerification.setOnClickListener {
            activity?.startActivityForResult(Intent(context, CaptureActivity::class.java), RootActivity.CAPTURE_IMAGE_REQUEST_CODE)
        }
    }
}