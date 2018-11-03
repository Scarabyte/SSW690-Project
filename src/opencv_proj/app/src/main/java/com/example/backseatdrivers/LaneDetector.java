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
import static org.opencv.core.Core.addWeighted;

public class LaneDetector {
    private static final String TAG = "LaneDetector";

    public LaneDetector() {
        /* Perform initialization here. */
    }

    private void transformPoints(int x, int y, MatOfPoint2f srcPts, MatOfPoint2f dstPts) {
        Point[] src = new Point[4];
        Point[] dst = new Point[4];

        /* Source region is a trapezoid */
        src[0] = new Point(x*0.45, y*0.50);
        src[1] = new Point(x*0.55, y*0.50);
        src[2] = new Point(x*0.90, y*0.95);
        src[3] = new Point(x*0.10, y*0.95);

        /* Destination region is the full image mat */
        dst[0] = new Point(x*0.3, 0);
        dst[1] = new Point(x*0.7, 0);
        dst[2] = new Point(x*0.7, y-1);
        dst[3] = new Point(x*0.3, y-1);

        srcPts.fromArray(src);
        dstPts.fromArray(dst);
    }

    private void transformToSkyView(Mat in, Mat out) {
        Mat skyTransformHomographyMatrix;
        MatOfPoint2f srcPts = new MatOfPoint2f();
        MatOfPoint2f dstPts = new MatOfPoint2f();

        /* Stretch the trapezoid area to the full image mat */
        transformPoints(in.cols(), in.rows(), srcPts, dstPts);
        skyTransformHomographyMatrix = Imgproc.getPerspectiveTransform(srcPts, dstPts);
        Imgproc.warpPerspective(in, out, skyTransformHomographyMatrix, out.size());
    }

    private void transformToNormalView(Mat in, Mat out) {
        Mat skyTransformHomographyMatrix;
        MatOfPoint2f srcPts = new MatOfPoint2f();
        MatOfPoint2f dstPts = new MatOfPoint2f();

        /* Stretch the trapezoid area to the full image mat */
        transformPoints(in.cols(), in.rows(), dstPts, srcPts);
        skyTransformHomographyMatrix = Imgproc.getPerspectiveTransform(srcPts, dstPts);
        Imgproc.warpPerspective(in, out, skyTransformHomographyMatrix, out.size());
    }

    private Point findLaneLine(Mat in, int y, boolean left, Point previous) {
        int x1, x2, x, start, stop, dx1, dx2, step;
        double[] point;
        double iThreshold = 200.0;
        int dThreshold = 30;
        int wThreshold = 3;
        boolean found = false;
        if (left) {
            start = in.width() / 2;
            stop = 0;
            step = -1;
        }
        else {
            start = in.width() / 2;
            stop = in.width() - 1;
            step = 1;
        }
        x1 = start - step;
        x2 = stop + step;
        // Find the first pixel transition from zero to one.
        for (x = start; x != stop; x += step) {
            point = in.get(y,x);
            if (point != null) {
                if (point[0] > iThreshold) {
                    x1 = x;
                    break;
                }
            }
        }
        if (x1 != start-1) {
            // Find the pixel transition from one to zero.
            for (x = x1 + step; x != stop; x += step) {
                point = in.get(y, x);
                if (point != null) {
                    if (point[0] < iThreshold) {
                        x2 = x;
                        found = true;
                        break;
                    }
                }
            }
        }

        if (!found)
            return null;

        // Discard points that are far away from the previous.
        if (previous != null) {
            dx1 = x1 - (int) previous.x;
            dx2 = x2 - (int) previous.y;
            if (dx1 > dThreshold || dx1 < -dThreshold || dx2 > dThreshold || dx2 < -dThreshold)
                return null;
        }

        // Discard points that are too narrow.
        if ( (left && (x1-x2) < wThreshold) || (!left && (x2-x1) < wThreshold))
            return null;

        // Return a found point.
        if (left) {
            int temp = x2;
            x2 = x1;
            x1 = temp;
        }
        return new Point(x1,x2);
    }

    private List<Point> filterMarkers(List<Point> markers) {
        int sumDx = 0;
        int avgDx;
        int dx;
        int lastX;
        List<Point> goodMarkers = new ArrayList<>();
        if (markers.size() > 0) {
            for (int m = 1; m < markers.size(); m++) {
                sumDx += Math.abs(markers.get(m).x - markers.get(m - 1).x);
            }
            avgDx = (sumDx / markers.size()) * 3;
            lastX = (int)markers.get(0).x;
            for (int m = 1; m < markers.size(); m++) {
                dx = Math.abs(((int)markers.get(m).x) - lastX);
                if (dx < avgDx) {
                    if (goodMarkers.isEmpty()) {
                        goodMarkers.add(markers.get(m - 1));
                    }
                    goodMarkers.add(markers.get(m));
                    lastX = (int)markers.get(m).x;
                }
                if (goodMarkers.isEmpty()) {
                    lastX = (int)markers.get(m).x;
                }
            }
        }
        return goodMarkers;
    }

    private void findLaneLines(Mat in, Mat out) {
        int mid;
        Point lineL = null;
        Point lineR = null;
        Scalar color = new Scalar(0, 255, 0); // Green
        List<Point> markersL = new ArrayList<>();
        List<Point> markersR = new ArrayList<>();
        List<Point> markers = new ArrayList<>();

        for (int y=in.height()-1; y >= 0; y-=10) {
            lineL = findLaneLine(in, y, true, lineL);
            if (lineL != null) {
                mid = ((int) lineL.x + (((int) lineL.y - (int) lineL.x) / 2));
                markersL.add(new Point(mid,y));
            }
            lineR = findLaneLine(in, y, false, lineR);
            if (lineR != null) {
                mid = ((int) lineR.x + (((int) lineR.y - (int) lineR.x) / 2));
                markersR.add(new Point(mid,y));
            }
        }
        markersL = filterMarkers(markersL);
        for (int m = 0; m < markersL.size(); m++) {
            markers.add(markersL.get(m));
            Imgproc.drawMarker(out, markersL.get(m), color, Imgproc.MARKER_DIAMOND, 4, 2);
        }
        markersR = filterMarkers(markersR);
        for (int m = markersR.size()-1; m >= 0; m--) {
            markers.add(markersR.get(m));
            Imgproc.drawMarker(out, markersR.get(m), color, Imgproc.MARKER_DIAMOND, 4,2 );
        }
        if (!markers.isEmpty()) {
            Point[] polyPoints = new Point[markers.size()];
            for (int m = 0; m < markers.size(); m++) {
                polyPoints[m] = markers.get(m);
            }
            List<MatOfPoint> mop = new ArrayList<>();
            mop.add(new MatOfPoint(polyPoints));
            Imgproc.fillPoly(out, mop, new Scalar(192, 192, 192));
        }
    }

    public List<MatOfPoint> detect(CameraBridgeViewBase.CvCameraViewFrame image, Mat outputImage,
                                   CameraCalibrator calibrator, boolean inSkyView) {
        return detect(image.rgba(), image.gray(), outputImage, calibrator, inSkyView);
    }

    public List<MatOfPoint> detect(Mat rgba, Mat gray, Mat outputImage, CameraCalibrator calibrator, boolean inSkyView) {
        List<MatOfPoint> lanePoints = new ArrayList<MatOfPoint>();
        Mat linesHough = new Mat();
        Mat tempImage = new Mat();
        Mat birdImage = new Mat();
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
/*        Imgproc.GaussianBlur(tempImage, tempImage, new Size(5,5), 3, 3);
        Imgproc.Canny(tempImage, tempImage, 10, 100);
        Imgproc.HoughLinesP(tempImage, linesHough, 1, Math.PI / 180, 5, 100, 10);
        for (int x = 0; x < linesHough.rows(); x++) {
            double[] l = linesHough.get(x, 0);
            Imgproc.line(outputImage, new Point(l[0], l[1]), new Point(l[2], l[3]),
                    new Scalar(255, 0, 0), 1, Imgproc.LINE_AA, 0);
        }
*/
        /* Convert image to sky view */
        transformToSkyView(gray, tempImage);
        transformToSkyView(rgba, birdImage);
        Imgproc.Sobel(tempImage, sobelImage, tempImage.depth(), 1, 0, 3, 1);
        Imgproc.threshold(sobelImage, scanned, 37.5, 255, Imgproc.THRESH_BINARY);
        findLaneLines(scanned, birdImage);
        transformToNormalView(birdImage,tempImage);
        Core.addWeighted(tempImage,0.5, rgba, 0.5, 0.0, outputImage);

        return lanePoints;
    }
}
