package com.hdfuzy.englishalert.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hdfuzy.englishalert.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AboutUsActivity extends AppCompatActivity {
    CircleImageView analyst, designer, developer, tester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ImageView backButton = findViewById(R.id.back_button);
        analyst = findViewById(R.id.analyst);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Ffotoanalyst.jpeg?alt=media&token=9c90aa23-d733-40f0-bfe4-750a257fbcfa")
                .error(R.drawable.profile_picture)
                .into(analyst);

        designer = findViewById(R.id.designer);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Ffotodesigner.jpeg?alt=media&token=4f23235b-bb57-4d28-94fe-7c4ee22cf548")
                .error(R.drawable.profile_picture)
                .into(designer);

        developer = findViewById(R.id.developer);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Ffotodev.jpg?alt=media&token=f5cf85ee-d7ca-4853-bf53-5b1ed2c9706d")
                .error(R.drawable.profile_picture)
                .into(developer);

        tester = findViewById(R.id.tester);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Ffototester.jpeg?alt=media&token=1aa09997-759f-474a-a15f-cfc0c1a544ad")
                .error(R.drawable.profile_picture)
                .into(tester);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutUsActivity.super.onBackPressed();
            }
        });
    }
}