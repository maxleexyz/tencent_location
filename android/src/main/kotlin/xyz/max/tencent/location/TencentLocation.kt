package xyz.max.tencent.tencent_location

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.tencent.map.geolocation.TencentLocation
import com.tencent.map.geolocation.TencentLocationListener
import com.tencent.map.geolocation.TencentLocationManager
import io.flutter.plugin.common.EventChannel.EventSink
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry


/** TencentLocation */
class TencentLocation(private val applicationContext: Context, var activity: Activity?) : PluginRegistry.RequestPermissionsResultListener, PluginRegistry.ActivityResultListener{

    private val TAG = "TencentLocation"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34

    // 权限状态
    private var locationPermissionState = 0

    // Store result until a permission check is resolved
    var result: Result? = null

    // Store result until a location is getting resolved
    var getLocationResult: Result? = null

    var events: EventSink? = null


    private var locationManager: TencentLocationManager? = null;

    init {
//        this.locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    }

//    fun setActivity(@Nullable activity: Activity?) {
//        this.activity = activity
//        if (this.activity != null) {
//            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
//            mSettingsClient = LocationServices.getSettingsClient(activity)
//            createLocationCallback()
//            createLocationRequest()
//            buildLocationSettingsRequest()
//        } else {
//            if (mFusedLocationClient != null) {
//                mFusedLocationClient.removeLocationUpdates(mLocationCallback)
//            }
//            mFusedLocationClient = null
//            mSettingsClient = null
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && locationManager != null) {
//                locationManager.removeNmeaListener(mMessageListener)
//                mMessageListener = null
//            }
//            locationManager = null
//        }
//    }

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
        this.locationPermissionState = ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION) +
                ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.CHANGE_WIFI_STATE)

//                ActivityCompat.checkSelfPermission(activity!!,
//                Manifest.permission.ACCESS_FINE_LOCATION)
        return this.locationPermissionState === PackageManager.PERMISSION_GRANTED
//        return
    }


    fun requestPermissions() {
        if (activity == null) {
            throw ActivityNotFoundException()
        }
        if (checkPermissions()) {
            result!!.success(1)
            return
        }
        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CHANGE_WIFI_STATE), REQUEST_PERMISSIONS_REQUEST_CODE);

//        ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                REQUEST_PERMISSIONS_REQUEST_CODE)
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
        Log.d(TAG, "startRequestingLocation")

        if (locationManager == null) {
            locationManager = TencentLocationManager.getInstance(applicationContext)
        }
        Log.d(TAG, "locationManager ${locationManager == null}")

        locationManager!!.requestSingleFreshLocation(null, object : TencentLocationListener {

            override fun onLocationChanged(location: TencentLocation, error: Int, reason: String?) {
                Log.d(TAG, "onLocationChanged")

                when (error) {
                    0 -> {
                        val map = HashMap<String, Any?>();
                        map["provider"] = location.provider
                        map["latitude"] = location.latitude
                        map["longitude"] = location.longitude
                        map["altitude"] = location.altitude
                        map["accuracy"] = location.accuracy
                        map["name"] = location.name
                        map["address"] = location.address
                        map["nation"] = location.nation
                        map["province"] = location.province
                        map["city"] = location.city
                        map["district"] = location.district
                        map["town"] = location.town
                        map["village"] = location.village
                        map["street"] = location.street
                        map["streetNo"] = location.streetNo
                        map["areaStat"] = location.areaStat
                        map["poiList"] = location.poiList
                        map["bearing"] = location.bearing
                        map["speed"] = location.speed
                        map["time"] = location.time
                        map["elapsedRealtime"] = location.elapsedRealtime
                        map["gpsRssi"] = location.gpsRssi
                        map["indoorBuildingId"] = location.indoorBuildingId
                        map["indoorBuildingFloor"] = location.indoorBuildingFloor
                        map["indoorLocationType"] = location.indoorLocationType
                        map["direction"] = location.direction
                        map["cityCode"] = location.cityCode
                        map["cityPhoneCode"] = location.cityPhoneCode
                        map["coordinateType"] = location.coordinateType
                        map["isMockGps"] = location.isMockGps
                        map["code"] = 200

                        if (getLocationResult != null) {
                            getLocationResult!!.success(map)
                            getLocationResult = null;
                        }
                    }
                    else -> {
                        Log.d(TAG, "onLocationChanged: error:$error reason:$reason")
                    }
                }
            }

            override fun onStatusUpdate(var1: String?, var2: Int, var3: String?) {
                Log.d(TAG, "onStatusUpdate")

                Log.d(TAG, "onStatusUpdate: var1:$var1 var2:$var2 var3:$var3")

            }

        }, Looper.getMainLooper())

//        locationManager!!.requestSingleFreshLocation(null, this, Looper.getMainLooper())
    }


}
