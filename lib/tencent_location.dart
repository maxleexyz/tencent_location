import 'dart:async';

import 'package:flutter/services.dart';
import 'types.dart';

class TencentLocation {
  static const MethodChannel _methodChannel = const MethodChannel('tencent_location');

  Future<PermissionStatus> hasPermission() async {
    final int result = await _methodChannel.invokeMethod('hasPermission');
    return _parsePermissionStatus(result);
  }

  Future<PermissionStatus> requestPermission() async {
    final int result = await _methodChannel.invokeMethod('requestPermission');
    return _parsePermissionStatus(result);
  }

  Future initLocation(String apiKey) async {
    final int result = await _methodChannel.invokeMethod('initLocation', apiKey);
    return result;
  }

  Future<LocationData> getLocation() async {
    final Map<String, dynamic> resultMap = await _methodChannel.invokeMapMethod('getLocation');
    return LocationData.fromMap(resultMap);
  }

  static PermissionStatus _parsePermissionStatus(int result) {
    switch (result) {
      case 0:
        return PermissionStatus.denied;
      case 1:
        return PermissionStatus.granted;
      case 2:
        return PermissionStatus.deniedForever;
      case 3:
        return PermissionStatus.grantedLimited;
      default:
        throw PlatformException(
          code: 'UNKNOWN_NATIVE_MESSAGE',
          message: 'Could not decode parsePermissionStatus with $result',
        );
    }
  }
}
