package com.example.opencvproject;

/*
   The LaneDetector class performs image processing and algorithmic functionality
   to detect a lane of travel in the supplied image.
 */

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.MatOfPoint;

import java.util.ArrayList;
import java.util.List;

public class LaneDetector {

    public LaneDetector() {
        /* Perform initialization here. */
    }

    public List<MatOfPoint> detect(CameraBridgeViewBase.CvCameraViewFrame image) {

        List<MatOfPoint> lanePoints = new ArrayList<MatOfPoint>();

        /* Process the image and detect a lane. Return the points that identify the lane. */

        return lanePoints;
    }
}
