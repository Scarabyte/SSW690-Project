# Risks

This document enumerates, scores and visually displays the risks to the project. Risk responses are enumerated and tracked.

## Risk Dashboard

Total Number of Risks: TBD

| P/I     |  1  |  3  |  9  |
|:-------:|:---:|:---:|:---:|
|  __9__  |     |     |     |
|  __3__  |     |     |  2  |
|  __1__  |     |  1  |     |

## Risk Register

| Num | Risk Statement | Type | Probability | Impact | Score |
|:---:| -------------- | ---- |:-----------:|:------:|:-----:|
|  1  | If OpenCV cannot be made to work on the Android platform, then the project team will need to find an alternative. | Technical, Schedule | 3 | 9 | 27 |
|  2  | If the Android platform doesn't have enough throughput, then real-time image processing won't be possible. | Technical | 1 | 3 | 3 |
|  3  | If significant development time is required to build the software, then the project will not meet it's deadlines. | Schedule | 3 | 9 | 27 |

## Risk Responses

In this section, each risk identified in the risk register with scores of 27 or higher have a defined response. The response includes the type of action to take, a description of the response and the resultant risk score after the response has been completed.

| Risk | Num | Response | Type | Probability After | Impact After | Score After |
|:----:|:---:| -------- |:----:|:-----------------:|:------------:|:-----------:|
|   1  |  1  | Research other computer vision libraries to determine feasibility of a replacement. | Mitigation | 1 | 3 | 3 |
|   1  |  2  | Instead of using video and computer vision, use camera still images at highest rate. | Mitigation | 1 | 3 | 3 |
|   3  |  3  | Project team members work through Android app tutorial. | Mitigation | 3 | 3 | 9 |
|   3  |  4  | Project team members take additional tutorials (YouTube videos?) | Mitigation | 3 | 3 | 9 |
|   3  |  5  | Locate and utilize a suitable starting application template | Mitigation | 1 | 3 | 3 |
