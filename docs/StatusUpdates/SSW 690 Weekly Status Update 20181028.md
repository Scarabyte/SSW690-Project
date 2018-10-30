## SSW 690 Weekly Status Update 

### Team : _Back Seat Drivers_

### Week Ending: 10/28/18 (Sun) – Week 9 of class

### Project Status: (Green, yellow, red)

_One word assessment of how is the team doing relative to the schedule?_

| ![Green](https://github.com/Scarabyte/SSW690-Project/blob/master/docs/StatusUpdates/status_green.png?raw=true) | Green  |
| ----------- |:-----------:|

### Team Status Summary: Where are we now?

_One or two sentences describing high level project status and progress._

Goals for Week 9 of class:
Continue working on lane detection algorithm and testing framework from where we left them after Sprint 2.

### Last week's activities by team member:

#### Adam:

* _What did I work on?_
  * Completed sky view task
  * Reviewed code and continued lane detection research
* _What did I accomplish?_
  * Got some screenshots to demo the sky view mode

#### Sapana:

* _What did I work on?_
  * With Keith's help finished task 57 for testing
* _What did I accomplish?_
  * Test of LDWSProcessor (lane detection) with stock image. Need some more work in next Sprint.
  * Understood how the Android ImageView class works
  
#### Keith:

* _What did I work on?_
  * Testing task
  * Fit Curves task
* _What did I accomplish?_
  * Architected the ability to perform a self test on a stock image. This involved creating another Activity with an ImageView widget, bound to a menu item available in the toolbar. The stock image is loaded from a resource file with the application, sent to the LDWS Processor to run our algorithms and processing, then the processed image is displayed on screen.
  * Began, but did not finish, the Fit Curves task. Implemented a Sobel filter to isolate the lane markings. Begin the development of a sliding window lane detection algorithm that finds the peaks of the intensities of the images and establishes a series of points that identify the middle of the lane markings. This will then be used as input to a curve fitting algorithm.

#### Rakshith:

* _What did I work on?_
  * Adam incorporated the area of interest into this commits while working on sky view.
  * Researched articles related to figuring out lane edge
* _What did I accomplish?_
  * Did not accomplish completion of sprint 3 goal. Adam stepped in to finish up the story assigned to me

### Issues, Risks, and Opportunities:

* _What problems did you encounter and what impact might those have on meeting the project schedule?_
  * One team member has a lot of business obligations, another has some health-related issues that may limit availability
* _What new opportunities did you discover, if any?_

### Action Items/Goals for next week:

#### Team Goals for next week:

* _Where do we want the project to be next week?_

#### Individual Goals

##### Adam:

* _What do you plan to work on and accomplish in the next week?_
  * Assist Keith if needed for the Fit Curves task
  * Try to close some additional issues as part of Sprint 4 assignments

##### Sapana:

* _What do you plan to work on and accomplish in the next week?_
  * Reaserch and fix color space issue from yellow to blue when image is processed
  * Continue testing with images which has white dotted center and side lines  

##### Keith:

* _What do you plan to work on and accomplish in the next week?_

##### Rakshith:

* _What do you plan to work on and accomplish in the next week?_
  * Will start working on the presentation for the end of the semester.
### Other Comments:

* _Any other relevant information to help us to manage the project and keep our customer informed and happy._
  * The upcoming week is the beginning of Sprint 4 (out of 5). We've made great progress so far, now it's time to start wrapping it up and finishing things off.
