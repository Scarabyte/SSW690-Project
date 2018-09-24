## SSW 690 Weekly Status Update 

### Team : _Back Seat Drivers_

### Week Ending: 9/23/18 (Sun) – Week 4 of class

### Project Status: (Green, yellow, red)

_One word assessment of how is the team doing relative to the schedule?_

| ![Green](https://github.com/Scarabyte/SSW690-Project/blob/master/docs/StatusUpdates/status_green.png?raw=true) | Green  |
| ----------- |:-----------:|

### Team Status Summary: Where are we now?

_One or two sentences describing high level project status and progress._

Goals for Week 4 of class:
* Estimate story points for the open issues
* Sprint planning for Sprint 1

### Last week&#39;s activities by team member:

#### Adam:

* _What did I work on?_
  * Estimates and suggested priorities for all open issues (as of the weekly team meeting)
  * Sprint 1 task: Hough lines
* _What did I accomplish?_
  * Executed app with grayscale image, and with Canny Edge filtering
  * Uploaded first draft of Hough lines (needs some more work)
  * Found some stock images of roads we can use for testing purposes

#### Sapana:

* _What did I work on?_
* _What did I accomplish?_

#### Keith:

* _What did I work on?_
  * Sprint 1 Task: Canny Edge Detector
  * Research: Lane Detection
* _What did I accomplish?_
  * Implemented the Canny Edge detction and displayed on screen.
  * Researched the OpenCV interface to the Android Camera - didn't get too far. Would like to better control the camera image as currently it's not using the full capabilities of my camera's image.
  * Researched methods to detect lane edges. Still in the early stages on this.

#### Rakshith:

* _What did I work on?_
* _What did I accomplish?_


### Issues, Risks, and Opportunities:

* _What problems did you encounter and what impact might those have on meeting the project schedule?_
  * App sometimes crashes after uploading first Hough lines iteration (Adam; others have not yet experienced this)
* _What new opportunities did you discover, if any?_
  * Current draft of the implementation is procedural; might refactor to Extract Method so we can test and refine each of the filters independently

### Action Items/Goals for next week:

#### Team Goals for next week:

* _Where do we want the project to be next week?_
  * Continue work on Sprint 1; the first set of user stories should be complete (at least functional; refinements possible)
  * Testing? Can we feed in some of our stock images?
  * Research methods to actually identify lanes in our filtered images

#### Individual Goals

##### Adam:

* _What do you plan to work on and accomplish in the next week?_
  * Continue Sprint 1 work (refine Hough lines implementation)

##### Sapana:

* _What do you plan to work on and accomplish in the next week?_

##### Keith:

* _What do you plan to work on and accomplish in the next week?_
  * Continue research into lane detection methods.
  * Look into developing a menu system that will allow us to change the parameters of the image filters during run time.
  * Determine how to utilize the full camera interface instead of a cropped image.

##### Rakshith:

* _What do you plan to work on and accomplish in the next week?_

### Other Comments:

* _Any other relevant information to help us to manage the project and keep our customer informed and happy._
  * Work on Sprint 1 is progressing well so far; the 'real' challenges are still upcoming with lane detection methods
