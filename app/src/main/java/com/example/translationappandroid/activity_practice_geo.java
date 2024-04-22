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

public class activity_practice_geo extends AppCompatActivity implements View.OnClickListener{
    TextView questionTextView;
    TextView totalQuestionTextView;
    Button ansA,ansB,ansC,ansD;
    Button btn_submit;

    private ImageView exitBtn;
    Animation scaleUp, scaleDown;

    int score=0;
    int totalQuestion = question_geo.questions.length;
    int currentQuestionIndex =0;
    String selectedAnswer="";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_geo);

        totalQuestionTextView = findViewById(R.id.total_questions);
        questionTextView = findViewById(R.id.question);
        ansA = findViewById(R.id.ans_a);
        ansB = findViewById(R.id.ans_b);
        ansC = findViewById(R.id.ans_c);
        ansD = findViewById(R.id.ans_d);

        btn_submit = findViewById(R.id.submit_btn);

        ansA.setOnClickListener(this);
        ansB.setOnClickListener(this);
        ansC.setOnClickListener(this);
        ansD.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        loadNewQuestion();

        exitBtn = findViewById(R.id.exit_quiz);
        //Button Animation
        scaleUp = AnimationUtils.loadAnimation(this,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this,R.anim.scale_down);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitBtn.startAnimation(scaleUp);
                exitBtn.startAnimation(scaleDown);
                Intent intent = new Intent(activity_practice_geo.this, practiceActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadNewQuestion(){
        totalQuestionTextView.setText("Question: " + (currentQuestionIndex+1) + "/" + totalQuestion);
        if(currentQuestionIndex==totalQuestion){
            finishQuiz();
            return;
        }
        questionTextView.setText(question_geo.questions[currentQuestionIndex]);

        ansA.setText(question_geo.choices[currentQuestionIndex][0]);
        ansB.setText(question_geo.choices[currentQuestionIndex][1]);
        ansC.setText(question_geo.choices[currentQuestionIndex][2]);
        ansD.setText(question_geo.choices[currentQuestionIndex][3]);

        selectedAnswer="";
    }

    private void finishQuiz(){
        String passStatus;
        if(score >= totalQuestion*0.6){
            passStatus = "Passes";
        } else {
            passStatus = "Failed";
        }
        new AlertDialog.Builder(this)
                .setTitle(passStatus)
                .setMessage("Your Score: "+score+" Out of "+totalQuestion+" Questions")
                .setPositiveButton("Try Again?", ((dialog, i) -> restartQuiz()))
                .setCancelable(false)
                .show();
    }

    private void restartQuiz(){
        score = 0;
        currentQuestionIndex = 0;
        totalQuestionTextView.setText("Question: " + (currentQuestionIndex+1) + "/" + totalQuestion);
        loadNewQuestion();
    }

    @Override
    public void onClick(View view){
        ansA.setBackgroundColor(Color.WHITE);
        ansB.setBackgroundColor(Color.WHITE);
        ansC.setBackgroundColor(Color.WHITE);
        ansD.setBackgroundColor(Color.WHITE);

        Button clickedButton = (Button) view;

        if(clickedButton.getId() == R.id.submit_btn) {
            if (!selectedAnswer.isEmpty()) {
                if (selectedAnswer.equals(question_geo.correctAnswers[currentQuestionIndex])) {
                    score++;
                } else {
                    clickedButton.setBackgroundColor(Color.GREEN);
                }
                currentQuestionIndex++;
                loadNewQuestion();
            } else {

            }
        } else {
            selectedAnswer=clickedButton.getText().toString();
            clickedButton.setBackgroundColor(Color.BLUE);
        }
    }
}