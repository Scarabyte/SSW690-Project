## Team Back Seat Drivers: Computer vision driving aids
Author - All team members

Team

* Belorkar, Sapana
* Burbidge, Adam
* Roseberry, Keith
* Varadaraju, Rakshith

Issue Date of Baselined Document

Subsequent issue dates listed earliest to latest

| Revision    | Issue Date  |
| ----------- |:-----------:|
| First Draft | 11-Sep-2018 |

### INTRODUCTION (required)
The project will develop an Android application that continuously evaluates the video feed from the camera to identify driving hazards and notify the operator of one or more of 1) traffic sign recognition and speed violation; 2) lane detection and departure; and 3) obstacle and collision imminence. The software will utilize the Android operating system facilities to interface with the camera, display and speaker. The camera will provide the needed video input at a suitable rate to support processing, the display will provide output information in a visual format to the vehicle operator, and the speaker will provide audial notification in the case of a detected event.

The development will be performed using agile methods, particularly scrum. The requirements will be captured as user stories, sprints will be executed each week of the semester, and output evaluated at the end of each sprint in a retrospective. The team will perform as much prototyping as is possible to vet ideas and algorithms, and evolve the prototype into working software as the project progresses. Status reports will be provided weekly, including burn-down charts, highest priority items in the backlog, and information pertaining to completed and planned work.

*TBD: Original text to be deleted before publishing:*

*This is a brief description of the project, at most 2 paragraphs.  
You should point to key documents, e.g., requirements documents, architecture review documents, 
state the problem you are trying to solve and state success criteria. 
Briefly state what is the problem you are trying to solve and perhaps a user story of how it will be used.*

### ROLES AND RESPONSIBILITIES (required)
These vary for each type of product and, for small projects, folks may serve multiple roles.  
This is a list of common roles we have used for software development:
* Development Lead (Adam)
* Architect (Keith)
* Developers (Adam, Keith, Rak)
* Test Lead (Sapana)
* Testers (All)
* Documentation (All)
* Documentation Editor (Rak)
* Designer (Keith)
* Customer Representative (All)

#### Roles not needed/excluded due to team size
* Buildmeister (???) - manages pull requests and builds the "official" application
* User advocate (only 1 name) -- not needed
* Modification Request Board (1 leader, multiple representatives) -- not needed
* Risk Management (only 1 name)
* System Administrator (only 1 name)
* Requirements Resource (usually 1 name)
* Customer responsible for acceptance testing

### METHOD (required) - Assigned:Adam
_These are unique to software development, although there may be some overlap._
* Software:
  * Language(s) with version number including the compiler if appropriate
    * Android Studio 3.1.4
    * JRE: 1.8.0_152-release-1024-b02 amd64
    * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
  * Operating System(s) with release number
    * Windows 10 64-bit
  * Software packages/libraries used with release/version number
    * OpenCV Andoid Library on Android Studio Version 3.4.3
  * Code conventions – this should preferably be a pointer to a document agreed to and followed by everyone
    * [https://source.android.com/setup/contribute/code-style](https://source.android.com/setup/contribute/code-style)
* Hardware:
  * Development Hardware
    * Moto Z Play, Android 8.0.0
    * HTC One M8, Android 6.0
  * Test Hardware
    * Samsung Galaxy S5
  * Target/Deployment Hardware
    * Consumer Android device (multiple) running Android Marshmallow (6.0.0) or higher
* Back up plan (individual and project)
  * Android Virtual Device using AVD Manager in Android Studio
  * Platform-independent groundwork using desktop PCs (e.g. algorithm development, model training, etc)
  * Switch target to Raspberry Pi if an insurmountable difficulty is encountered (note: becomes riskier as project progresses)
* Review Process:
  * Leverage the sprint retrospective - review contributions from the past week/sprint.
  * Will you do architecture, usability, design, security, privacy or code reviews?
  * What approach will you use for the reviews (formal, informal, corporate standard)?
  * Who is responsible for the reviews and resolving any issues uncovered by the reviews?
  * Code readings?
* Build Plan:
  * Revision control system and repository used
    * GitHub repository located at https://github.com/Scarabyte/SSW690-Project/
    * Includes issue tracking and project management with the ZenHub extension
  * Regularity of the builds
    * Once work on sprints begins, informal builds should be completed by the team member for each new feature/issue addressed (i.e. to the extent possible, do not commit code that does not produce a working build)
    * Official build should be produced at the midpoint and the end of the sprint (i.e. weekly, for a 2-week sprint duration) to confirm the project continues to perform correctly
  * Deadlines for the builds – deadline for source updates
    * New code should be committed by Sunday evening, with a build on Monday to allow time for testing and, if necessary, debugging or raising new issues to be addressed
  * Multiplicity of builds
    * Each team member has access to the repository to download and test the project on their own device
      * An issue should be raised in GitHub if one team member encounters an issue that the others did not (See Target Hardware above; the end-users will represent a wide range of devices and configurations)
  * Regression test process – see test plan
    * Tests should be automated whenever possible so that a significant portion can be executed against each build
      * Any failing test that previously passed should trigger an issue in GitHub
* Modification Request Process:
  * Use issues in GitHub/ZenHub, marked as "bug" and "enhancement" as appropriate
  * Decision process (board – if more than paragraph should point to alternate description)
    * Team will agree on issue status and assignments during sprint planning meetings
  * State whether there will be two process streams one during development and one after development
    * For a project of this size and scope (small team, limited time frame to complete the initial prototype), a single process stream for modification requests should be sufficient; the team will agree on the request status during regular meetings (note that some requests may fall outside the scope of work planned)
      * This plan could be revised as the needs of the project change

### Virtual and Real Work Space - Assigned:Keith
The project team are geographically dispersed and have significant limiations on time during which the entire team can meet in person. To support the remote nature of the project team, the GitHub repository and system will be the primary method used as a workspace. There are some trade-offs in this approach, which must be considered by the project team when contributing to the project.

* Document Formatting Restrictions: The MarkDown (MD) method of documenting in GitHub is limited in its ability to format text and doesn't include built-in diagramming or editing tools. Word processing systems such as Microsoft Word and Google Docs are superior in this manner, but the project team is unable to utilize these systems due to security restrictions at their workplaces. Therefore, the use of the MarkDown documents in GitHub will be the preferred approach. Project team members will work around any formatting limitations accordingly.

* Lack of Concurrent Editing of Documents: The ability to work in a single document at the same time is a huge benefit to using Google Docs, but due to the security restrictions of the project team's workplaces the use of Google Docs is prohibited. Therefore, the project team will utilize the MarkDown facility of GitHub and will need to carefully manage editing of the documentation. For small, quick changes editing the MD document in the master branch may suffice. For more significant changes, the project team should create a branch and a subsequent pull request to merge the changes back into the master in a controlled manner.

An extension to the GitHub system to manage user stories, backlog and sprint execution will be utilized. The ZenHub extension will provide a higher level of data and information management that GitHub alone cannot provide. Since ZenHub is integrated with GitHub, the data is also visible in the GitHub repository (user stories, enhancements, etc. in ZenHub are stored as GitHub issues). The project team will develop user stories and track issues and enhancements with the ZenHub extension.

GitHub Repository Location: https://github.com/Scarabyte/SSW690-Project

ZenHub Extension Location: https://app.zenhub.com/workspace/o/scarabyte/ssw690-project/boards?repos=146369608

An alternative that may be invoked if needed is the Polarion ALM web-based agile management application. This system supports tracking and execution of agile projects and includes functionality for online documentation generation and maintenance, backlog management, team collaboration and more. The system can be connected to the GitHub repository in order to manage repository-based assets such as source code and build scripts. While the Polarion ALM system can be used for all of these features it is an enterprise-class tool that requires a certain amount of learning to use effectively. Due to this, the project team will only invoke the use of the Polarion ALM tool if the GitHub and ZenHub combination are not satisfying the project needs.

### COMMUNICATION PLAN (required) - Assigned:Rak
#### _“Heartbeat” meetings_
_This section describes the operation of the “heartbeat” meetings, meetings that take the pulse of the project.  
Usually these meetings are weekly and I prefer to have them early in the day before folks get into their regular routine, 
but this is not necessary.  
The meeting should include only necessary individuals – no upper level management or lurkers.  
It should have a set agenda, with the last part of the meeting reviewing open issues and risks.  
It should be SHORT, thirty minutes or less is ideal.  
Notes should be provided after the meeting and issues should be tracked and reviewed each meeting, usually at the end._

#### _Status meetings_
_Status meetings have management as their target and should be held less frequently than heartbeat meetings, 
preferably biweekly, monthly or quarterly depending on the duration of the project.  It is solely to provide 
status for the project.  If issues arise, they should be addressed at a separate meeting (see next item). 
These should be short.  This section should describe the format and periodicity._

#### _Issues meetings_
_If a problem does arise, never surprise your manager.  Schedule a meeting at his or her earliest convenience.  
This section describes how alerts will arise and the governance of when to trigger an alert – usually after a 
discussion at the heartbeat meeting._

### TIMELINE AND MILESTONES (required) - Assigned:Sapana
_This section should be crisp containing 4-10 milestones for the duration of the project, each of which would 
trigger a re-issuing of this document to report on progress.  Each milestone should define a 100% complete item, 
should list the critical participants and list begin time and end time.  Each time you re-issue this document 
you should highlight changes with italics or bold – colors will not show up on a photocopy._

Note that for this project we have a few time boxes.  They are:
* Week of September 25th – In first demo, present Architecture and design. Architect along with team members will finalize and present finalized version of Architecture and design of lane departure warning system
* Week of October 9th first demo, In second demo, team will present research done so far and features/stories developed in Sprint 1 and 2
* Week of October 23rd second demo, In third demo, team will present features/stories developed in Sprint 3
* Week of November 6th third demo, In fourth demo, team will present features/stories developed in Sprint 4
* Week of November 27th fourth demo, In final 20 to 30 min presentation, team will describe the project, explain major features, challenges team encountered along the way, lessons learned and live demonstration of deployed project  
* Week of December 10th final product


### TESTING POLICY/PLAN (optional–software relevant) - Assigned:Adam
_This should probably point to a plan or the document would get unwieldy.  
At the very least it should describe when testing begins._

Testing covers multiple levels of project development:
* Unit Testing
  * New functions are tested as they are created. May be linked to the closure of a specific issue, with the test results as evidence.
  * Unit testing will be performed as discussed in SSW-567 for each new function or modification introduced
* Integration Testing
  * Unit testing on a single module uses simulated inputs; when two or more related modules are completed, they will be tested together to ensure proper integration, with the "real" output of one function serving as the input to the other.
* System Testing
  * This testing will begin roughly midway through development when a significant portion of the project has been completed, and continuing on to the end of the lifecycle. The objective is to verify that the overall system performs correctly under known conditions. (The stated goal for the initial prototype is to perform correctly in normal daylight driving conditions, i.e. excluding nighttime, adverse weather and bright sunlight/glare conditions.)
* Acceptance Testing
  * Once the developers are satisfied with the system performance, the customers and end-users determine whether the product is acceptable, i.e. that the given requirements have been met to the satisfaction of the customer. With an Agile approach, this occurs multiple times during development, at the end of each sprint.

### RISKS (required) - Assigned:Keith
Risk management is an integral activity to all development processes and proper risk management allows for the identification and effective responses to project risks. Risks may impact many dimensions associated with a development project, including technical, personnel, cost and schedule, and environmental. Risks typically affect more than one dimension, but are identified with a primary dimension.

All risks will be scored based on their probability of occurrence and the impact on the project if they do occur. Both probability and impact will be scored as follows:

* 1 = Low
* 3 = Medium
* 9 = High

The total score of a risk is the product of the probability and impact scores. Risks that have a total score of 27 and higher will require one or more responses in order to manage the impact of the risk. Risks that have a total score of less than 27 will be monitored and addressed accordingly if their scores change. A separate GitHub MD document [Risks](Risks.md) will be used to track and manage the risks of the project. The MD document will include a risk register (enumeration of all project risks and scores) and a risk dashboard that displays the counts of the risks in each score value.

Once a risk is identified, an appropriate response is formulated. The responses include mitigation, transferrance, elimination and acceptance. Most risks are typically mitigated to reduce their impact upon realization. However, some risks may be eliminated entirely or transferred to another organization. Although rare, it is possible that the project team may choose to accept a risk since either the probability of the risk occuring or the impact if it does occur is small enough to absorb on the project.

Risks will be managed at each Sprint Retrospective/Weekly Status Meeting (see the Communication Plan section above).

### ASSUMPTIONS (required) - Assigned:Rak
_It may be clear to the project insiders what assumptions are being made about staffing, hardware, vacations, rewards, ... 
but make it clear to everyone else and to the other half of the project that cannot read your thoughts._

### DISTRIBUTION LIST - Assigned:Sapana
_Who receives this document?_

All team members who are playing different roles in the project, product owner, Project Manager. It should also go on the shared resource such as wiki/Sharepoint where other stakeholders have access if needed.

### MORE OPTIONAL SECTIONS:
_These should be self-explanatory._

#### _Worry beads_
I add this section to describe the things as manager I am most worried about at the time of latest document issue.  
This section is useful because it helps you to focus on the parts most likely to fail.  Sometimes, I segment 
the worries by time scale: day, week, month, quarter ... lifetime.

#### _Documentation Plan_
Many years ago we had much too much documentation, now we have precious little – this has to change.  
Write documentation as if you’ll need to personally support the project forever – you just might need to and 
you’ll be glad you took the time to document the obvious, the not so obvious and the obscure.  
As an example, it’s useful to document alternate architectures and designs you did not pursue along with the rationale.  
What were the “gotchas” you were trying to avoid?  

#### _Build Plan_
When builds and testing become complex, this might be a separate section or point to a separate document.
