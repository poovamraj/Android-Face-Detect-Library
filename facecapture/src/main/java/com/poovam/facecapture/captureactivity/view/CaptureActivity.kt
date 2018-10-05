package com.poovam.facecapture.captureactivity.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.FragmentTransaction
import android.view.MenuItem
import com.poovam.facecapture.R
import com.poovam.facecapture.captureactivity.presenter.CapturePresenter
import com.poovam.facecapture.captureactivity.preview.view.PreviewFragment
import com.poovam.facecapture.framework.baseactivity.BaseActivity
import com.poovam.facecapture.framework.camera.handler.FotoApparatCameraHandler
import com.poovam.facecapture.framework.camera.handler.Handler
import kotlinx.android.synthetic.main.activity_capture.*


/**
 * Created by poovam-5255 on 9/28/2018.
 * The activity which will have the camera instance
 */
class CaptureActivity : BaseActivity(), Handler.PermissionEvents, CapturePresenter.PresenterEvents{

    var permissionView: PermissionRationaleView? = null

    lateinit var presenter: CapturePresenter

    private val previewFragment = PreviewFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)

        val cameraHandler = FotoApparatCameraHandler(this, cameraView)
        cameraHandler.setPermissionEventsListener(this)
        lifecycle.addObserver(cameraHandler)

        presenter = CapturePresenter(cameraHandler)
        presenter.eventListener = this
        lifecycle.addObserver(presenter)

        setTitle()
        showUpButton()
        setCaptureTheme()
    }

    private fun setCaptureTheme(){
        changeActionBarColor("#00000000")
        changeStatusBarColor("#00000000")
    }

    private fun setPreviewTheme(){
        setupTheme()
    }

    override fun onSuccess() {
        addPreviewFragment()
    }

    override fun onError(error: Throwable) {

    }

    private fun addPreviewFragment(){
        setPreviewTheme()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.add(R.id.container, previewFragment, PreviewFragment.NAME)
        fragmentTransaction.commit()
    }

    private fun removePreviewFragment(){
        setCaptureTheme()
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.remove(previewFragment)
        fragmentTransaction.commit()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onPermissionResult(requestCode,permissions,grantResults)
    }

    override fun onPermissionAccepted() {
        permissionView?.dismiss()
    }

    override fun onPermissionDenied() {
        finish()
    }

    override fun onPermissionDeniedDontShowAgain(){
        permissionView = PermissionRationaleView(this)
        permissionView?.show()
        permissionView?.onAcceptClicked = {
            redirectToSettings()
        }
        permissionView?.onDenyClicked = {
            finish()
        }
    }

    private fun redirectToSettings(){
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", this.packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId){
            android.R.id.home -> {
                backPressHandling()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        backPressHandling()
    }

    private fun resumeCameraView(){
        removePreviewFragment()
        presenter.resumeCamera()
    }

    private fun backPressHandling(){
        if(supportFragmentManager.findFragmentByTag(PreviewFragment.NAME) != null){
            resumeCameraView()
        }else{
            finish()
        }
    }
}