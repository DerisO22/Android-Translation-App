package com.example.translationappandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class homeActivity extends AppCompatActivity {

    Button btn;
    Button btn2;
    Button btn3;
    Button btn4;

    float x1,x2,y1,y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Translation App");

        //Navigate Between Windows From Home Page
        btn = (Button) findViewById(R.id.button1);
        btn2 =(Button) findViewById(R.id.button2);
        btn3 = (Button) findViewById(R.id.button3);
        btn4 =(Button) findViewById(R.id.button4);

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