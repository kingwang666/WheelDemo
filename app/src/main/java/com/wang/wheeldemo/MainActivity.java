package com.wang.wheeldemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onMultiPicker(View view) {
        Intent intent = new Intent(this, MultiPickActivity.class);
        startActivity(intent);
    }

    public void onIOSMultiPicker(View view) {
        Intent intent = new Intent(this, IOSMultiPickActivity.class);
        startActivity(intent);
    }

    public void onWheel(View view) {
        Intent intent = new Intent(this, PickActivity.class);
        startActivity(intent);
    }

    public void onIOSWheel(View view) {
        Intent intent = new Intent(this, IOSPickActivity.class);
        startActivity(intent);
    }
}
