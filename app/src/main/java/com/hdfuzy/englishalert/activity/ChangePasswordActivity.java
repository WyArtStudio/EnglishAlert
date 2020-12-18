package com.hdfuzy.englishalert.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hdfuzy.englishalert.R;
import com.maksim88.passwordedittext.PasswordEditText;

public class ChangePasswordActivity extends AppCompatActivity {
    PasswordEditText newPasswordEdt, confirmPasswordEdt;
    private static final String TAG = "ChangeProfileActivity";
    private static final String USER = "users";
    DatabaseReference reff;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ImageView backButton = findViewById(R.id.back_button);
        newPasswordEdt = findViewById(R.id.new_password);
        confirmPasswordEdt = findViewById(R.id.confirm_password);

        ProgressDialog progressDialog = new ProgressDialog(this);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordActivity.super.onBackPressed();
            }
        });

        reff = FirebaseDatabase.getInstance().getReference().child(USER).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        FrameLayout changePasswordButton = findViewById(R.id.change_password_button);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                String newPassword = newPasswordEdt.getText().toString();
                String confirmPassword = confirmPasswordEdt.getText().toString();

                if (newPassword.isEmpty()) {
                    newPasswordEdt.setError("Isi Password Baru Anda");
                    progressDialog.dismiss();
                } else if (newPassword.length() < 6) {
                    newPasswordEdt.setError("Minimal 6 karakter");
                    progressDialog.dismiss();
                }
                else if (!confirmPassword.matches(newPassword)) {
                    confirmPasswordEdt.setError("Konfirmasikan Ulang Password Baru Anda");
                    progressDialog.dismiss();
                } else {
                    ProgressDialog dialog = new ProgressDialog(ChangePasswordActivity.this);
                    dialog.setMessage("Please wait...");
                    dialog.setCancelable(false);
                    dialog.show();
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(newPassword)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated");
                                    }
                                }
                            });
                    reff.child("password").setValue(newPassword);
                    updateUI();
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

    private void updateUI() {
        showMessage("Password Anda berhasil diubah!");
        super.onBackPressed();
    }

}