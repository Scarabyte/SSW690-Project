# Architecture Description

## 1. Scope
This Architecture Description provides a high level overview and details on the architecture of the Back Seat Driver application for the Android platform. The Back Seat Driver application will utilize the smart phone's camera to process a video of the oncoming road/surface in order to detect the travel lane and warn the operator when the vehicle is departing the lane (i.e., Lane Departure Warning System, or LDWS). This document describes the internal organization of the application from multiple perspectives to aid in the understanding, development, testing and maintenance of the application.

## 2. Architecture Goals & Constraints
The architecture is largely constrained by the Android operating system and application environment, and the OpenCV library that will be heavily leveraged to perform video capture, processing and output. In addition to these constraints, the Back Seat Driver team has established the following goals.

  * __Extensibility__: The architecture should facilitate not only quick delivery of a LDWS function, but allow for developers to quickly and easily extend the functionality for other purposes. Other purposes could include traffic sign recognition and forward collision alerting.
  * __Modularity__: The architecture should support adequate separation of functionality and responsibility such that the development and testing can be partitioned and completed independently from other parts of the application.
  * __Performance__: The architecture should optimize performance above other quality characteristics. This is because a poor performing application will fail to achieve the desired functionality, regardless of whether extensibility and modularity have been achieved.

## 3. Logical Architecture
TBD: Assigned to Keith

## 4. Process Architecture
TBD: Assigned to Adam

## 5. Development Architecture
TBD: Assigned to Sapana

## 6. Scenarios
TBD: Assigned to Rak

