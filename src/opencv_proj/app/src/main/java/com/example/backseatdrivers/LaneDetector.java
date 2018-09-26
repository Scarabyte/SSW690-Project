package com.example.backseatdrivers;

/*
   The LaneDetector class performs image processing and algorithmic functionality
   to detect a lane of travel in the supplied image.
 */

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
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
        Mat linesHough = new Mat();

        /* Process the image and detect a lane. Return the points that identify the lane. */
        Imgproc.GaussianBlur(image.gray(), outputImage, new Size(5,5), 3, 3);
        Imgproc.Canny(outputImage, outputImage, 10, 100);
        Imgproc.HoughLinesP(outputImage, linesHough, 1, Math.PI / 180, 5, 5, 1);
        Point pt1 = new Point();
        Point pt2 = new Point();
        for(int i = 0; i < linesHough.cols(); i++) {
            double[] val = linesHough.get(0, i);
            pt1.x = val[0];
            pt1.y = val[1];
            pt2.x = val[2];
            pt2.y = val[3];
            Imgproc.line(outputImage, pt1, pt2, new Scalar(0, 0, 255), 10);
        }

        return lanePoints;
    }
}
