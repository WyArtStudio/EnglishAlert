package com.hdfuzy.englishalert.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.hdfuzy.englishalert.R;

public class MaterialActivity extends AppCompatActivity {
    LinearLayout sparklingNewcomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        ImageView backButton = findViewById(R.id.back_button);
        sparklingNewcomer = findViewById(R.id.sparkling_newcomer);
        sparklingNewcomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent level1 = new Intent(MaterialActivity.this, SparklingNewcomerActivity.class);
                startActivity(level1);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialActivity.super.onBackPressed();
            }
        });
    }

    public void openSnackbar(View view) {
        Snackbar snackbar = Snackbar.make(view, "Fitur ini belum tersedia, silahkan tunggu perkembangan aplikasi dari kami", Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}