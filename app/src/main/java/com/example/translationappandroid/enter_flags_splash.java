package com.example.translationappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class enter_flags_splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_flags_splash);

        Thread mSplashThread;

        mSplashThread = new Thread(){
            @Override public void run(){
                try {
                    synchronized (this){
                        wait(3000);
                    }
                } catch (InterruptedException ignored){
                } finally {
                    startActivity(new Intent(getApplicationContext(), activity_practice_flags.class));
                    finish();
                }
            }

        };

        mSplashThread.start();
    }
}