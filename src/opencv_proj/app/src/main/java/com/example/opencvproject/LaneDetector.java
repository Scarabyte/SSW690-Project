package com.example.opencvproject;

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
        Imgproc.HoughLines(outputImage, linesHough, 1, 2*Math.PI/180, 20);
        for (int i = 0; i < linesHough.cols(); i++){
            double data[] = linesHough.get(0, i);
            double rho = data[0];
            double theta = data[1];
            double cosTheta = Math.cos(theta);
            double sinTheta = Math.sin(theta);
            double x0 = cosTheta * rho;
            double y0 = sinTheta * rho;
            Point pt1 = new Point(x0 + 10000 * (-sinTheta), y0 + 10000 * cosTheta);
            Point pt2 = new Point(x0 - 10000 * (-sinTheta), y0 - 10000 * cosTheta);
            Imgproc.line(outputImage, pt1, pt2, new Scalar(0, 0, 200), 3);
        }

        return lanePoints;
    }
}
