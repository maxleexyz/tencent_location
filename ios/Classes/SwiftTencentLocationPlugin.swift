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
          map["poiList"] = tl.poiList
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
          self.locationManager?.stopUpdatingLocation()
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
}

