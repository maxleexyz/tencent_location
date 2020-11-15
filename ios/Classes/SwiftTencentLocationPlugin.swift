import Flutter
import UIKit
import TencentLBS

public class SwiftTencentLocationPlugin: NSObject, FlutterPlugin, TencentLBSLocationManagerDelegate {
  
  var locationManager: TencentLBSLocationManager?
  
  
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "tencent_location", binaryMessenger: registrar.messenger())
    let instance = SwiftTencentLocationPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }
  
  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "hasPermission":
      if self.isPermissionGranted() {
        result(self.isHighAccuracyPermitted() ? 1: 3)
      } else {
        result(0)
      }
      break;
    case "requestPermission":
      if self.isPermissionGranted() {
        result(self.isHighAccuracyPermitted() ? 1: 3)
      } else if CLLocationManager.authorizationStatus() == CLAuthorizationStatus.notDetermined {
        // TODO:
        self.requestPermission()
      } else {
        result(2)
      }
      break;
    case "initLocation":
      if call.arguments is String {
        self.initLocationManager(apiKey: call.arguments as! String)
      }
      
      break;
    case "getLocation":
      self.locationManager?.requestLocation(completionBlock: { (tLocation, error) in
        if let e = error {
          debugPrint(e.localizedDescription);
          return
        }
        if let tl: TencentLBSLocation = tLocation {
          let location: CLLocation = tl.location
          var map = Dictionary<String, Any>();
          map["provider"] = tl.province
          map["latitude"] = location.coordinate.latitude
          map["longitude"] = location.coordinate.longitude
          map["altitude"] = location.altitude
          map["accuracy"] = 0.0
          map["name"] = tl.name
          map["address"] = tl.address
          map["nation"] = tl.nation
          map["province"] = tl.province
          map["city"] = tl.city
          map["district"] = tl.district
          map["town"] = tl.town
          map["village"] = tl.village
          map["street"] = tl.street
          map["streetNo"] = tl.street_no
          map["areaStat"] = tl.areaStat
//          map["poiList"] = tl.poiList
          map["bearing"] = 0.0
          map["speed"] = 0.0
          map["time"] = 0
          map["elapsedRealtime"] = 0
          map["gpsRssi"] = 0
          map["indoorBuildingId"] = ""
          map["indoorBuildingFloor"] = ""
          map["indoorLocationType"] = 0
          map["direction"] = 0.0
          map["cityCode"] = ""
          map["cityPhoneCode"] = ""
          map["coordinateType"] = 0
          map["isMockGps"] = 0
          map["code"] = 200
          result(map)
        }
        
      })
      break;
    default:
      break;
    }
  }
  
  private func initLocationManager(apiKey: String) {
    self.locationManager = TencentLBSLocationManager()
    self.locationManager?.delegate = self
    // 对应于申请的apiKey
    self.locationManager?.apiKey = apiKey
    self.locationManager?.pausesLocationUpdatesAutomatically = true
    // 需要后台定位的话，可以设置此属性为YES。
    //    self.locationManager?.allowsBackgroundLocationUpdates = true
    // 如果需要POI信息的话，根据所需要的级别来设定，定位结果将会根据设定的POI级别来返回，如：
    //    self.locationManager?.requestLevel = TencentLBSRequestLevel.name
    
    // 申请的定位权限，得和在info.list申请的权限对应才有效
    let authorizationStatus = CLLocationManager.authorizationStatus()
    if (authorizationStatus == CLAuthorizationStatus.notDetermined) {
      self.locationManager?.requestWhenInUseAuthorization()
    }
  }
  
  private func isPermissionGranted() -> Bool {
    var isPermissionGranted = false
    
    let status = CLLocationManager.authorizationStatus()
    
    //    #if TARGET_OS_OSX
    //    if (status == kCLAuthorizationStatusAuthorized) {
    //      // Location services are available
    //      isPermissionGranted = true
    //    } else if #available(macOS 10.12, *) {
    //      if (status == kCLAuthorizationStatusAuthorizedAlways) {
    //        // Location services are available
    //        isPermissionGranted = true
    //      }
    //    }
    //    #else //if TARGET_OS_IOS
    if (status == CLAuthorizationStatus.authorizedWhenInUse ||
          status == CLAuthorizationStatus.authorizedAlways) {
      // Location services are available
      isPermissionGranted = true
    }
    //    #endif
    
    else if (status == CLAuthorizationStatus.denied ||
              status == CLAuthorizationStatus.restricted) {
      // Location services are requested but user has denied / the app is restricted from
      // getting location
      isPermissionGranted = false
    } else if (status == CLAuthorizationStatus.notDetermined) {
      // Location services never requested / the user still haven't decide
      isPermissionGranted = false
    } else {
      isPermissionGranted = false
    }
    
    return isPermissionGranted
  }
  
  
  func isHighAccuracyPermitted() -> Bool {
    #if __IPHONE_14_0
    if #available(iOS 14.0, *) {
      let accuracy = CLLocationManager().accuracyAuthorization
      if (accuracy == CLAccuracyAuthorization.reducedAccuracy) {
        return false
      }
    }
    #endif
    return true
  }
  
  func requestPermission() {
    #if TARGET_OS_OSX
    if Bundle.main.object(forInfoDictionaryKey: "NSLocationWhenInUseUsageDescription") != nil {
      if #available(macOS 10.15, *) {
        CLLocationManager().requestAlwaysAuthorization()
      }
    }
    #else
    if Bundle.main.object(forInfoDictionaryKey: "NSLocationWhenInUseUsageDescription") != nil {
      CLLocationManager().requestWhenInUseAuthorization()
    } else if Bundle.main.object(forInfoDictionaryKey: "NSLocationAlwaysUsageDescription") != nil {
      CLLocationManager().requestAlwaysAuthorization()
    }
    #endif
    //        else {
    //          [NSException raise:NSInternalInconsistencyException format:
    //              @"To use location in iOS8 and above you need to define either "
    //              "NSLocationWhenInUseUsageDescription or NSLocationAlwaysUsageDescription in the app "
    //              "bundle's Info.plist file"];
    //        }
  }
  
}

