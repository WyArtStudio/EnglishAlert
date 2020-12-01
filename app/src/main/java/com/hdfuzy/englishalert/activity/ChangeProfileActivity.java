package com.hdfuzy.englishalert.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hdfuzy.englishalert.R;

public class ChangeProfileActivity extends AppCompatActivity {
    EditText changeEmailEdt, changeNameEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeProfileActivity.super.onBackPressed();
            }
        });

//        FrameLayout changeProfileButton = findViewById(R.id.change_profile_button);
//        changeProfileButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String newEmail = changeEmailEdt.getText().toString();
//                String newName = changeNameEdt.getText().toString();
//
//                if (newName.isEmpty()) {
//                    changeNameEdt.setError("Isi Nama Anda");
//                } else if (newEmail.isEmpty()) {
//                    changeEmailEdt.setError("Isi Email Anda");
//                } else {
//                    //
//                }
//            }
//        });

    }
}