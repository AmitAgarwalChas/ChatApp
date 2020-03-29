package com.example.chatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.chatapp.R;

public class MainActivity extends AppCompatActivity {

    static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView splashTitle = findViewById(R.id.splash_title);
        Animation splash = AnimationUtils.loadAnimation(this, R.anim.splash_animation);

        splashTitle.startAnimation(splash);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
