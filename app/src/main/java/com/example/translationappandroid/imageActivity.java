package com.example.translationappandroid;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class imageActivity extends AppCompatActivity {

    float x1, x2, y1, y2;

    // UI Views
    private MaterialButton inputImageBtn;
    private MaterialButton recognizeTextBtn;
    private ShapeableImageView imageTv;
    private EditText recognizeTextEt;

    // Tag
    private static final String TAG = "MAIN_TAG";

    private Uri imageUri = null;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    // Text Recognizer
    private TextRecognizer textRecognizer;

    private ProgressDialog progressDialog;

    // Actual Translation Stuff
    private TextView destinationLanguageTv;
    private Button sourceLanguageChooseBtn;
    private Button destinationLanguageChooseBtn;
    private Button translateBtn;

    private TranslatorOptions translatorOptions;
    private Translator translator;

    // Static variable to hold available languages
    private static ArrayList<ModelLanguage> languageArrayList;

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
        setContentView(R.layout.activity_image);
        setTitle("Image-Text Translation");

        // Init ImageViews By Id
        inputImageBtn = findViewById(R.id.inputImageBtn);
        recognizeTextBtn = findViewById(R.id.recognizeTextBtn);
        imageTv = findViewById(R.id.imageTv);
        recognizeTextEt = findViewById(R.id.recognizedTextEt);

        // Init Arrays of permissions required for camera, gallery
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Init textRecognizer
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        // Actual Translation Stuff
        // Get Elements Within activity_Text using ids
        destinationLanguageTv = findViewById(R.id.destinationLanguageTv);
        sourceLanguageChooseBtn = findViewById(R.id.sourceLanguageChooseBtn);
        destinationLanguageChooseBtn = findViewById(R.id.destinationLanguageChooseBtn);
        translateBtn = findViewById(R.id.translateBtn);

        // Prevent Capitalization
        inputImageBtn.setTransformationMethod(null);
        recognizeTextBtn.setTransformationMethod(null);
        sourceLanguageChooseBtn.setTransformationMethod(null);
        destinationLanguageChooseBtn.setTransformationMethod(null);
        translateBtn.setTransformationMethod(null);

        // Animation
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        // Language Init and Progress
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Successfully Performed. Tap Outside To Close");
        progressDialog.setCanceledOnTouchOutside(true);

        // Load available languages only if they haven't been loaded yet
        if (languageArrayList == null) {
            loadAvailableLanguages();
        }

        inputImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputImageDialog();
            }
        });

        recognizeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri == null) {
                    Toast.makeText(imageActivity.this, "Pick image first...", Toast.LENGTH_SHORT).show();
                } else {
                    recognizeTextFromImage();
                }
            }
        });

        imageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageTv.startAnimation(scaleUp);
                imageTv.startAnimation(scaleDown);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setSelectedItemId(R.id.bottom_image);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_image:
                    return true;
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), homeActivity.class));
//                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_text:
                    startActivity(new Intent(getApplicationContext(), textActivity.class));
//                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_voice:
                    startActivity(new Intent(getApplicationContext(), voiceActivity.class));
//                    overridePendingTransition(R.anim.slide_out_left, R.anim.slide_out_left);
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
        sourceLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceLanguageChoose();
            }
        });

        destinationLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinationLanguageChoose();
            }
        });

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translateBtn.startAnimation(scaleUp);
                translateBtn.startAnimation(scaleDown);
                validateData();
            }
        });
    }

    private void recognizeTextFromImage() {
        // Extract Text From Image
        // Use Exception Handling
        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);

            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            progressDialog.setMessage("Processing Language Model . . .");
                            progressDialog.show();
                            String recognizedText = text.getText();
                            recognizeTextEt.setText(recognizedText);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            Toast.makeText(imageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(this, inputImageBtn);

        popupMenu.getMenu().add(Menu.NONE, 1, 1, "CAMERA");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Gallery");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == 1) {
                    if (checkCameraPermissions()) {
                        pickImageCamera();
                    } else {
                        requestCameraPermissions();
                    }
                } else if (id == 2) {
                    if (checkStoragePermission()) {
                        pickImageGallery();
                    }
                } else {
                    requestStoragePermission();
                }
                return true;
            }
        });
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // Receive Image if Chosen
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Image Chosen
                        Intent data = result.getData();
                        imageUri = data.getData();
                        // Set to ImageView
                        imageTv.setImageURI(imageUri);
                    } else {
                        // Cancelled
                        Toast.makeText(imageActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private void pickImageCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Image Taken From Camera
                        imageTv.setImageURI(imageUri);
                    } else {
                        // Cancelled
                        Toast.makeText(imageActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions() {
        boolean cameraResult = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean storageResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return cameraResult && storageResult;
    }

    private void requestCameraPermissions() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    // Handle Permission Results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickImageCamera();
                    } else {
                        Toast.makeText(this, "Camera & Storage Permissions are required", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
                }
            }
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        pickImageGallery();
                    } else {
                        Toast.makeText(this, "Storage permission is required", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public String sourceLanguageText = "";

    private void validateData() {
        sourceLanguageText = recognizeTextEt.getText().toString().trim();

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
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: model ready, starting translate. . .");

                        progressDialog.show();
                        translator.translate(sourceLanguageText)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translatedText) {
                                        // Successfully Translated
                                        Log.d(TAG, "onSuccess: translatedText: " + translatedText);
                                        progressDialog.dismiss();
                                        destinationLanguageTv.setText(translatedText);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(imageActivity.this, "Failed to translate due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(imageActivity.this, "Failed to load model due to " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sourceLanguageChoose() {
        androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(this, sourceLanguageChooseBtn);

        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).languageTitle);
        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();

                sourceLanguageCode = languageArrayList.get(position).languageCode;
                sourceLanguageTitle = languageArrayList.get(position).languageTitle;

                sourceLanguageChooseBtn.setText(sourceLanguageTitle);

                Log.d(TAG, "OnMenuItemClick: sourceLanguageCode " + sourceLanguageCode);
                Log.d(TAG, "OnMenuItemClick: sourceLanguageTitle " + sourceLanguageTitle);

                return false;
            }
        });
    }

    private void destinationLanguageChoose() {
        androidx.appcompat.widget.PopupMenu popupMenu = new androidx.appcompat.widget.PopupMenu(this, destinationLanguageChooseBtn);

        for (int i = 0; i < languageArrayList.size(); i++) {
            popupMenu.getMenu().add(Menu.NONE, i, i, languageArrayList.get(i).getLanguageTitle());
        }
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();

                destinationLanguageCode = languageArrayList.get(position).languageCode;
                destinationLanguageTitle = languageArrayList.get(position).languageTitle;

                destinationLanguageChooseBtn.setText(destinationLanguageTitle);

                Log.d(TAG, "onMenuItemClick: destinationLanguageCode: " + destinationLanguageCode);
                Log.d(TAG, "onMenuItemClick: destinationLanguageTitle: " + destinationLanguageTitle);
                return false;
            }
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
                    Intent i = new Intent(imageActivity.this, voiceActivity.class);
                    startActivity(i);
                } else if (x1 - 150 > x2) {
                    Intent i = new Intent(imageActivity.this, practiceActivity.class);
                    startActivity(i);
                }
                break;
        }
        return false;
    }
}