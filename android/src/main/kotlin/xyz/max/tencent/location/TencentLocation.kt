package xyz.max.tencent.tencent_location

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry


/** TencentLocation */
class TencentLocation(private val applicationContext: Context, var activity: Activity?) : PluginRegistry.RequestPermissionsResultListener, PluginRegistry.ActivityResultListener {

    private val TAG = "TencentLocation"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    // 权限状态
    private var locationPermissionState = 0

    // Store result until a permission check is resolved
    var result: Result? = null

    // Store result until a location is getting resolved
    var getLocationResult: Result? = null

    var events: EventSink? = null

//  init {
//        this.locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

//  }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray): Boolean {
        return onRequestPermissionsResultHandler(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        TODO("Not yet implemented")
    }


    /**
     * Return the current state of the permissions needed.
     * 返回当前权限状态
     */
    fun checkPermissions(): Boolean {
        if (activity == null) {
            throw ActivityNotFoundException()
        }
        this.locationPermissionState = ActivityCompat.checkSelfPermission(activity!!,
                Manifest.permission.ACCESS_FINE_LOCATION)
        return this.locationPermissionState === PackageManager.PERMISSION_GRANTED
    }


    fun requestPermissions() {
        if (activity == null) {
            throw ActivityNotFoundException()
        }
        if (checkPermissions()) {
            result!!.success(1)
            return
        }
        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_REQUEST_CODE)
    }

    // 权限请求处理
    fun onRequestPermissionsResultHandler(requestCode: Int, permissions: Array<String>, grantResults: IntArray): Boolean {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE &&
                permissions.size == 1 &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Checks if this permission was automatically triggered by a location request
                if (getLocationResult != null || events != null) {
                    startRequestingLocation()
                }
                if (result != null) {
                    result!!.success(1)
                    result = null
                }
            } else {
                if (!shouldShowRequestPermissionRationale()) {
                    sendError("PERMISSION_DENIED_NEVER_ASK",
                            "Location permission denied forever - please open app settings", null)
                    if (result != null) {
                        result!!.success(2)
                        result = null
                    }
                } else {
                    sendError("PERMISSION_DENIED", "Location permission denied", null)
                    if (result != null) {
                        result!!.success(0)
                        result = null
                    }
                }
            }
            return true
        }
        return false
    }

    fun shouldShowRequestPermissionRationale(): Boolean {
        return shouldShowRequestPermissionRationale(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun sendError(errorCode: String, errorMessage: String, errorDetails: Any?) {
        if (getLocationResult != null) {
            getLocationResult!!.error(errorCode, errorMessage, errorDetails)
            getLocationResult = null
        }
        if (events != null) {
            events!!.error(errorCode, errorMessage, errorDetails)
            events = null
        }
    }


    fun startRequestingLocation() {

    }

}
