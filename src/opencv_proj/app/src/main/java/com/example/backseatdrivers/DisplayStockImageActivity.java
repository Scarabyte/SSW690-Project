package com.example.backseatdrivers;

import android.content.res.Resources;
import android.graphics.Bitmap;
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
    private static final String TAG = "DisplayStockImageActivi";

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
            cvImage = Utils.loadResource(this, R.drawable.single_lane_3);
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
            LDWSProcessor ldws = new LDWSProcessor();
            ldws.getLaneDetector().detect(cvRgba, cvGray, outputImage, null);
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
        final Resources res = getResources();
        switch (item.getItemId()) {
            case R.id.process_stock_image:
                Mat newImage = processImage();
                if (newImage != null) {
                    Bitmap bmImage = Bitmap.createBitmap(newImage.cols(), newImage.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(newImage, bmImage);
                    ImageView iv = findViewById(R.id.stock_image_view);
                    iv.setImageBitmap(bmImage);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
