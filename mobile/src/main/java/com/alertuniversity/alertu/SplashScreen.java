package com.alertuniversity.alertu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;


/**
 * Created by Trekele on 4/23/15.
 */
public class SplashScreen extends Activity
{
    //this will be the duration the splash screen is shown in milli seconds
    private final int splashInterval = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                //pass this to the constructor of the new intent, the main activity will be the next screen that will be displayed
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                //starts the activity that is in the new Intent created above^
                startActivity(i);
                finish();
            }
        }, splashInterval);


    }
}
