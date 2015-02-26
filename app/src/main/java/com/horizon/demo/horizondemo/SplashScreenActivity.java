package com.horizon.demo.horizondemo;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;


public class SplashScreenActivity extends Activity {

    //variables for making the mock progress bar fake a loading operation
    private ProgressBar mProgress;
    private int mProgressStatus=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        //animate logo by using the 'logo_animated.xml' file and a sequence of still images
        ImageView img = (ImageView)findViewById(R.id.loadingLogoView);
        img.setBackgroundResource(R.drawable.logo_animated);
        AnimationDrawable frameAnimation = (AnimationDrawable)img.getBackground();
        frameAnimation.start();

        //find the progress bar item in the layout and assign it to the Progress Bar variable here
        //in the code
        mProgress=(ProgressBar) findViewById(R.id.progressBar);
        //start a lengthy operation in the background to fake some sort of loading operation
        new Thread(new Runnable(){
            public void run(){
                mProgressStatus=mProgress.getProgress();
                while(mProgressStatus<100){
                    try {
                        Thread.sleep(40);
                        mProgressStatus += Math.random()*3;
                        if(mProgressStatus<100)
                            mProgress.setProgress(mProgressStatus);
                        else if(mProgressStatus>=100)
                            mProgress.setProgress(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //after the progress bar is filled, end this splash screen activity
                finish();
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        //TODO:implement proper way to stop the loading of the app. this just allows the back button to be pressed only when the progress bar is full. Bad form.
        if(mProgress.getProgress()==100)
            super.onBackPressed();
    }
}
