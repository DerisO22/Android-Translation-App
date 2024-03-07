package com.example.translationappandroid;

import androidx.appcompat.app.AppCompatActivity;

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
    ImageButton monsterBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        setTitle("Practice");

        monsterBtn = findViewById(R.id.monster);
        scaleUp = AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.scale_down);

        monsterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                monsterBtn.startAnimation(scaleUp);
                monsterBtn.startAnimation(scaleDown);
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