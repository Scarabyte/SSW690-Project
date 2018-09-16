# Design Description

This document presents the high level design of the Back Seat Driver application. The intent is to provide sufficient details such that consistency and organization is established for the implementation to be successful.

<div style="color:#997000;background-color:#FEEEBB;border=1px;">Warning: This document is a "living" document and will be updated as the Back Seat Driver application prototype evolves.</div>

## 1. References

[Architecture Description](ArchitectureDescription.md): Presents the architecture of the Back Seat Driver application.

## 2. Design

The design of the Back Seat Driver application is a decomposition of the architectural elements identifed in the [Architecture Description](ArchitectureDescription.md) document

### 2.1 LDWSProcessor

Responsibilities:
  * Main Controller for the Lane Departure Warning function
  * Contains the algorithms to determine the lane of travel and whether the vehicle is about to depart the lane of travel  
  * Responsible for activating device's camera and establishing logging mechanism by initializing the OpenCV Library modules
  * Invoked by MainActivity class when activated by the user or upon start up of the application

### 2.2 LaneDetector

Responsibilities:
  * Leverage the OpenCV library modules to apply Gaussian filter, Hough transformation and invoking Canny Edge detector
  * Invoked by the LDWSProcessor class when new frame of video is ready for processing
  

### 2.3 DepartureNotifier

  * Performs function to notify user of a lane departure by providing audible and possibly visual warning message
  * Invoked by the LDWSProcessor class when vehicle departs the lane, otherwise stays idle 

### 2.4 MainActivity

Responsibilities:
  * Start up and shut down the Back Seat Driver application.
  * Load and initialize the OpenCV library and touch/camera interfaces.
  * Invoke the LDWSProcessor to perform lane departure warning.

The following diagram depicts the design of the MainActivity class.

<p align="center"><img src="MainActivity.png"></p>

The MainActivity class extends the base Android.App.Activity class to form the basis of an Android application. It implements the OnTouchListener and CvCameraViewListener interfaces to interface to the touchpad and the camera devices. It uses the facilities of the BaseLoaderCallback and OpenCVLoader classes to perform loading of the OpenCV library, specifically loading the library from an installed location or from within the application itself. The CameraBridgeViewBase class from OpenCV is used to interface with the camera device, which allows the application to control the enabling/disabling of the camera video feed and to obtain characteristics of the video frame to be processed. Finally, it uses the LDWSProcessor class to invoke the specialized functionality of the Lane Departure Warning functionality.

## 3. Summary

TBD
