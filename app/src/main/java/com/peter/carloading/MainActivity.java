package com.peter.carloading;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.peter.carloading.widgets.CarLoadingView;

public class MainActivity extends AppCompatActivity {

    private CarLoadingView carLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carLoadingView = (CarLoadingView) findViewById(R.id.car_loading_view);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                carLoadingView.startAnim();
            }
        },2000);
    }
}
