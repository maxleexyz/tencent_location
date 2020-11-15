import 'dart:async';

import 'package:flutter/material.dart';
import 'package:tencent_location/tencent_location.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  initPlatformState() {
    TencentLocation().initLocation("YLOBZ-LU6EI-RCLGS-5K344-SA5U7-V7BAQ");
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: [
              FlatButton(
                child: Text("init"),
                onPressed: () async {
                  await TencentLocation().initLocation("");
                },
              ),
              FlatButton(
                child: Text("requestPermission"),
                onPressed: () async {
                  final permission = await TencentLocation().requestPermission();
                  debugPrint("requestPermission: ${permission.toString()}");
                },
              ),
              FlatButton(
                child: Text("hasPermission"),
                onPressed: () async {
                  final permission = await TencentLocation().hasPermission();
                  debugPrint("hasPermission: ${permission.toString()}");
                },
              ),
              FlatButton(
                child: Text("getLocation"),
                onPressed: () async {
                  final location = await TencentLocation().getLocation();
                  debugPrint("location: ${location.toString()}");
                },
              ),
            ],
          ),
        ),
      ),
    );
  }
}
