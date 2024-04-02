package com.example.translationappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Random;

public class enter_culture_splash extends AppCompatActivity {

    private TextView fun_fact;
    Random randomNum = new Random();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_culture_splash);

        //Randomized Fun Fact For Enter Splash
        fun_fact = findViewById(R.id.funFact_View);
        int randomIndex = randomNum.nextInt(enter_practice_tipString.tips.length);
        fun_fact.setText(enter_practice_tipString.tips[randomIndex]);

        Thread mSplashThread;

        mSplashThread = new Thread(){
            @Override public void run(){
                try {
                    synchronized (this){
                        wait(3000);
                    }
                } catch (InterruptedException ignored){
                } finally {
                    startActivity(new Intent(getApplicationContext(), activity_practice_culture.class));
                    finish();
                }
            }

        };

        mSplashThread.start();
    }
}