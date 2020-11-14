/// The response object of [Location.getLocation] and [Location.onLocationChanged]
///
/// speedAccuracy cannot be provided on iOS and thus is always 0.
class LocationData {
  LocationData._(
    this.provider,
    this.latitude,
    this.longitude,
    this.altitude,
    this.accuracy,
    this.name,
    this.address,
    this.nation,
    this.province,
    this.city,
    this.district,
    this.town,
    this.village,
    this.street,
    this.streetNo,
    this.areaStat,
    this.bearing,
    this.speed,
    this.time,
    this.elapsedRealtime,
    this.gPSRssi,
    this.indoorBuildingId,
    this.indoorBuildingFloor,
    this.indoorLocationType,
    this.direction,
    this.cityCode,
    this.cityPhoneCode,
    this.coordinateType,
    this.isMockGps,
  );

  factory LocationData.fromMap(Map<String, dynamic> dataMap) {
    return LocationData._(
      dataMap['provider'],
      dataMap['latitude'],
      dataMap['longitude'],
      dataMap['altitude'],
      dataMap['accuracy'],
      dataMap['name'],
      dataMap['address'],
      dataMap['nation'],
      dataMap['province'],
      dataMap['city'],
      dataMap['district'],
      dataMap['town'],
      dataMap['village'],
      dataMap['street'],
      dataMap['streetNo'],
      dataMap['areaStat'],
      dataMap['bearing'],
      dataMap['speed'],
      dataMap['time'],
      dataMap['elapsedRealtime'],
      dataMap['gPSRssi'],
      dataMap['indoorBuildingId'],
      dataMap['indoorBuildingFloor'],
      dataMap['indoorLocationType'],
      dataMap['direction'],
      dataMap['cityCode'],
      dataMap['cityPhoneCode'],
      dataMap['coordinateType'],
      dataMap['isMockGps'],
    );
  }

  final String provider;

  final double latitude;

  final double longitude;

  final double altitude;

  final double accuracy;

  final String name;

  final String address;

  final String nation;

  final String province;

  final String city;

  final String district;

  final String town;

  final String village;

  final String street;

  final String streetNo;

  final int areaStat;

  // final List<TencentPoi> poiList;

  final double bearing;

  final double speed;

  final int time;

  final int elapsedRealtime;

  final int gPSRssi;

  final String indoorBuildingId;

  final String indoorBuildingFloor;

  final int indoorLocationType;

  final double direction;

  final String cityCode;

  final String cityPhoneCode;

  final int coordinateType;

  final int isMockGps;

  @override
  String toString() => 'LocationData<lat: $latitude, long: $longitude>';


  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LocationData &&
          runtimeType == other.runtimeType &&
          latitude == other.latitude &&
          longitude == other.longitude &&
          accuracy == other.accuracy &&
          altitude == other.altitude &&
          speed == other.speed &&
          // speedAccuracy == other.speedAccuracy &&
          // heading == other.heading &&
          time == other.time;

  @override
  int get hashCode =>
      latitude.hashCode ^
      longitude.hashCode ^
      accuracy.hashCode ^
      altitude.hashCode ^
      speed.hashCode ^
      // speedAccuracy.hashCode ^
      // heading.hashCode ^
      time.hashCode;
}

/// Precision of the Location. A lower precision will provide a greater battery
/// life.
///
/// https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest
/// https://developer.apple.com/documentation/corelocation/cllocationaccuracy?language=objc
enum LocationAccuracy {
  /// To request best accuracy possible with zero additional power consumption
  powerSave,

  /// To request "city" level accuracy
  low,

  /// To request "block" level accuracy
  balanced,

  /// To request the most accurate locations available
  high,

  /// To request location for navigation usage (affect only iOS)
  navigation,
}

// Status of a permission request to use location services.
enum PermissionStatus {
  /// The permission to use location services has been granted for high accuracy.
  granted,

  /// The permission has been granted but for low accuracy. Only valid on iOS 14+.
  grantedLimited,

  /// The permission to use location services has been denied by the user. May
  /// have been denied forever on iOS.
  denied,

  /// The permission to use location services has been denied forever by the
  /// user. No dialog will be displayed on permission request.
  deniedForever
}
