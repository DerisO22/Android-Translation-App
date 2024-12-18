package com.example.translationappandroid;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.translation.Translator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class voiceActivity extends AppCompatActivity {

    float x1,x2,y1,y2;
    TextView detectedText;
    Button getVoice;

    //Translate Vars For Voice recognition
    private TextView destinationLanguageTv;
    private Button sourceLanguageChooseBtn;
    private Button destinationLanguageChooseBtn;
    private Button translateBtn;
    private TranslatorOptions translatorOptions;
    private Translator translator;
    private ProgressDialog progressDialog;
    private static ArrayList<ModelLanguage> languageArrayList; // Static variable to hold languages
    private ImageView topImage;

    private static final String TAG = "MAIN_TAG";

    //Initial Languages
    private String sourceLanguageCode = "en";
    private String sourceLanguageTitle = "English";
    private String destinationLanguageCode = "de";
    private String destinationLanguageTitle = "German";

    Animation scaleUp, scaleDown;

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

        //Voice translate
        destinationLanguageTv = findViewById(R.id.destinationLanguageTv);
        sourceLanguageChooseBtn = findViewById(R.id.sourceLanguageChooseBtn);
        destinationLanguageChooseBtn = findViewById(R.id.destinationLanguageChooseBtn);
        translateBtn = findViewById(R.id.translateBtn);
        topImage = findViewById(R.id.topImage);

        // Initialize languageArrayList if it is null
        if (languageArrayList == null) {
            loadAvailableLanguages();
        }

        getVoice.setTransformationMethod(null);
        sourceLanguageChooseBtn.setTransformationMethod(null);
        destinationLanguageChooseBtn.setTransformationMethod(null);
        translateBtn.setTransformationMethod(null);

        //Language Init and Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Your Translation");
        progressDialog.setCanceledOnTouchOutside(false);

        //Animation
        scaleUp = AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.scale_down);

        //Bottom Menu
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

        getVoice.setOnClickListener(v -> speak());

        //Voice Translation
        sourceLanguageChooseBtn.setOnClickListener(v -> sourceLanguageChoose());

        destinationLanguageChooseBtn.setOnClickListener(v -> destinationLanguageChoose());

        translateBtn.setOnClickListener(v -> {
            translateBtn.startAnimation(scaleUp);
            translateBtn.startAnimation(scaleDown);
            validateData();
        });

        topImage.setOnClickListener(v -> {
            topImage.startAnimation(scaleUp);
            topImage.startAnimation(scaleDown);
        });
    }

    public String sourceLanguageText = "";
    private void validateData() {
        sourceLanguageText = detectedText.getText().toString().trim();

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
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "onSuccess: model ready, starting translate. . .");

                    progressDialog.show();
                    translator.translate(sourceLanguageText)
                            .addOnSuccessListener(translatedText -> {
                                //Successfully Translated
                                Log.d(TAG, "onSuccess: translatedText: " + translatedText);
                                progressDialog.dismiss();
                                destinationLanguageTv.setText(translatedText);
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(voiceActivity.this, "Failed to translate due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(voiceActivity.this, "Failed to ready model due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void sourceLanguageChoose() {
        PopupMenu popupMenu = new PopupMenu(this, sourceLanguageChooseBtn);

        for(int i = 0; i < languageArrayList.size(); i++){
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).languageTitle);
        }

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            int position = item.getItemId();

            sourceLanguageCode = languageArrayList.get(position).languageCode;
            sourceLanguageTitle = languageArrayList.get(position).languageTitle;

            sourceLanguageChooseBtn.setText(sourceLanguageTitle);

            Log.d(TAG, "OnMenuItemClick: sourceLanguageCode " + sourceLanguageCode);
            Log.d(TAG, "OnMenuItemClick: sourceLanguageTitle " + sourceLanguageTitle);

            return false;
        });
    }

    private void destinationLanguageChoose() {
        PopupMenu popupMenu = new PopupMenu(this, destinationLanguageChooseBtn);

        for(int i = 0; i < languageArrayList.size(); i++){
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLanguageTitle());
        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(item -> {
            int position = item.getItemId();
            destinationLanguageCode = languageArrayList.get(position).languageCode;
            destinationLanguageTitle = languageArrayList.get(position).languageTitle;
            destinationLanguageChooseBtn.setText(destinationLanguageTitle);
            Log.d(TAG, "onMenuItemClick: destinationLanguageCode: " + destinationLanguageCode);
            Log.d(TAG, "onMenuItemClick: destinationLanguageTitle: " + destinationLanguageTitle);
            return false;
        });
    }

    //Load Popupmenus with all available languages
    private void loadAvailableLanguages() {
        languageArrayList = new ArrayList<>();
        List<String> languageCodeList = TranslateLanguage.getAllLanguages();

        for(String languageCode: languageCodeList){
            String languageTitle = new Locale(languageCode).getDisplayLanguage();
            Log.d(TAG, "LoadAvailableLanguages: languageCode: " + languageCode);
            Log.d(TAG, "LoadAvailableLanguages: languageTitle: " + languageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(languageCode, languageTitle);
            languageArrayList.add(modelLanguage);
        }
    }

    //Voice Recognition
    public void speak(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            detectedText.setText(Objects.requireNonNull(Objects.requireNonNull(data).getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)).get(0));
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
                if(x1 + 150 < x2){
                    Intent i = new Intent(voiceActivity.this, textActivity.class);
                    startActivity(i);
                } else if(x1 - 150 > x2){
                    Intent i = new Intent(voiceActivity.this, imageActivity.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}