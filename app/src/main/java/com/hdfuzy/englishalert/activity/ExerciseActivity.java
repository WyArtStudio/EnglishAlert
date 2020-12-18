package com.hdfuzy.englishalert.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.hdfuzy.englishalert.R;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        LinearLayout gotoQuiz = findViewById(R.id.goto_quiz);
        gotoQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goQuiz = new Intent(ExerciseActivity.this, ConfirmQuizActivity.class);
                startActivity(goQuiz);
            }
        });

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseActivity.super.onBackPressed();
            }
        });
    }

    public void openSnackbar(View view) {
        Snackbar snackbar = Snackbar.make(view, "Fitur ini belum tersedia, silahkan tunggu perkembangan aplikasi dari kami", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}
