package com.example.backseatdrivers;

/*
   The LaneDetector class performs image processing and algorithmic functionality
   to detect a lane of travel in the supplied image.
 */

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Size;


import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.FONT_HERSHEY_SIMPLEX;

public class LaneDetector {

    public LaneDetector() {
        /* Perform initialization here. */
    }

    public List<MatOfPoint> detect(CameraBridgeViewBase.CvCameraViewFrame image, Mat outputImage, CameraCalibrator calibrator) {

        List<MatOfPoint> lanePoints = new ArrayList<MatOfPoint>();
        Mat linesHough = new Mat();
        Mat tempImage = new Mat();
        Mat skyTransformHomographyMatrix = new Mat();

        /* Undistort the original image and the grayscale image, if calibrated. */
        if (calibrator.isCalibrated()) {
            Imgproc.undistort(image.rgba(), outputImage, calibrator.getCameraMatrix(), calibrator.getDistortionCoefficients());
            Imgproc.undistort(image.gray(), tempImage, calibrator.getCameraMatrix(), calibrator.getDistortionCoefficients());
            Imgproc.putText(outputImage, "CORRECTED",
                    new Point(outputImage.width() * 0.6, outputImage.height() * 0.1),
                    FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(255, 255, 0));
        }
        else {
            /* Use distorted images if not calibrated. */
            image.rgba().copyTo(outputImage);
            image.gray().copyTo(tempImage);
        }

        /* Process the image and detect a lane. Return the points that identify the lane. */
        Imgproc.GaussianBlur(tempImage, tempImage, new Size(5,5), 3, 3);
        Imgproc.Canny(tempImage, tempImage, 10, 100);
        Imgproc.HoughLinesP(tempImage, linesHough, 1, Math.PI / 180, 5, 100, 10);
        Point pt1 = new Point();
        Point pt2 = new Point();
        for (int x = 0; x < linesHough.rows(); x++) {
            double[] l = linesHough.get(x, 0);
            Imgproc.line(outputImage, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA, 0);
        }

        /* Convert image to sky view */
        // If the reference point (0, 0) is the top left, the points are at:
        // (Image.width * 0.45, Image.height * 0.5) - Polygon upper left corner
        // (Image.width * 0.55, Image.height * 0.5) - Polygon upper right corner
        // (Image.width, Image.height)              - Polygon lower right corner
        // (0, Image.height)                        - Polygon lower left corner
        // I believe you can define the points in any order, as long as you stay consistent.
        // The homography matrix will be recalculated accordingly.
        MatOfPoint2f src = new MatOfPoint2f(
                new Point(tempImage.width() * 0.45, tempImage.height() * 0.5),
                new Point(tempImage.width() * 0.55, tempImage.height() * 0.5),
                new Point(tempImage.width(), tempImage.height()),
                new Point(0, tempImage.height())
        );
        MatOfPoint2f dst = new MatOfPoint2f(
                new Point(tempImage.width() * 0.45, tempImage.height() * 0.5),
                new Point(tempImage.width() * 0.55, tempImage.height() * 0.5),
                new Point(tempImage.width() * 0.45, tempImage.height()),
                new Point(tempImage.width() * 0.55, tempImage.height())
        );

//        MatOfPoint2f Src = new MatOfPoint2f(
//                new Point(tempImage.width() * 0.45, tempImage.height() * 0.5),
//                new Point(tempImage.width() * 0.55, tempImage.height() * 0.5),
//                new Point(tempImage.width(), tempImage.height()),
//                new Point(0, tempImage.height())
//        );
//        MatOfPoint2f Dst = new MatOfPoint2f(
//                new Point(tempImage.width() * 0.45, tempImage.height() * 0.5),
//                new Point(tempImage.width() * 0.55, tempImage.height() * 0.5),
//                new Point(tempImage.width(), tempImage.height()),
//                new Point(0, tempImage.height())
//        );

        skyTransformHomographyMatrix = Imgproc.getPerspectiveTransform(src, dst);
        // TODO: Would be nice to have the image selectable: normal or sky view
        Imgproc.warpPerspective(outputImage, outputImage, skyTransformHomographyMatrix, outputImage.size());


        return lanePoints;
    }
}
