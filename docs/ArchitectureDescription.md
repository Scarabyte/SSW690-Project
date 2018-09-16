# Architecture Description

## 1. Scope
This Architecture Description provides a high level overview and details on the architecture of the Back Seat Driver application for the Android platform. The Back Seat Driver application will utilize the smart phone's camera to process a video of the oncoming road/surface in order to detect the travel lane and warn the operator when the vehicle is departing the lane (i.e., Lane Departure Warning System, or LDWS). This document describes the internal organization of the application from multiple perspectives to aid in the understanding, development, testing and maintenance of the application.

## 2. Architecture Goals & Constraints
The architecture is largely constrained by the Android operating system and application environment, and the OpenCV library that will be heavily leveraged to perform video capture, processing and output. In addition to these constraints, the Back Seat Driver team has established the following goals.

  * __Extensibility__: The architecture should facilitate not only quick delivery of a LDWS function, but allow for developers to quickly and easily extend the functionality for other purposes. Other purposes could include traffic sign recognition and forward collision alerting.
  * __Modularity__: The architecture should support adequate separation of functionality and responsibility such that the development and testing can be partitioned and completed independently from other parts of the application.
  * __Performance__: The architecture should optimize performance above other quality characteristics. This is because a poor performing application will fail to achieve the desired functionality, regardless of whether extensibility and modularity have been achieved.

## 3. Logical Architecture
In Progress: Assigned to Keith

The following diagram depicts the logical architecture of the software that operates the Back Seat Driver and LDWS functionality. The system utilizes a layered architecture where interaction among the architectural elements are strictly with the layers above and below. The architecture significantly leverages the standard Android application environment and the services provided by the OpenCV library. These services in libraries, then, execute on top of the Android operating system. Coupled with the Android operating system are specific drivers for hardware devices, in particular the speaker for audio notifications, the screen for visual output, the touchpad for user interaction, storage for capturing log messages and functional data, and the camera for video input.

<img src="Software Architecture Block Diagram.png">

### 3.1 Back Seat Driver Application Architecture

The following diagram depicts the internal architecture of the Back Seat Driver application.

<p align="center"><img src="Back Seat Driver App Architecture.png" width="500px"></p>

#### 3.1.1 MainActivity Class

The MainActivity class is derived from the android.app.Activity class and provides the application framework and fundamental operation. This includes initializing the services and libraries needed to perform processing, establishing application run-time characteristics such as "keep screen on" and ensuring that everything is shut down correctly when the application is terminated. The MainActivity class will vector processing to the LDWSProcessor class in order to invoke the lane departure warning functionality.

#### 3.1.2 LDWSProcessor Class

The LDWSProcessor class is the main controller for the Lane Departure Warning function. It contains the main algorithms to determine the lane of travel and whether the vehicle is on a trajectory to depart the lane of travel. It is invoked by the MainActivity class upon application startup or activation by the user and is responsible for intializing the appropriate resources to perform the function. This includes initializing the OpenCV library modules, activating the device's camera and establishing a logging mechanism. The LDWSProcessorClass leverages two additional classes for complete functionality - the LaneDetector class, which processes images to determine the lane of travel and the DepartureNotifier class, which audibly and possibly visually notifies the driver of a lane departure warning.

#### 3.1.3 LaneDetector Class

The LaneDetector class is responsible for performing image and algorithmic processing in order to detect the lane markers or boundaries of the travel lane. This class leverages the OpenCV library modules to perform filtering and processing such as applying a Gaussian filter, Hough transformation and invoking a Canny Edge Detector. The LaneDetector class is invoked by the LDWSProcessor class when a new frame of video is ready for processing. The output provided back to the LaneDetector class is a set of data structures that represent the detected lane of travel.

#### 3.1.4 DepartureNotifier Class

The DepartureNotifier class is the function that performs audible and possibly visual notification of a lane departure warning. The class interfaces with the Android's speaker device interface to issue the audible warning. If a visual notification is implemented, the class will invoke another Activity to overlay the visual warning on the application window. The DepartureNotifier class is invoked by the LDWSProcessor class when a warning needs to be issued; it is otherwise idle.

### 3.2 Correlation of Architecture to Goals

__Extensibility__ is accomlished by allowing the MainActivity to invoke additional "processor" classes, operating in parallel to the LDWSProcess class. For example, if a traffic sign recognition function is desired to be added, a new TrafficSignProcessor class (and associated classes) can be created. The MainActivity would then be extended to invoke the TrafficSignalProcessor class in addition to the LDWSProcessor class.

__Modularity__ is accomlished by partitioning the major sub-functions of the application functionality into separate classes. It is possible to have separate team members working independently on the LDWSProcessor, LaneDetector and DepartureNotifier classes without signficant impact to each other, as long as the public interfaces to the classes are well-designed and strictly enforced.

__Performance__ is accomplished by streamlining the processing into a serial/pipeline methodology whereby the MainActivity invokes the LDWSProcessor class, which then invokes the LaneDeparture class and when necessary the DepartureNotifier class. Performance of each of these sub-functions may then be easily measured and corrective action taken if needed to optimize their execution.

## 4. Process Architecture
TBD: Assigned to Adam

The following diagram the high-level process architecture of the Back Seat Driver application: a set of independently-executing processes that invoke each other in response to _events_ in the application space. The processes themselves contain sets of independent _routines_ that can be called individually, and combine to produce an executable unit of the respective function.

<p align="center"><img src="SSW690ProcessArchitecture.png" width="500px"></p>

### 4.1 Main Activity Process

This process is invoked by android.app.Activity when the user starts the application. It first initiates the resources required to run the application, and then executes the Run-Time routine periodically until the application is terminated, at which time the Shut down routine is executed. While executing, the Run-Time routine will itself invoke the LDWS Processor process, which will capture and analyze data from the device camera.

### 4.2 LDWS Processor Process

This process initiates the camera input, which will feed into the Image Capture routine. It also initializes the OpenCV library, which will be used to analyze data captured by the camera. The Image Capture routine runs periodically while the application is active, and invokes the Lane Detection process to determine when there is a lane departure event. Upon being notified of an event, the Notification routine invokes the Departure Notification process to inform the user, and invokes the logging routine to record the data. When relevant, the logging routine may also exchange data with the run-time routine, such as when an error occurs that requires corrective action.

### 4.3 Lane Detection Process

The Lane Detection process executes a periodic Image Processing routine to analyze the camera data. In order to extract the relevant information, the image is passed through a series of filters: a Gaussian filter, Hough transformation, and Canny edge detector. The output of these filters is then passed from the Image Filtering routine to the Lane Determination routine, which will process the filtered image to determine when a lane departure event has occurred. This routine will also run periodically whenever there is new image data to be processed, and upon detection of an event will invoke the Notification routine in the LDWS Processor Process.

### 4.4 Departure Notification Process

This process is invoked by the Notification routine when a lane departure event has been detected. It comprises an Audible Warning routine which interfaces with the device speakers and, if enabled, a Visual Warning routine that interfaces with the application window interface to provide visual feedback.

## 5. Development Architecture
TBD: Assigned to Sapana

The following diagram shows the components involved in the Back Seat Driver application.
<p align="center"><img src="ComponentDiagram.png" width="600px"></p>

## 6. Scenarios
Scenario 1 : Normal Operation
   - Launch the application from an android phone
   - Application processes inputs from camera looking for lane lines
   - Application alerts user when car is driven close to a lane
   - User closes application when destination is reached

Scenario 2 : Lane Departure Alert
   - Launch the application from an android phone
   - Application detects from camera that vehicle is leaving lane
   - Application alerts user with audio and visual cues that vehicle is leaving the lane
   - Application continues to monitor for next lane departure
