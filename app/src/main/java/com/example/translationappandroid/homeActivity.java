package com.example.translationappandroid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

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

        //Bottom Navigation Panel
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch(item.getItemId()){
                case R.id.bottom_home:
                    return true;
                case R.id.bottom_text:
                    startActivity(new Intent(getApplicationContext(), textActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    finish();
                    return true;
                case R.id.bottom_voice:
                    startActivity(new Intent(getApplicationContext(), voiceActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    finish();
                    return true;
                case R.id.bottom_image:
                    startActivity(new Intent(getApplicationContext(), imageActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    finish();
                    return true;
                case R.id.bottom_quiz:
                    startActivity(new Intent(getApplicationContext(), practiceActivity.class));
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    finish();
                    return true;
            }
            return false;
        });

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

