package xyz.max.tencent.tencent_location

import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** TencentLocationPlugin */
class MethodCallHandlerImpl(private val location: TencentLocation) : MethodCallHandler {

    private val TAG = "MethodCallHandlerImpl"

    private var channel: MethodChannel? = null

    private val METHOD_CHANNEL_NAME = "tencent_location"

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {

        when (call.method) {
            "getPlatformVersion" -> {
                result.success("Android MAX ${android.os.Build.VERSION.RELEASE}")
            }
            "hasPermission" -> onHasPermission(result)
            "requestPermission" -> onRequestPermission(result)
            "getLocation" -> onGetLocation(result)
            else -> {
                result.notImplemented()
            }
        }
    }

    // 权限检查
    private fun onHasPermission(result: Result) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            result.success(1);
            return
        }

        if (location.checkPermissions()) {
            result.success(1);
        } else {
            result.success(0);
        }
    }
    // 权限请求
    private fun onRequestPermission(result: Result) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            result.success(1);
            return
        }

        location.result = result;
        location.requestPermissions();
    }

    // 获取坐标
    private fun onGetLocation(result: Result) {
        location.getLocationResult = result;
        if (!location.checkPermissions()) {
            location.requestPermissions();
        } else {
            location.startRequestingLocation();
        }
    }

    fun startListening(messenger: BinaryMessenger) {
        if (channel != null) {
            Log.wtf(TAG, "Setting a method call handler before the last was disposed.")
            stopListening()
        }
        channel = MethodChannel(messenger, METHOD_CHANNEL_NAME)
        channel!!.setMethodCallHandler(this)
    }

    /**
     * Clears this instance from listening to method calls.
     */
    fun stopListening() {
        if (channel == null) {
            Log.d(TAG, "Tried to stop listening when no MethodChannel had been initialized.")
            return
        }
        channel!!.setMethodCallHandler(null)
        channel = null
    }
}
