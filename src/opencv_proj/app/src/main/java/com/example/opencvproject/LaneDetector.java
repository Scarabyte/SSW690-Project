package com.example.opencvproject;

/*
   The LaneDetector class performs image processing and algorithmic functionality
   to detect a lane of travel in the supplied image.
 */

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;


import java.util.ArrayList;
import java.util.List;

public class LaneDetector {

    public LaneDetector() {
        /* Perform initialization here. */
    }

    public List<MatOfPoint> detect(CameraBridgeViewBase.CvCameraViewFrame image, Mat outputImage) {

        List<MatOfPoint> lanePoints = new ArrayList<MatOfPoint>();

        /* Process the image and detect a lane. Return the points that identify the lane. */
        Imgproc.GaussianBlur(image.gray(), outputImage, new Size(5,5), 3, 3);

        return lanePoints;
    }
}
