package com.example.backseatdrivers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.core.Mat;

public class DisplayStockImageActivity extends AppCompatActivity {

    private LinearLayout mLinearLayout;

    private CameraCalibrator     dCalibrator;
    private LDWSProcessor        dLDWSProcessor;

    private int                  mWidth;
    private int                  mHeight;

    public DisplayStockImageActivity() {

        /* Initialize stuff here. */
        dCalibrator = new CameraCalibrator(mWidth, mHeight);
        dLDWSProcessor = new LDWSProcessor();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLinearLayout = new LinearLayout(this);
        ImageView i = new ImageView(this);
        CvCameraViewFrame inputFrame = ((CvCameraViewFrame)i);
        Mat outputImage = new Mat();
        dLDWSProcessor.process(inputFrame, outputImage, dCalibrator);

        /* i.setImageResource(R.drawable.single_lane_3);
        i.setAdjustViewBounds(true);
        i.setLayoutParams(
                new LinearLayout.LayoutParams(
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT)));
        mLinearLayout.addView(i);
        setContentView(mLinearLayout); */

        ImageView output = ((ImageView)outputImage);
        output.setImageResource(R.drawable.single_lane_3);
        output.setAdjustViewBounds(true);
        output.setLayoutParams(
                new LinearLayout.LayoutParams(
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT)));
        mLinearLayout.addView(output);
        setContentView(mLinearLayout);
    }
}
