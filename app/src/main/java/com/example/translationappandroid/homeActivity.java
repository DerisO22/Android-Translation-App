package com.example.translationappandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class homeActivity extends AppCompatActivity {

    androidx.cardview.widget.CardView btn;
    androidx.cardview.widget.CardView btn2;
    androidx.cardview.widget.CardView btn3;
    androidx.cardview.widget.CardView btn4;
    Animation scaleUp, scaleDown;

    float x1,x2,y1,y2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Translation App");

        //Navigate Between Windows From Home Page
        btn = (CardView) findViewById(R.id.button1);
        btn2 = (CardView) findViewById(R.id.button2);
        btn3 = (CardView) findViewById(R.id.button3);
        btn4 = (CardView) findViewById(R.id.button4);

        //Button Animation
        scaleUp = AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.scale_down);


        //Text Button
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                btn.startAnimation(scaleUp);
                btn.startAnimation(scaleDown);
                Intent intent = new Intent(homeActivity.this, textActivity.class);
                startActivity(intent);
            }
        });
        //Voice Button
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                btn2.startAnimation(scaleUp);
                btn2.startAnimation(scaleDown);
                Intent intent = new Intent(homeActivity.this, voiceActivity.class);
                startActivity(intent);
            }
        });
        //Image Button
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                btn3.startAnimation(scaleUp);
                btn3.startAnimation(scaleDown);
                Intent intent = new Intent(homeActivity.this, imageActivity.class);
                startActivity(intent);
            }
        });
        //Practice Button
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                btn4.startAnimation(scaleUp);
                btn4.startAnimation(scaleDown);
                Intent intent = new Intent(homeActivity.this, practiceActivity.class);
                startActivity(intent);
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
                if(x1-150 > x2){
                    Intent i = new Intent(homeActivity.this, textActivity.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}