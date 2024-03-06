package com.example.translationappandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class homeActivity extends AppCompatActivity {

    androidx.cardview.widget.CardView btn;
    androidx.cardview.widget.CardView btn2;
    androidx.cardview.widget.CardView btn3;
    androidx.cardview.widget.CardView btn4;

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

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(homeActivity.this, textActivity.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(homeActivity.this, voiceActivity.class);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(homeActivity.this, imageActivity.class);
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
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