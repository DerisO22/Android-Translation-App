package com.example.translationappandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.translation.Translator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class textActivity extends AppCompatActivity {

    float x1,x2,y1,y2;

    //Members for Text Translation
    private EditText sourceLanguageEt;
    private TextView destinationLanguageTv;
    private Button sourceLanguageChooseBtn;
    private Button destinationLanguageChooseBtn;
    private Button translateBtn;

    private TranslatorOptions translatorOptions;
    private Translator translator;
    private ProgressDialog progressDialog;
    private ArrayList<ModelLanguage> languageArrayList;

    private static final String TAG = "MAIN_TAG";

    //Initial Languages
    private String sourceLanguageCode = "en";
    private String sourceLanguageTitle = "English";
    private String destinationLanguageCode = "de";
    private String destinationLanguageTitle = "German";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        setTitle("Text Translation");

        //Get Elements Within activity_Text using ids
        sourceLanguageEt = findViewById(R.id.sourceLanguageEt);
        destinationLanguageTv = findViewById(R.id.destinationLanguageTv);
        sourceLanguageChooseBtn = findViewById(R.id.sourceLanguageChooseBtn);
        destinationLanguageChooseBtn = findViewById(R.id.destinationLanguageChooseBtn);
        translateBtn = findViewById(R.id.translateBtn);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Your Translation");
        progressDialog.setCanceledOnTouchOutside(false);
        loadAvailableLanguages();


        //Handle SourceLangaugeChoose Btn
        sourceLanguageChooseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sourceLanguageChoose();
            }
        });

        destinationLanguageChooseBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                destinationLanguageChoose();
            }
        });

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }


    public String sourceLanguageText = "";
    private void validateData() {

        sourceLanguageText = sourceLanguageEt.getText().toString().trim();

        if(sourceLanguageText.isEmpty()){
            Toast.makeText(this, "Enter text to translate", Toast.LENGTH_SHORT).show();
        } else {
            startTranslations();
        }
    }

    private void startTranslations() {

        progressDialog.setMessage("Processing Language Model . . .");
        progressDialog.show();

        TranslatorOptions translatorOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(destinationLanguageCode)
                .build();

        final com.google.mlkit.nl.translate.Translator translator = Translation.getClient(translatorOptions);


        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();

        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: model ready, starting translate. . .");

                        progressDialog.show();
                            translator.translate(sourceLanguageText)
                                    .addOnSuccessListener(new OnSuccessListener<String>(){

                                        @Override
                                        public void onSuccess(String translatedText) {
                                            //Successfully Translated
                                            Log.d(TAG, "onSuccess: translatedText: "+translatedText);
                                            progressDialog.dismiss();
                                            destinationLanguageTv.setText(translatedText);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(textActivity.this, "Failed to translate due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        progressDialog.dismiss();
                        Toast.makeText(textActivity.this, "Failed to ready model due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sourceLanguageChoose(){

        PopupMenu popupMenu = new PopupMenu(this, sourceLanguageChooseBtn);

        for(int i=0; i<languageArrayList.size();i++){
            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).languageTitle);
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();

                sourceLanguageCode = languageArrayList.get(position).languageCode;
                sourceLanguageTitle = languageArrayList.get(position).languageTitle;

                sourceLanguageChooseBtn.setText(sourceLanguageTitle);

                Log.d(TAG, "OnMenuItemClick: sourceLanguageCode "+sourceLanguageCode);
                Log.d(TAG, "OnMenuItemClick: sourceLanguageTitle "+sourceLanguageTitle);

                return false;
            }
        });
    }


    private void destinationLanguageChoose(){

        PopupMenu popupMenu = new PopupMenu(this, destinationLanguageChooseBtn);

        for(int i=0;i<languageArrayList.size();i++){
            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).getLanguageTitle());


            popupMenu.show();

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    int position = item.getItemId();

                    destinationLanguageCode = languageArrayList.get(position).languageCode;
                    destinationLanguageTitle = languageArrayList.get(position).languageTitle;

                    destinationLanguageChooseBtn.setText(destinationLanguageTitle);

                    Log.d(TAG, "onMenuItemClick: destinationLanguageCode: "+destinationLanguageCode);
                    Log.d(TAG, "onMenuItemClick: destinationLanguageTitle: "+destinationLanguageTitle);
                    return false;
                }
            });
        }
    }

    private void loadAvailableLanguages() {
        languageArrayList = new ArrayList<>();

        List<String> languageCodeList = TranslateLanguage.getAllLanguages();

        for(String languageCode: languageCodeList){
            String languageTitle = new Locale(languageCode).getDisplayLanguage();
            Log.d(TAG, "LoadAvailableLanguages: languageCode: "+languageCode);
            Log.d(TAG, "LoadAvailableLanguages: languageTitle: "+languageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(languageCode, languageTitle);
            languageArrayList.add(modelLanguage);
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