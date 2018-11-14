package com.example.backseatdrivers;

/*
   The LaneDetector class performs image processing and algorithmic functionality
   to detect a lane of travel in the supplied image.
 */

import android.util.Log;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
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

    private Point[] mROI = new Point[4];

    public LaneDetector() {
        /* Perform initialization here. */
        SetROI(46, 50, 54, 50, 70, 95, 30, 95);
    }

    public void SetROI(int ulx, int uly, int urx, int ury, int lrx, int lry, int llx, int lly) {
        mROI[0] = new Point(ulx/100.0, uly/100.0);
        mROI[1] = new Point(urx/100.0, ury/100.0);
        mROI[2] = new Point(lrx/100.0, lry/100.0);
        mROI[3] = new Point(llx/100.0, lly/100.0);
    }

    private void transformPoints(int x, int y, MatOfPoint2f srcPts, MatOfPoint2f dstPts) {
        Point[] src = new Point[4];
        Point[] dst = new Point[4];

        src[0] = new Point(x*mROI[0].x, y*mROI[0].y);
        src[1] = new Point(x*mROI[1].x, y*mROI[1].y);
        src[2] = new Point(x*mROI[2].x, y*mROI[2].y);
        src[3] = new Point(x*mROI[3].x, y*mROI[3].y);

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
//                dx = Math.abs(((int)markers.get(m).x) - (int)markers.get(m-1).x);
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

    private void findLaneLines(Mat in, Mat out) {
        int mid;
        LaneMarker lineL = null;
        LaneMarker lineR = null;
        Scalar colorL1 = new Scalar(255, 0, 255); // Magenta
        Scalar colorR1 = new Scalar(255, 255, 0); // Yellow
        Scalar colorL2 = new Scalar(0, 0, 255); // Blue
        Scalar colorR2 = new Scalar(255, 0, 0); // Red
        Scalar gray = new Scalar(64,64,64);
        List<LaneMarker> markersL = new ArrayList<>();
        List<LaneMarker> markersR = new ArrayList<>();
        List<LaneMarker> markers = new ArrayList<>();

        for (int y=in.height()-1; y >= 0; y-=10) {
            lineL = findLaneLine(in, y, true, lineL);
            if (lineL != null) {
                mid = ((int) lineL.x + (((int) lineL.y - (int) lineL.x) / 2));
                markersL.add(new LaneMarker(mid,y,true,1.0));
            }
            lineR = findLaneLine(in, y, false, lineR);
            if (lineR != null) {
                mid = ((int) lineR.x + (((int) lineR.y - (int) lineR.x) / 2));
                markersR.add(new LaneMarker(mid,y,true,1.0));
            }
        }

        // Fit curves to the detected lane points.
        WeightedObservedPoints obsL = new WeightedObservedPoints();
        WeightedObservedPoints obsR = new WeightedObservedPoints();
        PolynomialCurveFitter fitterL = PolynomialCurveFitter.create(2);
        PolynomialCurveFitter fitterR = PolynomialCurveFitter.create(2);
        markersL = filterMarkersByDistance(markersL);
//        markersL = filterMarkersBySlope(markersL);
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
                    }
                    coords += "("+(int)x+","+(int)y+")";
                }
                Log.d(TAG, coords);
            }
        }
        markersR = filterMarkersByDistance(markersR);
//        markersR = filterMarkersBySlope(markersR);
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
                for (int y = out.height()/5; y < out.height(); y += 17) {
                    double x = fR.value(y);
                    if ((int) x >= out.width()/2 && (int) x < out.width()) {
                        markers.add(new LaneMarker(x, y, true,1.0));
                        Imgproc.drawMarker(out, new Point(x, y), colorR2, Imgproc.MARKER_SQUARE, 4, 2);
                    }
                    coords += "("+(int)x+","+(int)y+")";
                }
                Log.d(TAG, coords);
            }
        }

        // Display a polygon on the image to highlight the travel lane.
        if (!markers.isEmpty()) {
            Point[] polyPoints = new Point[markers.size()];
            for (int m = 0; m < markers.size(); m++) {
                polyPoints[m] = markers.get(m);
            }
            List<MatOfPoint> mop = new ArrayList<>();
            mop.add(new MatOfPoint(polyPoints));
            Imgproc.fillPoly(out, mop, new Scalar(128, 32, 128));
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
        Imgproc.Canny(tempImage, tempImage, 35, 135);
        Imgproc.HoughLinesP(tempImage, linesHough, 1, Math.PI / 180, 5, 85, 25);
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
//        birdImage.copyTo(outputImage);
        transformToNormalView(birdImage,tempImage);
        Core.addWeighted(tempImage,0.5, rgba, 0.5, 0.0, outputImage);

        return lanePoints;
    }
}
