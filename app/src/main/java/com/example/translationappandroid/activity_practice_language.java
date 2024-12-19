package com.example.translationappandroid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class activity_practice_language extends AppCompatActivity implements View.OnClickListener {
    TextView questionTextView;
    TextView totalQuestionTextView;
    Button ansA, ansB, ansC, ansD;
    Button btn_submit;

    private ImageView exitBtn;
    Animation scaleUp, scaleDown;

    int score = 0;
    int totalQuestion = 20;
    int currentQuestionIndex = 0;
    String selectedAnswer = "";

    List<Integer> questionIndices;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_language);

        totalQuestionTextView = findViewById(R.id.total_questions);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_a);
        ansB = findViewById(R.id.ans_b);
        ansC = findViewById(R.id.ans_c);
        ansD = findViewById(R.id.ans_d);

        btn_submit = findViewById(R.id.submit_btn);
        btn_submit.setTransformationMethod(null);
        ansA.setTransformationMethod(null);
        ansB.setTransformationMethod(null);
        ansC.setTransformationMethod(null);
        ansD.setTransformationMethod(null);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        // Initialize and shuffle question indices
        questionIndices = new ArrayList<>();
        for (int i = 0; i < totalQuestion; i++) {
            questionIndices.add(i);
        }
        Collections.shuffle(questionIndices);

        loadNewQuestion();

        exitBtn = findViewById(R.id.exit_quiz);
        scaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitBtn.startAnimation(scaleUp);
                exitBtn.startAnimation(scaleDown);
                Intent intent = new Intent(activity_practice_language.this, practiceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadNewQuestion() {
        totalQuestionTextView.setText("Question: " + (currentQuestionIndex + 1) + "/" + totalQuestion);

        if (currentQuestionIndex == totalQuestion) {
            finishQuiz();
            return;
        }

        // Load questions and answers based on their shuffled indices
        int index = questionIndices.get(currentQuestionIndex);
        questionTextView.setText(question_language.questions[index]);
        ansA.setText(question_language.choices[index][0]);
        ansB.setText(question_language.choices[index][1]);
        ansC.setText(question_language.choices[index][2]);
        ansD.setText(question_language.choices[index][3]);

        selectedAnswer = "";
    }

    private void finishQuiz() {
        String passStatus;
        if (score >= totalQuestion * 0.6) {
            passStatus = "Passes";
        } else {
            passStatus = "Failed";
        }
        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Your Score: " + score + " Out of " + totalQuestion + " Questions")
                .setPositiveButton("Try Again?", ((dialog, i) -> restartQuiz()))
                .setCancelable(false)
                .show();
    }

    private void restartQuiz() {
        score = 0;
        currentQuestionIndex = 0;
        Collections.shuffle(questionIndices); // Shuffle again for a new round
        totalQuestionTextView.setText("Question: " + (currentQuestionIndex + 1) + "/" + totalQuestion);
        loadNewQuestion();
    }

    @Override
    public void onClick(View view) {
        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);

        Button clickedButton = (Button) view;

        if (clickedButton.getId() == R.id.submit_btn) {
            if (!selectedAnswer.isEmpty()) {
                int index = questionIndices.get(currentQuestionIndex);
                if (selectedAnswer.equals(question_language.correctAnswers[index])) {
                    score++;
                } else {
                    clickedButton.setBackgroundColor(Color.GREEN);
                }
                currentQuestionIndex++;
                loadNewQuestion();
            }
        } else {
            selectedAnswer = clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.CYAN);
        }
    }
}