package com.hdfuzy.englishalert.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hdfuzy.englishalert.R;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ProfileActivity";
    private static final String USER = "users";
    DatabaseReference reff;
    TextView userName, userMail, materialLoadStat, quizLoadStat;
    CircleImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ConstraintLayout btnEdit = findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(ProfileActivity.this, ChangeProfileActivity.class);
                startActivity(edit);
            }
        });

        ConstraintLayout btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);

        // Initialize
        userName = findViewById(R.id.username);
        userMail = findViewById(R.id.useremail);
        userPhoto = findViewById(R.id.userphoto);
        materialLoadStat = findViewById(R.id.material_load);
        quizLoadStat = findViewById(R.id.quiz_load);
        reff = FirebaseDatabase.getInstance().getReference().child(USER).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String name = Objects.requireNonNull(snapshot.child("userName").getValue()).toString();
                String mail = snapshot.child("email").getValue().toString();
                Long mLoad = (Long) snapshot.child("materialLoad").getValue();
                Long qLoad = (Long) snapshot.child("quizLoad").getValue();
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl("gs://englishalert-839f3.appspot.com/users_photos").child(snapshot.child("imgUrl").getValue().toString());
                try {
                    final File file = File.createTempFile("image", "jpg");
                    storageReference.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            if (ProfileActivity.this.isDestroyed()) {
                                userPhoto.setBackgroundResource(R.drawable.profile_picture);
                            } else {
                                Glide.with(ProfileActivity.this)
                                        .load(BitmapFactory.decodeFile(file.getAbsolutePath()))
                                        .error(R.drawable.profile_picture)
                                        .into(userPhoto);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                userName.setText(name);
                userMail.setText(mail);
                materialLoadStat.setText("" + mLoad);
                quizLoadStat.setText("" + qLoad);
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent home = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(home);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_discussion:
                        Intent discuss = new Intent(ProfileActivity.this, ArticleActivity.class);
                        startActivity(discuss);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_settings:
                        Intent settings = new Intent(ProfileActivity.this, SettingsActivity.class);
                        startActivity(settings);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Ingin logout akun?").setCancelable(false).setPositiveButton((CharSequence) "Ya", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    FirebaseAuth.getInstance().signOut();
                    Intent logout = new Intent(ProfileActivity.this, LoginActivity.class);
                    startActivity(logout);
                    finish();
                }
            }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            builder.create().show();
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ingin menutup aplikasi ?").setCancelable(false).setPositiveButton((CharSequence) "Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ProfileActivity.this.finish();
                finish();
                System.exit(0);
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
}