## SSW 690 Weekly Status Update 

### Team : _Back Seat Drivers_

### Week Ending: 9/30/18 (Sun) – Week 5 of class

### Project Status: (Green, yellow, red)

_One word assessment of how is the team doing relative to the schedule?_

| ![Green](https://github.com/Scarabyte/SSW690-Project/blob/master/docs/StatusUpdates/status_green.png?raw=true) | Green  |
| ----------- |:-----------:|

### Team Status Summary: Where are we now?

_One or two sentences describing high level project status and progress._

Goals for Week 5 of class:
* Complete Sprint 1 stories
* Start planning for Sprint 2

### Last week&#39;s activities by team member:

#### Adam:

* _What did I work on?_
  * Completed Sprint 1 tasks
  * Team Lead / Project Management housekeeping tasks
* _What did I accomplish?_
  * Completed first iteration of Hough Lines tasks - it needs some adjustments, but the function itself works
  * Reviewed team member's work as available and kept GitHub up to date
  * Kept team members informed about all discussions with professor; met with Rak individually since he was not able to attend the group meeting this week

#### Sapana:

* _What did I work on?_
  * 
* _What did I accomplish?_
  * 

#### Keith:

* _What did I work on?_
  * Sprint 1 Tasks
  * Research, research, research... and more research
* _What did I accomplish?_
  * Completed Sprint 1 task to perform Canny Edge detection. Note that the parameters may need to change depending on how well our detection algorithm functions.
  * Researched lane detection methods. Came up with the following sequence:
    1. Correct image distortion with calibration data (requires calibration function to have completed).
    2. Apply a region of interest and transform to 'sky view'.
    3. Filter the image to find the lane markers.
    4. Fit curves to both left and right side lane markers.
    5. Project back onto the original image for display.

#### Rakshith:

* _What did I work on?_
* _What did I accomplish?_


### Issues, Risks, and Opportunities:

* _What problems did you encounter and what impact might those have on meeting the project schedule?_
  * Projecting lines from the Hough transform onto the original image is not yet working.
* _What new opportunities did you discover, if any?_
  * Could reduce the image resolution of the video feed to allow for better performance.

### Action Items/Goals for next week:

#### Team Goals for next week:

* _Where do we want the project to be next week?_
  * Estimates and stories for Sprint 2 identified
  * Preliminary testing framework
  * Continue any supporting research as needed

#### Individual Goals

##### Adam:

* _What do you plan to work on and accomplish in the next week?_
  * Review the new issues / tasks opened for the lane detection; make estimates and assignments for Sprint 2
  * Start working on Sprint 2 tasks assigned to me

##### Sapana:

* _What do you plan to work on and accomplish in the next week?_
  * 

##### Keith:

* _What do you plan to work on and accomplish in the next week?_
  * Translate research findings into user stories/backlog items.
  * Work on Sprint 2 backlog items.
  * Continue research to find any way we can optimize our software.
  
Note: I'm on travel for three weeks beginning 10/08. I'll be able to work while traveling but my responses may be delayed and my attendance in group meetings/classes may be impacted.

##### Rakshith:

* _What do you plan to work on and accomplish in the next week?_

### Other Comments:

* _Any other relevant information to help us to manage the project and keep our customer informed and happy._
  * In terms of the project management aspects our team is currently doing very well. The professor (customer) asked how our application performs the lane detection, which has been the primary focus of our research and will be a big part of the Sprint 2 tasks. We will continue to keep the professor updated with our current progress during the weekly class meetings.
  
