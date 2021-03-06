package com.example.backseatdrivers;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

public class DisplayStockImageActivity extends AppCompatActivity {
    private static final String TAG = "DisplayStockImageActivity";
    private LDWSProcessor mLDWS = new LDWSProcessor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stock_image);
        Toolbar tb = findViewById(R.id.stock_image_toolbar);
        setSupportActionBar(tb);
    }

    protected Mat processImage() {
        Mat cvImage = null;
        Mat cvRgba = null;
        Mat cvGray = null;
        Mat outputImage = null;
        try {
            cvImage = Utils.loadResource(this, R.drawable.test01);
        }
        catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        if (cvImage != null) {
            outputImage = new Mat();
            cvRgba = new Mat();
            cvGray = new Mat();
            Imgproc.cvtColor(cvImage, cvRgba, Imgproc.COLOR_RGB2RGBA);
            Imgproc.cvtColor(cvImage, cvGray, Imgproc.COLOR_RGB2GRAY);
            mLDWS.getLaneDetector().detect(cvRgba, cvGray, outputImage, null, false);
        }
        return outputImage;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.stockimage, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.view_undistorted:
                mLDWS.getLaneDetector().SetViewToShow(LaneDetector.SHOW_UNDISTORTED);
                break;
            case R.id.view_grayscale:
                mLDWS.getLaneDetector().SetViewToShow(LaneDetector.SHOW_GRAYSCALE);
                break;
            case R.id.view_skyview:
                mLDWS.getLaneDetector().SetViewToShow(LaneDetector.SHOW_SKY_VIEW);
                break;
            case R.id.view_sobel:
                mLDWS.getLaneDetector().SetViewToShow(LaneDetector.SHOW_SOBEL);
                break;
            case R.id.view_threshold:
                mLDWS.getLaneDetector().SetViewToShow(LaneDetector.SHOW_THRESHOLD);
                break;
            case R.id.view_lane_markers:
                mLDWS.getLaneDetector().SetViewToShow(LaneDetector.SHOW_LANE_MARKERS);
                break;
            case R.id.view_polynomial:
                mLDWS.getLaneDetector().SetViewToShow(LaneDetector.SHOW_POLYNOMIAL);
                break;
            case R.id.view_position:
                mLDWS.getLaneDetector().SetViewToShow(LaneDetector.SHOW_POSITION);
                break;
            case R.id.view_polygon:
                mLDWS.getLaneDetector().SetViewToShow(LaneDetector.SHOW_POLYGON);
                break;
            case R.id.view_normal:
                mLDWS.getLaneDetector().SetViewToShow(LaneDetector.SHOW_NORMAL_VIEW);
                break;
            case R.id.view_final:
                mLDWS.getLaneDetector().SetViewToShow(LaneDetector.SHOW_FINAL);
                break;
        }
        Mat newImage = processImage();
        if (newImage != null) {
            Bitmap bmImage = Bitmap.createBitmap(newImage.cols(), newImage.rows(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(newImage, bmImage);
            ImageView iv = findViewById(R.id.stock_image_view);
            iv.setImageBitmap(bmImage);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
        MediaPlayer warn = MediaPlayer.create(this, R.raw.depart);
        warn.start();
        return true;
    }
}
