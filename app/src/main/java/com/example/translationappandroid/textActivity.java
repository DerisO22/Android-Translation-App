package com.example.translationappandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class textActivity extends AppCompatActivity {

    float x1,x2,y1,y2;

    private Spinner fromSpinner, toSpinner;
    private TextInputEditText sourceEdt;
    private MaterialButton translateBtn;
    private TextView translatedText;
    String[] fromLanguages = {"From", "English", "German"};
    String[] toLanguages = {"To", "English", "German"};

    private static final int REQUEST_PERMISSION_CODE = 1;
    int languageCode, fromLangaugeCode, toLangaugeCode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        setTitle("Text Translation");

        fromSpinner = findViewById(R.id.firstLangMenu);
        toSpinner = findViewById(R.id.secondLangMenu);
        sourceEdt = findViewById(R.id.idEdtSource);
        translateBtn = findViewById(R.id.idBtnTranslate);
        translatedText = findViewById(R.id.idTranslatedText);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //fromLangaugeCode = getLangaugeCode(fromLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    Intent i = new Intent(textActivity.this, homeActivity.class);
                    startActivity(i);
                }else if(x1-150 > x2){
                    Intent i = new Intent(textActivity.this, voiceActivity.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}