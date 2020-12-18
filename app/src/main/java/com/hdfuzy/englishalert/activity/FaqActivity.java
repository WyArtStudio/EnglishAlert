package com.hdfuzy.englishalert.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdfuzy.englishalert.R;

public class FaqActivity extends AppCompatActivity {
    TextView flaticon, manyPixels, lottieFiles, googleFonts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        flaticon = findViewById(R.id.flaticon);
        flaticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flat = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.flaticon.com/"));
                startActivity(flat);
            }
        });

        manyPixels = findViewById(R.id.manypixels);
        manyPixels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mPixels = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.manypixels.co/gallery"));
                startActivity(mPixels);
            }
        });

        lottieFiles = findViewById(R.id.lottiefiles);
        lottieFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent lFiles = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://lottiefiles.com/"));
                startActivity(lFiles);
            }
        });

        googleFonts = findViewById(R.id.googlefonts);
        googleFonts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gFonts = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://fonts.google.com/specimen/Montserrat?selection.family=Montserrat"));
                startActivity(gFonts);
            }
        });

        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaqActivity.super.onBackPressed();
            }
        });
    }
}