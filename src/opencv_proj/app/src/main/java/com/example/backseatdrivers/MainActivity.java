package com.example.backseatdrivers;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.SurfaceView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements OnTouchListener, CvCameraViewListener2 {
    private static final String  TAG              = "MainActivity";

    private int                  mWidth;
    private int                  mHeight;

    private CameraBridgeViewBase mOpenCvCameraView;
    private static CameraCalibrator     mCalibrator;
    private static OnCameraFrameRender  mOnCameraFrameRender;
    private LDWSProcessor        mLDWSProcessor;

    private static final int     MODE_LDWS = 0;
    private static final int     MODE_CALIBRATION = 1;
    private static final int     MODE_SKYVIEW = 2;
    private int                  mMode = MODE_LDWS;

//    private boolean              mInSkyView = false;

    private BaseLoaderCallback   mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                    mOpenCvCameraView.setOnTouchListener(MainActivity.this);
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.color_blob_detection_surface_view);

        mOpenCvCameraView = findViewById(R.id.color_blob_detection_activity_surface_view);
        mOpenCvCameraView.setMaxFrameSize(900,900);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setCvCameraViewListener(this);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.preview_mode).setEnabled(true);
        if (!mCalibrator.isCalibrated())
            menu.findItem(R.id.preview_mode).setEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Resources res = getResources();
        switch (item.getItemId()) {
            case R.id.mode_skyview:
                mMode = MODE_SKYVIEW;
                item.setChecked(true);
                mOpenCvCameraView.enableView();
                return true;
            case R.id.mode_calibration:
                mMode = MODE_CALIBRATION;
                item.setChecked(true);
                mOpenCvCameraView.enableView();
                return true;
            case R.id.mode_lane_departure:
                mMode = MODE_LDWS;
                item.setChecked(true);
                mOpenCvCameraView.enableView();
                return true;
            case R.id.stock_image:
                item.setChecked(true);
                mOpenCvCameraView.disableView();
                Intent intent = new Intent(this, DisplayStockImageActivity.class);
                startActivity(intent);
                return true;
            case R.id.calibration:
                mOnCameraFrameRender =
                    new OnCameraFrameRender(new CalibrationFrameRender(mCalibrator));
                return true;
            case R.id.undistortion:
                mOnCameraFrameRender =
                    new OnCameraFrameRender(new UndistortionFrameRender(mCalibrator));
                item.setChecked(true);
                return true;
            case R.id.comparison:
                mOnCameraFrameRender =
                    new OnCameraFrameRender(new ComparisonFrameRender(mCalibrator, mWidth, mHeight, getResources()));
                item.setChecked(true);
                return true;
            case R.id.calibrate:
                if (mCalibrator.getCornersBufferSize() < 2) {
                    (Toast.makeText(this, res.getString(R.string.more_samples), Toast.LENGTH_SHORT)).show();
                    return super.onOptionsItemSelected(item);
                }
                mOnCameraFrameRender = new OnCameraFrameRender(new PreviewFrameRender());
                // start the AsyncTask, passing the Activity context
                // in to a custom constructor
                new MyTask(this).execute();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static class MyTask extends AsyncTask<Void, Void, Void> {

        private WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        MyTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }
        private ProgressDialog calibrationProgress;

        @Override
        protected void onPreExecute() {
            calibrationProgress = new ProgressDialog(activityReference.get());
            //calibrationProgress.setTitle(res.getString(R.string.calibrating));
            //calibrationProgress.setMessage(res.getString(R.string.please_wait));
            // TODO: Don't want to use hardcoded strings. Should be from strings.xml
            calibrationProgress.setTitle("Calibrating...");
            calibrationProgress.setMessage("Please wait");
            calibrationProgress.setCancelable(false);
            calibrationProgress.setIndeterminate(true);
            calibrationProgress.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            mCalibrator.calibrate();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            calibrationProgress.dismiss();
            mCalibrator.clearCorners();
            mOnCameraFrameRender = new OnCameraFrameRender(new CalibrationFrameRender(mCalibrator));
            //String resultMessage = (mCalibrator.isCalibrated()) ?
            //        res.getString(R.string.calibration_successful)  + " " + mCalibrator.getAvgReprojectionError() :
            //        res.getString(R.string.calibration_unsuccessful);
            // TODO: Don't want to use hardcoded strings. Should be from strings.xml
            String resultMessage = (mCalibrator.isCalibrated()) ?
                    "Successfully calibrated!\nAvg. re-projection error:"  + " " + mCalibrator.getAvgReprojectionError() :
                    "Unsuccessful calibration.\nTry again";
            Toast.makeText(activityReference.get(), resultMessage, Toast.LENGTH_SHORT).show();

            if (mCalibrator.isCalibrated()) {
                CalibrationResult.save(activityReference.get(),
                        mCalibrator.getCameraMatrix(), mCalibrator.getDistortionCoefficients());
            }
        }
    }


    public void onCameraViewStarted(int width, int height) {
        mLDWSProcessor = new LDWSProcessor();
        if (mWidth != width || mHeight != height) {
            mWidth = width;
            mHeight = height;
            mCalibrator = new CameraCalibrator(mWidth, mHeight);
            if (CalibrationResult.tryLoad(this, mCalibrator.getCameraMatrix(), mCalibrator.getDistortionCoefficients())) {
                mCalibrator.setCalibrated();
            }
            mOnCameraFrameRender = new OnCameraFrameRender(new CalibrationFrameRender(mCalibrator));
        }
    }

    public void onCameraViewStopped() {
        return;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (mMode == MODE_CALIBRATION) {
            mCalibrator.addCorners();
        }
        return false; // don't need subsequent touch events
    }

//    public boolean inSkyView() {
//        if (mMode == MODE_SKYVIEW) { mInSkyView = true; }
//        else { mInSkyView = false; }
//
//        return mInSkyView;
//    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        /* Send the image to the LDWSProcessor to process a travel lane and detect
           whether the vehicle is leaving the travel lane. */
        Mat outputImage = inputFrame.rgba();
        if (mMode == MODE_LDWS) {
            outputImage = new Mat();
            mLDWSProcessor.process(inputFrame, outputImage, mCalibrator, false);
        }
        else if (mMode == MODE_SKYVIEW) {
            outputImage = new Mat();
            mLDWSProcessor.process(inputFrame, outputImage, mCalibrator, true);
        }
        else if (mMode == MODE_CALIBRATION) {
            outputImage = mOnCameraFrameRender.render(inputFrame);
        }

        return outputImage;
    }

}
