package com.hdfuzy.englishalert.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hdfuzy.englishalert.R;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText oldPasswordEdt, newPasswordEdt, confirmPasswordEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ImageView backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordActivity.super.onBackPressed();
            }
        });

//        FrameLayout changePasswordButton = findViewById(R.id.change_password_button);
//        changePasswordButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String oldPassword = oldPasswordEdt.getText().toString();
//                String newPassword = newPasswordEdt.getText().toString();
//                String confirmPassword = confirmPasswordEdt.getText().toString();
//
//                if (oldPassword.isEmpty()) {
//                    oldPasswordEdt.setError("Isi Password Lama Anda");
//                } else if (newPassword.isEmpty()) {
//                    newPasswordEdt.setError("Isi Password Baru Anda");
//                } else if (!confirmPassword.matches(newPassword)) {
//                    confirmPasswordEdt.setError("Konfirmasikan Ulang Password Baru Anda");
//                } else {
//                    //
//                }
//            }
//        });
    }
}