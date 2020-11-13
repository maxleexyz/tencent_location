package xyz.max.tencent.location

import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodChannel
import xyz.max.tencent.tencent_location.MethodCallHandlerImpl
import xyz.max.tencent.tencent_location.StreamHandlerImpl
import xyz.max.tencent.tencent_location.TencentLocation

/** TencentLocationPlugin */
class TencentLocationPlugin : FlutterPlugin, ActivityAware {

    private var location: TencentLocation? = null
    private var methodCallHandler: MethodCallHandlerImpl? = null
    private var streamHandlerImpl: StreamHandlerImpl? = null

    private var activityBinding: ActivityPluginBinding? = null

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        location = TencentLocation(flutterPluginBinding.applicationContext,  /* activity= */null)
        initInstance(flutterPluginBinding.binaryMessenger)
    }


    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        if (methodCallHandler != null) {
            methodCallHandler!!.stopListening();
            methodCallHandler = null;
        }
        if (streamHandlerImpl != null) {
            streamHandlerImpl!!.stopListening();
            streamHandlerImpl = null;
        }
        location = null;
    }

    // MARK: -- ActivityAware START
    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityBinding = binding
        try {
            location!!.activity = binding.activity
            this.setup()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetachedFromActivityForConfigChanges() {
        this.detachActivity();
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        this.attachToActivity(binding);
    }

    override fun onDetachedFromActivity() {
        detachActivity()
    }

    // MARK: -- ActivityAware END
    private fun initInstance(binaryMessenger: BinaryMessenger) {
        methodCallHandler = MethodCallHandlerImpl(location!!)
        methodCallHandler!!.startListening(binaryMessenger)
        streamHandlerImpl = StreamHandlerImpl(location!!)
        streamHandlerImpl!!.startListening(binaryMessenger)
    }

    private fun setup() {
        activityBinding!!.addActivityResultListener(location!!)
        activityBinding!!.addRequestPermissionsResultListener(location!!)
    }

    private fun attachToActivity(binding: ActivityPluginBinding) {
        activityBinding = binding
        try {
            location!!.activity = binding.activity
            setup()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun detachActivity() {
        activityBinding!!.removeActivityResultListener(location!!)
        activityBinding!!.removeRequestPermissionsResultListener(location!!)
        activityBinding = null
        location!!.activity = null
    }


}
