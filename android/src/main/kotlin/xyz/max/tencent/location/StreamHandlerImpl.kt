package xyz.max.tencent.tencent_location


import android.util.Log
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.EventChannel.StreamHandler
import io.flutter.plugin.common.MethodChannel


/** TencentLocationPlugin */
class StreamHandlerImpl(private val location: TencentLocation): StreamHandler {

  private val TAG = "StreamHandlerImpl"

  private var channel: EventChannel? = null

  private val STREAM_CHANNEL_NAME = "tencent_location/stream"

  override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
    TODO("Not yet implemented")
  }

  override fun onCancel(arguments: Any?) {
    TODO("Not yet implemented")
  }

  /**
   * Registers this instance as a stream events handler on the given
   * `messenger`.
   */
  fun startListening(messenger: BinaryMessenger) {
    if (channel != null) {
      Log.wtf(TAG, "Setting a method call handler before the last was disposed.")
      stopListening()
    }
    channel = EventChannel(messenger, STREAM_CHANNEL_NAME)
    channel!!.setStreamHandler(this)
  }

  /**
   * Clears this instance from listening to stream events.
   */
  fun stopListening() {
    if (channel == null) {
      Log.d(TAG, "Tried to stop listening when no EventChannel had been initialized.")
      return
    }
    channel!!.setStreamHandler(null)
    channel = null
  }

}
