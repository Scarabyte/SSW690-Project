package com.example.backseatdrivers;

/*
   The LaneDetector class performs image processing and algorithmic functionality
   to detect a lane of travel in the supplied image.
 */

import android.media.MediaPlayer;
import android.util.Log;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;


import java.util.ArrayList;
import java.util.List;

import static org.opencv.core.Core.FONT_HERSHEY_COMPLEX;
import static org.opencv.core.Core.FONT_HERSHEY_SIMPLEX;
import static org.opencv.core.Core.LINE_8;

public class LaneDetector {
    private static final String TAG = "LaneDetector";

    public static final int SHOW_FINAL = 0;
    public static final int SHOW_UNDISTORTED = 1;
    public static final int SHOW_GRAYSCALE = 2;
    public static final int SHOW_SKY_VIEW = 3;
    public static final int SHOW_SOBEL = 4;
    public static final int SHOW_THRESHOLD = 5;
    public static final int SHOW_LANE_MARKERS = 6;
    public static final int SHOW_POLYNOMIAL = 7;
    public static final int SHOW_POSITION = 8;
    public static final int SHOW_POLYGON = 9;
    public static final int SHOW_NORMAL_VIEW = 10;

    private int mViewToShow = SHOW_FINAL;
    private MediaPlayer mMediaPlayer = null;
    private Lane mPreviousLane = null;
    private boolean mAudioOn = true;

    private Point[] mROI = new Point[4];
    private Point[] mDST = new Point[4];

    public LaneDetector() {
        /* Perform initialization here. */
        SetROI(46, 65, 54, 65, 70, 95, 30, 95);
        SetDST(30, 0, 70, 0, 70, 100, 30, 100);
    }

    public void SetViewToShow(int viewCode) {
        mViewToShow = viewCode;
    }

    public void SetMediaPlayer(MediaPlayer p) {
        mMediaPlayer = p;
    }

    public void SetAudioOn(boolean state) {
        mAudioOn = state;
    }

    public void SetROI(int ulx, int uly, int urx, int ury, int lrx, int lry, int llx, int lly) {
        mROI[0] = new Point(ulx/100.0, uly/100.0);
        mROI[1] = new Point(urx/100.0, ury/100.0);
        mROI[2] = new Point(lrx/100.0, lry/100.0);
        mROI[3] = new Point(llx/100.0, lly/100.0);
    }

    public void SetDST(int ulx, int uly, int urx, int ury, int lrx, int lry, int llx, int lly) {
        mDST[0] = new Point(ulx/100.0, uly/100.0);
        mDST[1] = new Point(urx/100.0, ury/100.0);
        mDST[2] = new Point(lrx/100.0, lry/100.0);
        mDST[3] = new Point(llx/100.0, lly/100.0);
    }

    private double bound(double extent, double value) {
        if (value < 0.0) return 0.0;
        if (value > extent) return extent;
        return value;
    }

    private void transformPoints(int x, int y, MatOfPoint2f srcPts, MatOfPoint2f dstPts) {
        Point[] src = new Point[4];
        Point[] dst = new Point[4];

        src[0] = new Point(bound(x, x*mROI[0].x), bound(y, y*mROI[0].y));
        src[1] = new Point(bound(x, x*mROI[1].x), bound(y, y*mROI[1].y));
        src[2] = new Point(bound(x, x*mROI[2].x), bound(y, y*mROI[2].y));
        src[3] = new Point(bound(x, x*mROI[3].x), bound(y, y*mROI[3].y));

        dst[0] = new Point(bound(x, x*mDST[0].x), bound(y, y*mDST[0].y));
        dst[1] = new Point(bound(x, x*mDST[1].x), bound(y, y*mDST[1].y));
        dst[2] = new Point(bound(x, x*mDST[2].x), bound(y, y*mDST[2].y));
        dst[3] = new Point(bound(x, x*mDST[3].x), bound(y, y*mDST[3].y));

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

    private LaneMarker findLaneLine(Mat in, int y, boolean left, LaneMarker previous) {
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
                return new LaneMarker(x1, x2, false, 1.0);
        }

        // Discard points that are too narrow.
        if ( (left && (x1-x2) < wThreshold) || (!left && (x2-x1) < wThreshold))
            return new LaneMarker(x1, x2, false, 1.0);

        // Return a found point.
        if (left) {
            int temp = x2;
            x2 = x1;
            x1 = temp;
        }
        return new LaneMarker(x1,x2,true, 0.0);
    }

    private List<LaneMarker> filterMarkersBySlope(List<LaneMarker> markers) {
        List<LaneMarker> allMarkers = new ArrayList<>();
        double avgSlope = 0.0;
        double slope = 0.0;
        double sumSlope = 0.0;
        int slopeCount = 0;
        LaneMarker lastValid = null;
        double x1, y1, x2, y2;

        if (markers.size() > 0) {

            // Calculate the average slope of all the valid markers in the set.
            for (int m = 1; m < markers.size(); m++) {
                if (markers.get(m-1).v) {
                    lastValid = markers.get(m-1);
                }
                if (lastValid != null && markers.get(m).v) {
                    x1 = lastValid.x;
                    y1 = lastValid.y;
                    x2 = markers.get(m).x;
                    y2 = markers.get(m).y;

                    slope = Math.abs((y2 - y1) / (x2 - x1));
                    sumSlope += slope;

                    if (slopeCount == 0) {
                        slopeCount += 2; // Capture first two points
                    } else {
                        slopeCount++; // Capture next good point.
                    }
                }
            }
            avgSlope = sumSlope / slopeCount;

            // Invalidate those markers that have a slope higher than the average.
            lastValid = null;
            allMarkers.add(markers.get(0));
            for (int m = 1; m < markers.size(); m++) {
                if (markers.get(m-1).v) {
                    lastValid = markers.get(m-1);
                }
                if (lastValid != null && markers.get(m).v) {
                    x1 = lastValid.x;
                    y1 = lastValid.y;
                    x2 = markers.get(m).x;
                    y2 = markers.get(m).y;

                    slope = Math.abs((y2 - y1) / (x2 - x1));

                    if (Math.abs(slope-avgSlope) < 1.0) {
                        allMarkers.add(markers.get(m));
                    }
                    else {
                        allMarkers.add(new LaneMarker(markers.get(m).x, markers.get(m).y, false, 1.0));
                    }
                }
                else {
                    allMarkers.add(markers.get(m));
                }
            }

        }

        return allMarkers;
    }

    private List<LaneMarker> filterMarkersByDistance(List<LaneMarker> markers) {
        int sumDx = 0;
        int avgDx;
        int dx;
        int lastX;
        List<LaneMarker> goodMarkers = new ArrayList<>();
        List<LaneMarker> allMarkers = new ArrayList<>();
        if (markers.size() > 0) {
            for (int m = 1; m < markers.size(); m++) {
                sumDx += Math.abs(markers.get(m).x - markers.get(m - 1).x);
            }
            avgDx = (sumDx / markers.size()) * 1;
            lastX = (int)markers.get(0).x;
            for (int m = 1; m < markers.size(); m++) {
                dx = Math.abs(((int)markers.get(m).x) - lastX);
                if (dx < avgDx) {
                    if (goodMarkers.isEmpty()) {
                        goodMarkers.add(markers.get(m - 1));
                    }
                    goodMarkers.add(markers.get(m));
                    allMarkers.add(new LaneMarker(markers.get(m).x, markers.get(m).y, true, 1.0));
                    lastX = (int)markers.get(m).x;
                }
                else {
                    allMarkers.add(new LaneMarker(markers.get(m).x, markers.get(m).y, false, 1.0));
                }
                if (goodMarkers.isEmpty()) {
                    lastX = (int)markers.get(m).x;
                }
            }
        }
        return allMarkers;
    }

    private List<LaneMarker> weighMarkers(List<LaneMarker> markers) {
        // Weight is based on the horizontal and vertical distance from the previous point.
        // All points are considered.
        List<LaneMarker> allMarkers = new ArrayList<>();
        for (int m = 1; m < markers.size(); m++) {
            LaneMarker m1 = markers.get(m-1);
            LaneMarker m2 = markers.get(m);
            double dx = Math.abs(m1.x - m2.x);
            double dy = Math.abs(m1.y - m2.y);
            double weight = 1 / (dx+dy);
            if (allMarkers.isEmpty()) {
                allMarkers.add(new LaneMarker(m1.x, m1.y, m1.v, weight));
            }
            allMarkers.add(new LaneMarker(m2.x, m2.y, m2.v, weight));
        }
        return allMarkers;
    }

    private void findLaneLines(Mat in, Mat out, Lane lane, Lane previousLane, Mat temp) {
        int mid;
        LaneMarker lineL = null;
        LaneMarker lineR = null;
        Scalar colorL1 = new Scalar(255, 0, 255); // Magenta
        Scalar colorR1 = new Scalar(255, 255, 0); // Yellow
        Scalar colorL2 = new Scalar(0, 0, 255); // Blue
        Scalar colorR2 = new Scalar(255, 0, 0); // Red
        Scalar gray = new Scalar(63,63,63);
        Scalar green = new Scalar(31, 255, 31);
        Scalar red = new Scalar(255, 31, 31);
        Scalar blue = new Scalar(31, 31, 255);
        Scalar laneColor = gray;
        List<LaneMarker> markersL = new ArrayList<>();
        List<LaneMarker> markersR = new ArrayList<>();
        List<LaneMarker> markers = new ArrayList<>();
        List<LaneMarker> markersTemp = new ArrayList<>();

        for (int y=in.height()-1; y >= 0; y-=10) {
            lineL = findLaneLine(in, y, true, lineL);
            if (lineL != null) {
                mid = ((int) lineL.x + (((int) lineL.y - (int) lineL.x) / 2));
                markersL.add(new LaneMarker(mid,y,true,1.0));
                if (mViewToShow == SHOW_LANE_MARKERS) {
                    Imgproc.line(temp, new Point(lineL.x-10,y), new Point(lineL.x, y), red, 1, LINE_8);
                    Imgproc.line(temp, new Point(lineL.y,y), new Point(lineL.y+10, y), red, 1, LINE_8);
                    Imgproc.line(temp, new Point(lineL.x,y-4), new Point(lineL.x,y+4), red, 1, LINE_8);
                    Imgproc.line(temp, new Point(lineL.y,y-4), new Point(lineL.y,y+4), red, 1, LINE_8);
                }
            }
            lineR = findLaneLine(in, y, false, lineR);
            if (lineR != null) {
                mid = ((int) lineR.x + (((int) lineR.y - (int) lineR.x) / 2));
                markersR.add(new LaneMarker(mid,y,true,1.0));
                if (mViewToShow == SHOW_LANE_MARKERS) {
                    Imgproc.line(temp, new Point(lineR.x-10,y), new Point(lineR.x, y), red, 1, LINE_8);
                    Imgproc.line(temp, new Point(lineR.y,y), new Point(lineR.y+10, y), red, 1, LINE_8);
                    Imgproc.line(temp, new Point(lineR.x,y-4), new Point(lineR.x,y+4), red, 1, LINE_8);
                    Imgproc.line(temp, new Point(lineR.y,y-4), new Point(lineR.y,y+4), red, 1, LINE_8);
                }
            }
        }

        if (mViewToShow == SHOW_LANE_MARKERS) {
            return;
        }

        // Fit curves to the detected lane points.
        WeightedObservedPoints obsL = new WeightedObservedPoints();
        WeightedObservedPoints obsR = new WeightedObservedPoints();
        PolynomialCurveFitter fitterL = PolynomialCurveFitter.create(2);
        PolynomialCurveFitter fitterR = PolynomialCurveFitter.create(2);
        markersL = filterMarkersByDistance(markersL);
        markersL = weighMarkers(markersL);
        int countL = 0;
        if (!markersL.isEmpty()) {
            for (int m = 0; m < markersL.size(); m++) {
                if (markersL.get(m).v) {
                    Imgproc.drawMarker(out, markersL.get(m), colorL1, Imgproc.MARKER_DIAMOND, 6, 3);
                    obsL.add(markersL.get(m).w, markersL.get(m).y, markersL.get(m).x);
                    countL++;
                }
                else {
                    Imgproc.drawMarker(out, markersL.get(m), gray, Imgproc.MARKER_DIAMOND, 6, 3);
                }
            }
            if (countL > 0) {
                double coeffL[] = fitterL.fit(obsL.toList());
                String coords = "L:";
                for (int c = 0; c < coeffL.length; c++) {
                    Log.d(TAG, "Coefficient L#" + c + ": " + coeffL[c]);
                }
                PolynomialFunction fL = new PolynomialFunction(coeffL);
                for (int y = out.height()-1; y > out.height()/5; y -= 17) {
                    double x = fL.value(y);
                    if ((int) x >= 0 && (int) x < out.width()/2) {
                        markers.add(new LaneMarker(x, y, true,1.0));
                        Imgproc.drawMarker(out, new Point(x, y), colorL2, Imgproc.MARKER_SQUARE, 4, 2);
                        if (mViewToShow == SHOW_POLYNOMIAL) {
                            Imgproc.drawMarker(temp, new Point(x, y), colorL2, Imgproc.MARKER_SQUARE, 4, 2);
                        }
                    }
                    coords += "("+(int)x+","+(int)y+")";
                }
                Log.d(TAG, coords);
                lane.xL = new Point(fL.value(out.height()-10), out.height()-10);
            }
        }
        markersR = filterMarkersByDistance(markersR);
        markersR = weighMarkers(markersR);
        int countR = 0;
        if (!markersR.isEmpty()) {
            for (int m = 0; m < markersR.size(); m++) {
                if (markersR.get(m).v) {
                    Imgproc.drawMarker(out, markersR.get(m), colorR1, Imgproc.MARKER_DIAMOND, 6, 3);
                }
                else {
                    Imgproc.drawMarker(out, markersR.get(m), gray, Imgproc.MARKER_DIAMOND, 6, 3);
                }
                obsR.add(markersR.get(m).w, markersR.get(m).y, markersR.get(m).x);
                countR++;
            }
            if (countR > 0) {
                double coeffR[] = fitterR.fit(obsR.toList());
                String coords = "R:";
                for (int c = 0; c < coeffR.length; c++) {
                    Log.d(TAG, "Coefficient R#" + c + ": " + coeffR[c]);
                }
                PolynomialFunction fR = new PolynomialFunction(coeffR);
                for (int y = out.height()-1; y > out.height()/5; y -= 17) {
                    double x = fR.value(y);
                    if ((int) x >= out.width()/2 && (int) x < out.width()) {
                        markersTemp.add(new LaneMarker(x, y, true, 1.0));
                        Imgproc.drawMarker(out, new Point(x, y), colorR2, Imgproc.MARKER_SQUARE, 4, 2);
                        if (mViewToShow == SHOW_POLYNOMIAL) {
                            Imgproc.drawMarker(temp, new Point(x, y), colorR2, Imgproc.MARKER_SQUARE, 4, 2);
                        }
                    }
                    coords += "("+(int)x+","+(int)y+")";
                }
                Log.d(TAG, coords);
                for (int m = markersTemp.size()-1; m >= 0; m--){
                    markers.add(markersTemp.get(m));
                }
                lane.xR = new Point(fR.value(out.height()-10), out.height()-10);
            }
        }

        if (mViewToShow == SHOW_POLYNOMIAL) {
            return;
        }

        // Display the vehicle position and the center of the lane.
        Point vehicle = new Point(out.width()/2, out.height()-1);
        if (lane.xL != null && lane.xR != null) {
            Point middle = new Point(lane.xL.x + ((lane.xR.x-lane.xL.x)/2.0), out.height()-11);

            /* Calculate the percent from middle. */
            /* 0.0% is exactly in the middle. */
            /* -100.0% is all the way to the left. */
            /* +100.0% is all the way to the right. */
            double percentPerPixel = 200.0 / out.width();
            lane.percentFromCenter = (vehicle.x - middle.x) * percentPerPixel;

            /* If the vehicle is more than 10% from the center of the lane, set */
            /* the color of the lane polygon to red. Otherwise it's green.      */
            laneColor = green;
            if (lane.percentFromCenter > 10.0 || lane.percentFromCenter < -10.0) {
                laneColor = red;

                // Play a warning sound if departing.
                if (previousLane != null && mMediaPlayer != null && mAudioOn) {
                    if (previousLane.percentFromCenter >= -10.0 && previousLane.percentFromCenter <= 10.0) {
                        mMediaPlayer.start();
                    }
                }
            }
            if (mViewToShow == SHOW_POSITION) {
                Imgproc.line(temp, lane.xL, lane.xR, red, 4, LINE_8);
                Imgproc.line(temp, new Point(lane.xL.x, lane.xL.y-10), new Point(lane.xL.x, lane.xL.y+10), red, 4, LINE_8);
                Imgproc.line(temp, new Point(lane.xR.x, lane.xR.y-10), new Point(lane.xR.x, lane.xR.y+10), red, 4, LINE_8);
                Imgproc.drawMarker(temp, vehicle, blue, Imgproc.MARKER_TRIANGLE_UP, 10, 2);
                Imgproc.drawMarker(temp, middle, blue, Imgproc.MARKER_TRIANGLE_DOWN, 10, 2);
            }
        }

        if (mViewToShow == SHOW_POSITION) {
            return;
        }

        // Display a polygon on the image to highlight the travel lane.
        if (!markers.isEmpty()) {
            Point[] polyPoints = new Point[markers.size()];
            for (int m = 0; m < markers.size(); m++) {
                polyPoints[m] = markers.get(m);
            }
            List<MatOfPoint> mop = new ArrayList<>();
            mop.add(new MatOfPoint(polyPoints));
            Imgproc.fillPoly(out, mop, laneColor);
            if (mViewToShow == SHOW_POLYGON) {
                Imgproc.fillPoly(temp, mop, laneColor);
            }
        }

        if (mViewToShow == SHOW_POLYGON) {
            return;
        }

        // Draw the vehicle and lane position indicators.
        Imgproc.drawMarker(out, vehicle, blue, Imgproc.MARKER_TRIANGLE_UP, 10, 2);
        if (lane.xL != null && lane.xR != null) {
            Point middle = new Point(lane.xL.x + ((lane.xR.x - lane.xL.x) / 2.0), out.height() - 11);
            Imgproc.drawMarker(out, middle, blue, Imgproc.MARKER_TRIANGLE_DOWN, 10, 2);
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
        Lane theLane = new Lane();

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

        if (mViewToShow == SHOW_UNDISTORTED) {
            // Output image already has undistorted view.
            return lanePoints;
        }

        if (mViewToShow == SHOW_GRAYSCALE) {
            gray.copyTo(outputImage);
            return lanePoints;
        }

        /* Convert image to sky view */
        transformToSkyView(gray, tempImage);
        transformToSkyView(rgba, birdImage);

        if (mViewToShow == SHOW_SKY_VIEW) {
            tempImage.copyTo(outputImage);
            return lanePoints;
        }

        /* Run a Sobel filter to find the edges in the image. */
        Imgproc.Sobel(tempImage, sobelImage, tempImage.depth(), 1, 0, 3, 1);
        if (mViewToShow == SHOW_SOBEL) {
            sobelImage.copyTo(outputImage);
            return lanePoints;
        }

        /* Perform a binary threshold to filter out soft edges. */
        Imgproc.threshold(sobelImage, scanned, 37.5, 255, Imgproc.THRESH_BINARY);
        if (mViewToShow == SHOW_THRESHOLD) {
            scanned.copyTo(outputImage);
            return lanePoints;
        }

        /* Detect the lane lines and paint the results. */
        Mat intermediate = new Mat();
        Imgproc.cvtColor(scanned, intermediate, Imgproc.COLOR_GRAY2RGB);
        findLaneLines(scanned, birdImage, theLane, mPreviousLane, intermediate);
        mPreviousLane = theLane;

        if (mViewToShow == SHOW_LANE_MARKERS ||
            mViewToShow == SHOW_POLYNOMIAL ||
            mViewToShow == SHOW_POSITION ||
            mViewToShow == SHOW_POLYGON) {
            intermediate.copyTo(outputImage);
            return lanePoints;
        }

        /* Switch back to normal view. */
        transformToNormalView(birdImage,tempImage);
        if (mViewToShow == SHOW_NORMAL_VIEW) {
            tempImage.copyTo(outputImage);
            return lanePoints;
        }

        /* Combine the processed image with the original. */
        Core.addWeighted(tempImage,0.5, rgba, 0.5, 0.0, outputImage);

        /* Display the vehicle position data. */
        String percentString = String.format("%+02.1f%%", theLane.percentFromCenter);
        Imgproc.putText(outputImage, percentString,
                new Point(outputImage.width() * 0.45, outputImage.height() * 0.85),
                FONT_HERSHEY_COMPLEX, 0.8, new Scalar(31, 31, 255));

        return lanePoints;
    }
}
