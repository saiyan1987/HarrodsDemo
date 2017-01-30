package com.example.faheem.harrodsdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Handler mHandler = new Handler();
        Runnable mStartActivityTask= new Runnable() {
            public void run() {
                Intent i = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
            }
        };
        mHandler.postDelayed(mStartActivityTask, 4000);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Handler mHandler = new Handler();
        Runnable mStartActivityTask= new Runnable() {
            public void run() {
                Intent i = new Intent(SplashActivity.this,MainActivity.class);
                startActivity(i);
            }
        };
        mHandler.postDelayed(mStartActivityTask, 6000);
    }
}
