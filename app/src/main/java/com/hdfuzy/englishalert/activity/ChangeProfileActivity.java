package com.hdfuzy.englishalert.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hdfuzy.englishalert.R;
import com.hdfuzy.englishalert.model.User;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChangeProfileActivity extends AppCompatActivity {
    private static final String TAG = "ChangeProfileActivity";
    private static final String USER = "users";
    CircleImageView userPhoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    EditText changeEmailEdt, changeNameEdt;
    private FirebaseDatabase database;
    Uri pickedImgUri;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);
        ImageView backButton = findViewById(R.id.back_button);
        changeEmailEdt = findViewById(R.id.change_email);
        changeNameEdt = findViewById(R.id.change_username);
        ConstraintLayout changePicture = findViewById(R.id.change_picture);
        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });

        ProgressDialog progressDialog = new ProgressDialog(this);

        userPhoto = findViewById(R.id.user_profile);
        DatabaseReference reff = FirebaseDatabase.getInstance().getReference().child(USER).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reff.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = Objects.requireNonNull(dataSnapshot.child("userName").getValue()).toString();
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://englishalert-839f3.appspot.com/users_photos").child(dataSnapshot.child("imgUrl").getValue().toString());
                try {
                    final File file = File.createTempFile("image", "jpg");
                    storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            if (ChangeProfileActivity.this.isDestroyed()) {
                                userPhoto.setBackgroundResource(R.drawable.profile_picture);
                            } else {
                                Glide.with(ChangeProfileActivity.this)
                                        .load(BitmapFactory.decodeFile(file.getAbsolutePath()))
                                        .error(R.drawable.profile_picture)
                                        .into(userPhoto);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChangeProfileActivity.this, "Failed to Load Image", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeProfileActivity.super.onBackPressed();
            }
        });

        FrameLayout changeProfileButton = findViewById(R.id.change_profile_button);
        changeProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Please wait...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                String newEmail = changeEmailEdt.getText().toString();
                String newName = changeNameEdt.getText().toString();

                if (newName.isEmpty()) {
                    changeNameEdt.setError("Isi Nama Anda");
                    progressDialog.dismiss();
                } else if (newEmail.isEmpty()) {
                    changeEmailEdt.setError("Isi Email Anda");
                    progressDialog.dismiss();
                } else {
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String imgUrl = snapshot.child("imgUrl").getValue().toString();
                            user = FirebaseAuth.getInstance().getCurrentUser();
                            user.updateEmail(newEmail)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "User email address updated");
                                            }
                                        }
                                    });

                            String newImgUrl;
                            if (pickedImgUri == null) {
                                newImgUrl = imgUrl;
                            } else {
                                newImgUrl = pickedImgUri.getLastPathSegment();
                                updateUserInfo(pickedImgUri, user);
                            }
                            reff.child("imgUrl").setValue(newImgUrl);
                            reff.child("email").setValue(newEmail);
                            reff.child("userName").setValue(newName);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    updateUI();
                }
            }
        });

    }

    private void updateUI() {
        showMessage("Profil Anda berhasil diubah!");
        super.onBackPressed();
    }

    private void checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(ChangeProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ChangeProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(ChangeProfileActivity.this, "Mohon setujui untuk keperluan izin", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(ChangeProfileActivity.this, new String[]
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
            userPhoto.setImageURI(pickedImgUri);
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
                                        }
                                    }
                                });
                    }
                });
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