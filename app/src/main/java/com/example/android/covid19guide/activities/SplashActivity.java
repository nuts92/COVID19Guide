package com.example.android.covid19guide.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.example.android.covid19guide.R;

/**
 * This activity class displays the first startup screen which appears when the COVID19 Guide App is opened.
 * It is a simple constant screen for a fixed amount of time which is used to display the App Logo
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Declaring and initializing splashTimeOut variable which represents the amount of time that the splash screen will be displayed.
        int splashTimeOut = 3000;

        //Set time to handler and call Handler().postDelayed, it will call run method of runnable after set time and then redirect to
        //the MainActivity where the user can view the latest News' Articles regarding the COVID19 Virus.
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);

                startActivity(homeIntent);

                finish();
            }
        }, splashTimeOut);

        // Declaring and initializing the animation and logo object variables
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.transition_animation);

        ImageView logo = findViewById(R.id.logo);

        //setting and assigning the animation object to the logo ImageView object so that the logo is displayed with this animation.
        logo.setAnimation(animation);
    }
}
