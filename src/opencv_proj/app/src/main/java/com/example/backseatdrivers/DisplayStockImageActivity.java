package com.example.backseatdrivers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayStockImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_stock_image);

        TextView textView = findViewById(R.id.textView);
        textView.setText("It's ALIVE!!!");
    }
}
