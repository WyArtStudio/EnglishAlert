package com.hdfuzy.englishalert.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hdfuzy.englishalert.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView register, login;
    EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(ForgotPasswordActivity.this, RegisterActivity.class);
                startActivity(register);
                finish();
            }
        });

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForgotPasswordActivity.super.onBackPressed();
            }
        });

//        FrameLayout sendButton = findViewById(R.id.send_button);
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailEditText.getText().toString();
//                if (email.isEmpty()) {
//                    emailEditText.setError("Masukkan Email Anda");
//                } else {
//                    //
//                }
//            }
//        });
    }

    public void closeKeyboard(View view) {
        ((InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), DEFAULT_KEYS_DISABLE);
    }
}