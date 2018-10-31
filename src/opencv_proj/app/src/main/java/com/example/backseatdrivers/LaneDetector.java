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

    private void transformToSkyView(Mat in, Mat out) {
        Mat skyTransformHomographyMatrix;
        Point[] src = new Point[4];
        Point[] dst = new Point[4];

        /* Source region is a trapezoid */
//        src[0] = new Point(in.cols()*0.45, in.rows()*0.45);
        src[0] = new Point(in.cols()*0.0725, in.rows()*0.45);
        src[1] = new Point(in.cols()*0.575, in.rows()*0.45);
        src[2] = new Point(in.cols()-1, in.rows()-1);
        src[3] = new Point(0, in.rows()-1);

        /* Destination region is the full image mat */
//        dst[0] = new Point(out.cols()*0.45, out.rows()*0.45);
        dst[0] = new Point(out.cols()*0.0725, out.rows()*0.45);
        dst[1] = new Point(out.cols()*0.575, out.rows()*0.45);
        dst[2] = new Point(out.cols()*0.55, out.rows()-1);
        dst[3] = new Point(out.cols()*0.45, out.rows()-1);

        /* Stretch the trapezoid area to the full image mat */
        skyTransformHomographyMatrix = Imgproc.getPerspectiveTransform(
                new MatOfPoint2f(src[0], src[1], src[2], src[3]),
                new MatOfPoint2f(dst[0], dst[1], dst[2], dst[3]));
        Imgproc.warpPerspective(in, out, skyTransformHomographyMatrix, out.size());
    }

    public List<MatOfPoint> detect(CameraBridgeViewBase.CvCameraViewFrame image, Mat outputImage, CameraCalibrator calibrator, boolean inSkyView) {
        return detect(image.rgba(), image.gray(), outputImage, calibrator, inSkyView);
    }

    public List<MatOfPoint> detect(Mat rgba, Mat gray, Mat outputImage, CameraCalibrator calibrator, boolean inSkyView) {
        List<MatOfPoint> lanePoints = new ArrayList<MatOfPoint>();
        Mat linesHough = new Mat();
        Mat tempImage = new Mat();
        boolean undistorted = false;

        /* Undistort the original image and the grayscale image, if calibrated. */
        if (calibrator != null) {
            if (calibrator.isCalibrated()) {
                Imgproc.undistort(rgba, outputImage, calibrator.getCameraMatrix(), calibrator.getDistortionCoefficients());
                Imgproc.undistort(gray, tempImage, calibrator.getCameraMatrix(), calibrator.getDistortionCoefficients());
                Imgproc.putText(outputImage, "CORRECTED",
                        new Point(outputImage.width() * 0.6, outputImage.height() * 0.1),
                        FONT_HERSHEY_SIMPLEX, 1.0, new Scalar(255, 255, 0));
                undistorted = true;
            }
        }
        if (!undistorted) {
            /* Use distorted images if not calibrated. */
            rgba.copyTo(outputImage);
            gray.copyTo(tempImage);
        }

        /* Process the image and detect a lane. Return the points that identify the lane. */
        Imgproc.GaussianBlur(tempImage, tempImage, new Size(5,5), 3, 3);
        Imgproc.Canny(tempImage, tempImage, 10, 100);
        Imgproc.HoughLinesP(tempImage, linesHough, 1, Math.PI / 180, 5, 100, 10);
        for (int x = 0; x < linesHough.rows(); x++) {
            double[] l = linesHough.get(x, 0);
            Imgproc.line(outputImage, new Point(l[0], l[1]), new Point(l[2], l[3]), new Scalar(255, 0, 0), 1, Imgproc.LINE_AA, 0);
        }

        /* Convert image to sky view */
        if (inSkyView) {
            transformToSkyView(outputImage, outputImage);
        }
        else {
            transformToSkyView(tempImage, tempImage);
        }

        return lanePoints;
    }
}
