package com.hdfuzy.englishalert.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.hdfuzy.englishalert.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    TextView register, login;
    EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        register = findViewById(R.id.register);
        emailEditText = findViewById(R.id.send_email);
        TextView callAdmin = findViewById(R.id.call_admin);
        callAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent group = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://wa.me/6281413543830?text=Halo+admin%2C+saya+membutuhkan+sedikit+bantuan."));
                startActivity(group);
            }
        });

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

        FrameLayout sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                if (email.isEmpty()) {
                    emailEditText.setError("Masukkan Email Anda");
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    if (auth != null) {
                        auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("ForgotPasswordActivity", "Email sent.");
                                            showMessage("Reset link telah dikirim! Silahkan cek E-Mail Anda.");
                                        }
                                    }
                                });
                    } else {
                        showMessage("Akun tidak ditemukan! Silahkan daftar terlebih dahulu");
                    }
                }
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void closeKeyboard(View view) {
        ((InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), DEFAULT_KEYS_DISABLE);
    }
}