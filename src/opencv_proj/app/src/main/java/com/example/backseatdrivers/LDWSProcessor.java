package com.example.backseatdrivers;

/*
   The LDWSProcessor class processes an image (either a frame of a video
   or a camera snapshot) for the purposes of Lane Departure Warning.
 */

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;

import java.util.List;

public class LDWSProcessor {

    private LaneDetector mLaneDetector;
    private DepartureNotifier mDepartureNotifier;

    public LDWSProcessor() {

        /* Initialize stuff here. */
        mLaneDetector = new LaneDetector();
        mDepartureNotifier = new DepartureNotifier();

    }

    public LaneDetector getLaneDetector() {
        return mLaneDetector;
    }

    public void process(CameraBridgeViewBase.CvCameraViewFrame image, Mat outputImage,
                CameraCalibrator calibrator, boolean inSkyView) {
        /* Process an image here. */
        List<MatOfPoint> lanePoints = mLaneDetector.detect(image, outputImage, calibrator, inSkyView);
        if (check_lane_departure(lanePoints)) {
            /* Notify the operator of a potential lane departure. */
            mDepartureNotifier.notifyOperator();
        }

    }

    private boolean check_lane_departure(List<MatOfPoint> lanePoints) {

        /* Determine if we're departing the lane. Return true if departing. */
        boolean departing = false;
        return departing;

    }

}
