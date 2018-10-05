package com.poovam.facecapture.framework.camera.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.shouldShowRequestPermissionRationale
import android.support.v4.content.ContextCompat




/**
 * Created by poovam-5255 on 9/29/2018.
 */
class PermissionDelegate(private val activity: Activity) {

    private val REQUEST_CODE = 10

    fun hasCameraPermission(): Boolean {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.CAMERA
        )
        return permissionCheckResult == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission(){
        ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CODE
        )
    }

    fun hasTickedDontAskAgain(): Boolean{
        return !shouldShowRequestPermissionRationale(activity,Manifest.permission.CAMERA)
    }

    fun resultGranted(requestCode: Int,
                      permissions: Array<out String>,
                      grantResults: IntArray): Boolean {


        if (requestCode != REQUEST_CODE || grantResults.isEmpty() || permissions[0] != Manifest.permission.CAMERA) {
            return false
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }
}