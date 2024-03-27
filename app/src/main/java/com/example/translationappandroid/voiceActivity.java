package com.example.translationappandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class voiceActivity extends AppCompatActivity {

    float x1,x2,y1,y2;
    TextView detectedText;
    Button getVoice;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);
        setTitle("Voice Translation");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.bottom_voice);

        detectedText = findViewById(R.id.detectedText);
        getVoice = findViewById(R.id.recordVoice);

        bottomNavigationView.setOnItemSelectedListener(item ->{
            switch(item.getItemId()){
                case R.id.bottom_voice:
                    return true;
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), homeActivity.class));
                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_text:
                    startActivity(new Intent(getApplicationContext(), textActivity.class));
                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_image:
                    startActivity(new Intent(getApplicationContext(), imageActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    finish();
                    return true;
                case R.id.bottom_quiz:
                    startActivity(new Intent(getApplicationContext(), practiceActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_right);
                    finish();
                    return true;
            }
            return false;
        });

        getVoice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                speak();
            }
        });
    }

    public void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode== RESULT_OK){
            detectedText.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        }
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
                    Intent i = new Intent(voiceActivity.this, textActivity.class);
                    startActivity(i);
                }else if(x1-150 > x2){
                    Intent i = new Intent(voiceActivity.this, imageActivity.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}