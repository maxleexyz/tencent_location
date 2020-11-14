# tencent_location

A new Flutter plugin.

## Getting Started

This project is a starting point for a Flutter
[plug-in package](https://flutter.dev/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

For help getting started with Flutter, view our
[online documentation](https://flutter.dev/docs), which offers tutorials,
samples, guidance on mobile development, and a full API reference.

### iOS

需要在info.plist中追加 NSLocationWhenInUseUsageDescription 或NSLocationAlwaysUsageDescription 字段，以申请定位权限。

allowsBackgroundLocationUpdates表示是否允许后台定位。默认为 NO。只在iOS 9.0 及以后起作用。设置为 YES 的时候必须保证 Background Modes 中的 Location updates 处于选中状态，否则会抛出异常。注意，如果不设置为YES，不需要申请该权限，否则会审核不通过!

