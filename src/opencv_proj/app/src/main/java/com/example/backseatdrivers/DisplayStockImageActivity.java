package com.example.backseatdrivers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DisplayStockImageActivity extends AppCompatActivity {

    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLinearLayout = new LinearLayout(this);
        ImageView i = new ImageView(this);
        i.setImageResource(R.drawable.single_lane_3);
        i.setAdjustViewBounds(true);
        i.setLayoutParams(
                new LinearLayout.LayoutParams(
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT)));
        mLinearLayout.addView(i);
        setContentView(mLinearLayout);
    }
}
