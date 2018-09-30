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
        Mat tempImage = new Mat();

        /* Process the image and detect a lane. Return the points that identify the lane. */
        Imgproc.GaussianBlur(image.rgba(), outputImage, new Size(5,5), 3, 3);
        Imgproc.GaussianBlur(image.gray(), tempImage, new Size(5,5), 3, 3);

        Imgproc.Canny(tempImage, tempImage, 10, 100);
        Imgproc.HoughLinesP(tempImage, linesHough, 1, Math.PI / 180, 5, 100, 10);
        Point pt1 = new Point();
        Point pt2 = new Point();
        for (int x = 0; x < linesHough.rows(); x++) {
            double[] l = linesHough.get(x, 0);
            Imgproc.line(outputImage, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA, 0);
        }

        return lanePoints;
    }
}
