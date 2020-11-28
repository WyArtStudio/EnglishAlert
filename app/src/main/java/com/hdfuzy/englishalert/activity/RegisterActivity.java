package com.hdfuzy.englishalert.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hdfuzy.englishalert.R;
import com.hdfuzy.englishalert.model.User;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {
    CircleImageView registerPhoto;
    FrameLayout registerButton;
    EditText registerName, registerEmail, registerPassword;
    TextView login;
    Uri pickedImgUri;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    private static final String TAG = "RegisterActivity";
    private static final String USER = "users";
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ProgressDialog progressDialog = new ProgressDialog(this);

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        registerPhoto = findViewById(R.id.register_photo);
        registerPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference(USER);
        registerName = findViewById(R.id.register_username);
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                String userName = registerName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                boolean connected = false;
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    connected = true;
                } else {
                    connected = false;
                }

                if (userName.isEmpty()) {
                    registerName.setError("Isi E-Mail Anda");
                    progressDialog.dismiss();
                } else if (email.isEmpty()) {
                    registerEmail.setError("Isi nama Anda");
                    progressDialog.dismiss();
                } else if (password.isEmpty()) {
                    registerPassword.setError("Buat password Anda");
                    progressDialog.dismiss();
                } else if (pickedImgUri == null) {
                    showMessage("Upload foto profil Anda");
                    progressDialog.dismiss();
                } else if (connected) {
                    if (email.matches(emailPattern)) {
                        String imgUrl = pickedImgUri.getLastPathSegment();
                        user = new User(email, password, userName, imgUrl);
                        registerUser(email, password);
                        progressDialog.dismiss();
                    } else {
                        registerEmail.setError("Isi email Anda dengan benar");
                    }
                    progressDialog.dismiss();
                } else {
                    showMessage("Error, check your network connection.");
                    progressDialog.dismiss();
                }
            }
        });
    }

    public void registerUser(String email, String password) {
        ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("Please wait...");
        loading.setCancelable(false);
        loading.show();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Akun Anda berhasil dibuat!");
                            updateUserInfo(pickedImgUri, mAuth.getCurrentUser());
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user, mAuth);
                            loading.dismiss();
                        } else {
                            Log.w(TAG, "Akun Anda gagal dibuat!\n" + task.getException());
                            showMessage("Pendaftaran Gagal! Alamat E-Mail yang Anda tautkan mungkin sudah digunakan");
                            loading.dismiss();
                        }
                    }
                });
    }

    public void updateUI(FirebaseUser currentUser, FirebaseAuth firebaseAuth) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String keyId = firebaseUser.getUid();
        mDatabase.child(keyId).setValue(user);
        Intent dashboard = new Intent(this, HomeActivity.class);
        startActivity(dashboard);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegisterActivity.this, "Mohon setujui untuk keperluan izin", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]
                                {Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {
            pickedImgUri = data.getData();
            registerPhoto.setImageURI(pickedImgUri);
        }
    }

    private void updateUserInfo(Uri pickedImgUri, final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            showMessage("Pendaftaran selesai!");
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    public void closeKeyboard(View view) {
        ((InputMethodManager) getApplicationContext().getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), DEFAULT_KEYS_DISABLE);
    }

    public void onBackPressed() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}