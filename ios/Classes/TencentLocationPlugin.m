#import "TencentLocationPlugin.h"
#if __has_include(<tencent_location/tencent_location-Swift.h>)
#import <tencent_location/tencent_location-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "tencent_location-Swift.h"
#endif

@implementation TencentLocationPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftTencentLocationPlugin registerWithRegistrar:registrar];
}
@end
