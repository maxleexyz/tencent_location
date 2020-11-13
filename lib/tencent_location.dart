
import 'dart:async';

import 'package:flutter/services.dart';

class TencentLocation {
  static const MethodChannel _channel =
      const MethodChannel('tencent_location');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
