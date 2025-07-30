package com.samiksha.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

object CommonAppPermission {
    var PERMISSIONS = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    private const val PERMISSION_ALL = 1

    var PERMISSIONS_ABOVE14 = arrayOf(
        Manifest.permission.READ_MEDIA_VIDEO,
        Manifest.permission.READ_MEDIA_AUDIO
    )
    private const val PERMISSION_ALL_ABOVE14 = 1


    /**
     * PERMISSION SECTION
     */
    fun requestPermissionGranted(context: Context?): Boolean {
        return if (!hasPermissions(context, *PERMISSIONS)) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as Activity?)!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //                showPermissionDisclaimerDialog(context);
                ActivityCompat.requestPermissions(context!!, PERMISSIONS, PERMISSION_ALL)
                false
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //                showPermissionDisclaimerDialog(context);
                ActivityCompat.requestPermissions(context!!, PERMISSIONS, PERMISSION_ALL)
                false
            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(context!!, PERMISSIONS, PERMISSION_ALL)
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                false
            }
        } else {
            // Permission has already been granted
            true
        }
    }


    /**
     * PERMISSION SECTION API Above 14
     */
    fun requestPermissionGrantedAbove14(context: Context?): Boolean {
        return if (!hasPermissionsabove14(context, *PERMISSIONS_ABOVE14)) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    (context as Activity?)!!,
                    Manifest.permission.READ_MEDIA_VIDEO
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //                showPermissionDisclaimerDialog(context);
                ActivityCompat.requestPermissions(context!!, PERMISSIONS_ABOVE14, PERMISSION_ALL_ABOVE14)
                false
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context!!,
                    Manifest.permission.READ_MEDIA_AUDIO
                )
            ) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //                showPermissionDisclaimerDialog(context);
                ActivityCompat.requestPermissions(context!!, PERMISSIONS_ABOVE14, PERMISSION_ALL_ABOVE14)
                false
            } else {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(context!!, PERMISSIONS_ABOVE14, PERMISSION_ALL_ABOVE14)
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                false
            }
        } else {
            // Permission has already been granted
            true
        }
    }


    private fun hasPermissionsabove14(context: Context?, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= 34 && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }

    private fun hasPermissions(context: Context?, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }
}
