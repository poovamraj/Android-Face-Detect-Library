package com.poovam.facecapture.captureactivity.view

import android.app.Dialog
import android.content.Context
import android.view.Window
import com.poovam.facecapture.R
import kotlinx.android.synthetic.main.dialog_permission.*

/**
 * Created by poovam-5255 on 9/29/2018.
 * This is the dialog view that will show if the user has denied permission with "Dont Show Again"
 */

class PermissionRationaleView(context: Context?){

    private val dialog: Dialog = Dialog(context)

    var onAcceptClicked: ((Dialog)-> Unit)? = null

    var onDenyClicked: ((Dialog)-> Unit)? = null

    init {
        initDialogSetup()
        initViews()
    }

    private fun initDialogSetup(){
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_permission)
        dialog.setCancelable(false)
    }

    private fun initViews(){
        dialog.accept.setOnClickListener { onAcceptClicked?.invoke(dialog) }
        dialog.deny.setOnClickListener { onDenyClicked?.invoke(dialog) }
    }

    fun show(){
        dialog.show()
    }

    fun dismiss(){
        if(dialog.isShowing){
            try {
                dialog.dismiss()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}