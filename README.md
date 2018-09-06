# SSW690-Project
Repository for SSW690 Project

Team Members:
* Belorkar, Sapana
* Burbidge, Adam
* Roseberry, Keith
* Varadaraju, Rakshith

References/Links:
* [OpenCV Library](https://opencv.org/)
* [OpenCV on Android](https://opencv.org/platforms/android/)
* [OpenCV on Raspberry Pi](https://www.pyimagesearch.com/2017/09/04/raspbian-stretch-install-opencv-3-python-on-your-raspberry-pi/)
* Article: [Three Sensor Types Drive Autonomous Vehicles](https://www.sensorsmag.com/components/three-sensor-types-drive-autonomous-vehicles)
* Article: [Smartphone based mass traffic sign recognition for real-time navigation maps enhancement](https://ieeexplore.ieee.org/document/7975125/)

### Lane Departure Warning System (LDWS) Functionality

From [Wikipedia](https://en.wikipedia.org/wiki/Lane_departure_warning_system): A lane detection system uses the principle of [Hough transform](https://en.wikipedia.org/wiki/Hough_transform) and [Canny edge detector](https://en.wikipedia.org/wiki/Canny_edge_detector) to detect lane lines from realtime camera images fed from the front-end camera of the automobile.

![LDWS Flowchart](Lane_Detection_Algorithm.jpg)

### Obstacle Detection Functionality

From [ExtremeTech](https://www.extremetech.com/extreme/189486-how-googles-self-driving-cars-detect-and-avoid-obstacles): Google mounts regular cameras around the exterior of the car in pairs with a small separation between them. The overlapping fields of view create a parallax not unlike your own eyes that allow the system to track an object’s distance in real time. As long as it has been spotted by more than one camera, the car knows where it is. These stereo cameras have a 50-degree field of view, but they’re only accurate up to about 30 meters.

*Barrier: Most mobile phones only have one camera and even if dual cameras are available, the separation between the cameras will not be sufficient to detect an object. Object detection is enhanced with non-visual sensors such as LIDAR, which a mobile phone will not have.*
