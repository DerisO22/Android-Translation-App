package com.example.translationappandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class practiceActivity extends AppCompatActivity {

    float x1,x2,y1,y2;
    Animation scaleUp, scaleDown;
    private CardView cultureCategBtn;
    private CardView flagsCategBtn;
    private CardView geographyCategBtn;
    private CardView languageCategBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        setTitle("Practice");

        //Category Buttons
        cultureCategBtn = findViewById(R.id.button1);
        flagsCategBtn = findViewById(R.id.button2);
        geographyCategBtn = findViewById(R.id.button3);
        languageCategBtn = findViewById(R.id.button4);

        scaleUp = AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.scale_down);

        cultureCategBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cultureCategBtn.startAnimation(scaleUp);
                cultureCategBtn.startAnimation(scaleDown);
            }
        });

        flagsCategBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagsCategBtn.startAnimation(scaleUp);
                flagsCategBtn.startAnimation(scaleDown);
            }
        });

        geographyCategBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                geographyCategBtn.startAnimation(scaleUp);
                geographyCategBtn.startAnimation(scaleDown);
            }
        });

        languageCategBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageCategBtn.startAnimation(scaleUp);
                languageCategBtn.startAnimation(scaleDown);
            }
        });
    }


    //Swipe Window Right
    public boolean onTouchEvent(MotionEvent touchEvent){
        switch(touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if(x1+150 < x2){
                    Intent i = new Intent(practiceActivity.this, imageActivity.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}