# Assignment Week 2
## Requirements/Features and Development Plan

10 September 2018

**Team Members / Honor pledge**

Sapana Belorkar Adam Burbidge Keith Roseberry Rakshith Varadaraju

_I pledge on my honor that I have not given or received any unauthorized assistance on this assignment/examination. I further pledge that I have not copied any material from a book, article, the Internet or any other source except where I have expressly cited the source._

# **Assignment**

Each team must:

1. Gather requirements/user stories using whatever techniques you prefer
2. Document the major features your project will deliver in GitHub and assign an initial priority to each feature
3. Identify the features in your project&#39;s Minimal Viable Product
4. Create a GitHub milestone to define the issues to be addressed in the first sprint
5. Create the first draft of your Development Plan using the Dev Plan Template ([https://sit.instructure.com/courses/28338/modules/items/611547](https://sit.instructure.com/courses/28338/modules/items/611547)) in the course materials section of Canvas.

We will do a quick informal review of the features in the MVP and your plans for the first sprint in class on Week 3.

# **Response**

1. Gather requirements/user stories using whatever techniques you prefer
  * User stories are being collected using the "ZenHub" add-on for GitHub. A "user story" tag is created to identify issues.

2. Document the major features your project will deliver in GitHub and assign an initial priority to each feature

We initially identified three major functions for the completed system to provide:
  * Lane Departure Warning System (LDWS) Functionality
  * Obstacle Detection Functionality
  * Traffic Sign Recognition Functionality

Due to the complexity of the Obstacle Detection Functionality, the team decided to postpone this feature. Preliminary research indicates that this functionality would typically be achieved using two cameras to provide a stereoscopic view similar to human vision. Most consumer-grade mobile devices do not have suitable dual cameras, and without this 3-D view, the distance of a detected object may be difficult or impossible to determine. The function's accuracy is also enhanced with non-visual sensors such as LIDAR, which a mobile phone will not have. Therefore, due to the technical limitations and time constraints of the semester, the team decided to assign this feature a lower priority, and focus first on the others.

For the remaining two functions, high-level flowcharts have been added to the function descriptions. While there is no strong preference for one or the other, the general feeling among the team is that the Lane Departure Functionality may be the simpler function to tackle first. 

Traffic sign recognition is complicated by the fact that signs come in many different shapes. While the shape of a sign could be used to give a hint about its identity (for example, a hexagonal Stop sign or a triangular Yield sign), the most useful recognitions from a user's perspective may be speed limits and/or directional signs, which would involve an element of text recognition. A challenge from a development point of view is that in some cases it may be difficult to distinguish between "real" road signs and billboards or signs on someone's front lawn. The color of a sign could also be significant (for example, orange signs in construction zones), adding to the complexity of this function.

The Lane Departure Functionality has its own complexities in that the lane markings may be in different colors (for example, white or yellow), and different configurations (for instance, solid or broken, double solid, or solid coupled with dashed). For this project, the challenge of the colors of the lane markings is mitigated by the conversion to grayscale in the flowchart, but in reality there could be some significance to differently-colored lanes. Similarly, the lane configurations described above are the most common ones, so there is a finite set of variations to consider.

Therefore, based on the initial research, the team decided to prioritize first the Lane Departure Warning System (LDWS) Functionality, second the Traffic Sign Recognition Functionality, and third the Obstacle Detection Functionality.

3. Identify the features in your project&#39;s Minimal Viable Product

As discussed in Item 2 above, the Minimal Viable Product for this project will be an initial version of the LDWS Functionality.
The specific features needed to provide this functionality, as well as the acceptance criteria, will be documented in the project's storyboards. Also, as mentioned in the project's Overview presentation, in the first iteration we will initially exclude nighttime, adverse weather and bright sunlight/glare conditions, in order to concentrate first on producing a working system under "sunny day" conditions.

4. Create a GitHub milestone to define the issues to be addressed in the first sprint

Milestones have been created for Sprint 1, as well as the Architecture & Design and Final Presentation. More milestones will be defined as needed, and issues will be assigned during sprint planning reviews.

5. Create the first draft of your Development Plan using the Dev Plan Template

The development plan is created in [https://github.com/Scarabyte/SSW690-Project/blob/master/docs/DevelopmentPlan.md](https://github.com/Scarabyte/SSW690-Project/blob/master/docs/DevelopmentPlan.md). Note that as stated in the assignment text, this is still only the "first draft" of the development plan, and so may evolve over time as the needs of the project change.
