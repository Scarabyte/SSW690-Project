package com.example.backseatdrivers;

/*
   The LaneDetector class performs image processing and algorithmic functionality
   to detect a lane of travel in the supplied image.
 */

import android.util.Log;

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
    private static final String TAG = "LaneDetector";
    private boolean mDoneOnce = false;
    private boolean mScan = false;

    public LaneDetector() {
        /* Perform initialization here. */
    }

    private void transformToSkyView(Mat in, Mat out) {
        Mat skyTransformHomographyMatrix;
        Point[] src = new Point[4];
        Point[] dst = new Point[4];

        /* Source region is a trapezoid */
        src[0] = new Point(in.cols()*0.4, in.rows()*0.5);
        src[1] = new Point(in.cols()*0.6, in.rows()*0.5);
        src[2] = new Point(in.cols()-1, in.rows()-1);
        src[3] = new Point(0, in.rows()-1);

        /* Destination region is the full image mat */
        dst[0] = new Point(0,0);
        dst[1] = new Point(out.cols()-1, 0);
        dst[2] = new Point(out.cols()-1, out.rows()-1);
        dst[3] = new Point(0, out.rows()-1);

        /* Stretch the trapezoid area to the full image mat */
        skyTransformHomographyMatrix = Imgproc.getPerspectiveTransform(
                new MatOfPoint2f(src[0], src[1], src[2], src[3]),
                new MatOfPoint2f(dst[0], dst[1], dst[2], dst[3]));
        Imgproc.warpPerspective(in, out, skyTransformHomographyMatrix, out.size());
    }

    private Point findLaneLine(Mat in, int y, boolean left) {
        int x1, x2, x, start, stop, minX, maxX;
        double[] point;
        double threshold = 200.0;
        boolean found = false;
        if (left) {
            start = 0;
            stop = in.width() / 2;
        }
        else {
            start = in.width() / 2;
            stop = in.width() - 1;
        }
        x1 = minX = start-1;
        x2 = maxX = stop+1;
        // Find the left-most pixel.
        for (x = start; x < stop && x1 == minX; x++) {
            point = in.get(y,x);
            if (point != null) {
                if (point[0] > threshold) {
                    x1 = x;
                }
            }
        }
        if (x1 >= 0) {
            // Find the right-most pixel.
            for (x = stop; x > x1 && x > start && x2 == maxX; x--) {
                point = in.get(y,x);
                if (point != null) {
                    if (point[0] > threshold) {
                        x2 = x;
                        found = true;
                    }
                }
            }
        }
/*        String msg = "Y("+y+")";
        for (x=0; x<in.width()-1; x++) {
            if (in.get(x,y) != null) {
                if (in.get(y,x)[0] > 200.0) {
                    msg += "X";
                }
                else {
                    msg += "-";
                }
            }
            else {
                msg += "0";
            }
        }
        Log.d(TAG,msg);
*/        if (!found) return null;
        return new Point(x1,x2);
    }

    private void findLaneLines(Mat in, Mat out) {
        int lx1, lx2, rx1, rx2, midL, midR;
        int windowWidth = 50;
        Scalar color = new Scalar(255, 0, 255); // Magenta
        Scalar marker = new Scalar(0, 255, 0); // Green

        for (int y=in.height()-1; y >= 0; y-=10) {
            // Start at the right of the left bounding box and scan to the
            // end of the left bounding box.
            Point lineL = findLaneLine(in, y, true);
            if (lineL != null) {
                midL = ((int) lineL.x + (((int) lineL.y - (int) lineL.x) / 2));
                lx1 = (int) lineL.x - (windowWidth) / 2;
                if (lx1 < 0) lx1 = 0;
                lx2 = (int) lineL.y + (windowWidth) / 2;
                if (lx2 > (in.width() / 2) - 1) lx2 = (in.width() / 2) - 1;
                Imgproc.line(out, new Point(lx1,y), new Point(lx2,y), color);
                Imgproc.drawMarker(out, new Point(midL,y), marker, Imgproc.MARKER_DIAMOND, 6, 3);
            }

            // Start at the left of the right bounding box and scan to the
            // end of the right bounding box.
            Point lineR = findLaneLine(in, y, false);
            if (lineR != null) {
                midR = ((int) lineR.x + (((int) lineR.y - (int) lineR.x) / 2));
                rx1 = (int) lineR.x - (windowWidth) / 2;
                if (rx1 < (in.width() / 2 + 1)) rx1 = in.width() / 2 + 1;
                rx2 = (int) lineR.y + (windowWidth) / 2;
                if (rx2 > in.width() - 1) rx2 = in.width() - 1;
                Imgproc.line(out, new Point(rx1,y), new Point(rx2,y), color);
                Imgproc.drawMarker(out, new Point(midR,y), marker, Imgproc.MARKER_DIAMOND, 6, 3);
            }
        }
    }

    public void doit() {
        mScan = true;
    }

    public List<MatOfPoint> detect(CameraBridgeViewBase.CvCameraViewFrame image, Mat outputImage,
                                   CameraCalibrator calibrator, boolean inSkyView) {
        return detect(image.rgba(), image.gray(), outputImage, calibrator, inSkyView);
    }

    public List<MatOfPoint> detect(Mat rgba, Mat gray, Mat outputImage, CameraCalibrator calibrator, boolean inSkyView) {
        List<MatOfPoint> lanePoints = new ArrayList<MatOfPoint>();
        Mat linesHough = new Mat();
        Mat tempImage = new Mat();
        Mat sobelImage = new Mat();
        Mat scanned = new Mat();
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
            Imgproc.line(outputImage, new Point(l[0], l[1]), new Point(l[2], l[3]),
                    new Scalar(255, 0, 0), 1, Imgproc.LINE_AA, 0);
        }

        /* Convert image to sky view */
        if (inSkyView) {
            //transformToSkyView(outputImage, outputImage);
            transformToSkyView(gray, tempImage);
            Imgproc.Sobel(tempImage, sobelImage, tempImage.depth(), 1, 0, 3, 1);
            Imgproc.threshold(sobelImage, scanned, 37.5, 255, Imgproc.THRESH_BINARY);
            if (mScan) {
                findLaneLines(scanned, tempImage);
            }
            tempImage.copyTo(outputImage);
        }
        else {
            transformToSkyView(tempImage, tempImage);
        }

        return lanePoints;
    }
}
