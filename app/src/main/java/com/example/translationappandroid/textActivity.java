package com.example.translationappandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class textActivity extends AppCompatActivity {

    float x1, x2, y1, y2;

    // Members for Text Translation
    private EditText sourceLanguageEt;
    private TextView destinationLanguageTv;
    private Button sourceLanguageChooseBtn;
    private Button destinationLanguageChooseBtn;
    private Button translateBtn;
    private ImageView topImage;

    private TranslatorOptions translatorOptions;
    private Translator translator;
    private ProgressDialog progressDialog;
    private static ArrayList<ModelLanguage> languageArrayList;

    private static final String TAG = "MAIN_TAG";

    // Initial Languages
    private String sourceLanguageCode = "en";
    private String sourceLanguageTitle = "English";
    private String destinationLanguageCode = "de";
    private String destinationLanguageTitle = "German";

    // Animation
    Animation scaleUp, scaleDown;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        setTitle("Text Translation");

        // Get Elements Within activity_Text using ids
        sourceLanguageEt = findViewById(R.id.sourceLanguageEt);
        destinationLanguageTv = findViewById(R.id.destinationLanguageTv);
        sourceLanguageChooseBtn = findViewById(R.id.sourceLanguageChooseBtn);
        destinationLanguageChooseBtn = findViewById(R.id.destinationLanguageChooseBtn);
        translateBtn = findViewById(R.id.translateBtn);
        topImage = findViewById(R.id.imageTop);

        sourceLanguageChooseBtn.setTransformationMethod(null);
        destinationLanguageChooseBtn.setTransformationMethod(null);
        translateBtn.setTransformationMethod(null);

        // Animation
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        // Language Init and Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Getting Your Translation");
        progressDialog.setCanceledOnTouchOutside(false);

        // Load available languages only if it hasn't been loaded yet
        if (languageArrayList == null) {
            loadAvailableLanguages();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_text);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_text:
                    return true;
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), homeActivity.class));
//                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_left);
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

        // Handle SourceLanguageChoose Btn
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
        sourceLanguageText = sourceLanguageEt.getText().toString().trim();

        if (sourceLanguageText.isEmpty()) {
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
                                // Successfully Translated
                                Log.d(TAG, "onSuccess: translatedText: " + translatedText);
                                progressDialog.dismiss();
                                destinationLanguageTv.setText(translatedText);
                            })
                            .addOnFailureListener(e -> {
                                progressDialog.dismiss();
                                Toast.makeText(textActivity.this, "Failed to translate due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(textActivity.this, "Failed to ready model due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void sourceLanguageChoose() {
        PopupMenu popupMenu = new PopupMenu(this, sourceLanguageChooseBtn);

        for (int i = 0; i < languageArrayList.size(); i++) {
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

        for (int i = 0; i < languageArrayList.size(); i++) {
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

    private void loadAvailableLanguages() {
        languageArrayList = new ArrayList<>();
        List<String> languageCodeList = TranslateLanguage.getAllLanguages();

        for (String languageCode : languageCodeList) {
            String languageTitle = new Locale(languageCode).getDisplayLanguage();
            Log.d(TAG, "LoadAvailableLanguages: languageCode: " + languageCode);
            Log.d(TAG, "LoadAvailableLanguages: languageTitle: " + languageTitle);

            ModelLanguage modelLanguage = new ModelLanguage(languageCode, languageTitle);
            languageArrayList.add(modelLanguage);
        }
    }

    // Swipe Window Right
    public boolean onTouchEvent(MotionEvent touchEvent) {
        switch (touchEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = touchEvent.getX();
                y1 = touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = touchEvent.getX();
                y2 = touchEvent.getY();
                if (x1 + 150 < x2) {
                    Intent i = new Intent(textActivity.this, homeActivity.class);
                    startActivity(i);
                } else if (x1 - 150 > x2) {
                    Intent i = new Intent(textActivity.this, voiceActivity.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}
