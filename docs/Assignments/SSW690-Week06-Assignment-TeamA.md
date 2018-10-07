# Assignment Week 6
## Build, test, document, deploy your project

8 October 2018

**Team Members / Honor pledge**

Sapana Belorkar Adam Burbidge Keith Roseberry Rakshith Varadaraju

_I pledge on my honor that I have not given or received any unauthorized assistance on this assignment/examination. I further pledge that I have not copied any material from a book, article, the Internet or any other source except where I have expressly cited the source._

# **Assignment**

Weeks 4-12 are dedicated to building, testing, documenting, and deploying your project.  You should use GitHub for source control and project tracking.

Each team will meet weekly with faculty to discuss current status, issues, and concerns.  We&#39;ll also review the issues in your GitHub repository so be sure to keep GitHub up to date.

# **Response**

### Sprint 2 Week 1

In Sprint 1 we laid the initial groundwork of performing the image transformations and recognizing straight lines in the image. The "brains" of our app will be will come from using this information to recognize useful features in the image, i.e. identifying the actual lane markers. We have an open issue on our GitHub page that covers this: #17 Identify Road Features. In previous weeks we had deliberately set a high estimate on this (40 story points!) because we knew it was a large task requiring many steps. As part of our Sprint 2 planning, we converted this to an Epic, with a number of sub-tasks (Issues #42 and #45 through #49).

For Sprint 2, we're concentrating initially on the first set of tasks: calibrating the camera, and identifying the region of interest in the picture. The next step will be to convert the picture to a "sky view" (or "bird's eye view"); this involves distorting the image so that lines that should be parallel (such as the lane markings) appear to be actually parallel as if looking down on them from above, as opposed to converging at the horizon. This research is included in Sprint 2 as a "spike" - a task where the story point estimate is not necessarily known ahead of time, but the results of which will be useful for subsequent tasks.

In parallel, we are also continuing to work on the testing framework: at a high level, our general testing strategy involves feeding stock images and videos into our app and verifying that the lanes are correctly identified, and confirming on our physical devices that the app performs adequately at the user level. (As our expected output is an initial prototype for the app, we don't necessarily expect to have completed extensive or exhaustive testing, especially given the limited time and resources that we have available and that testing can, in itself, easily become a large software development project. However, we do plan to verify that the app is at least functional to the level expected of a development model.)
