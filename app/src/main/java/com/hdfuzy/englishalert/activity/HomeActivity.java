package com.hdfuzy.englishalert.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import java.util.Calendar;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String USER = "users";
    private DatabaseReference reff;
    TextView userName;
    ViewFlipper viewFlipper;
    ImageView icMateri, icLatihan, icKamus;
    CircleImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ConstraintLayout gotoSearch = findViewById(R.id.goto_search);
        gotoSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(search);
            }
        });

        userName = findViewById(R.id.username);
        TextView greetingMessage = (TextView) findViewById(R.id.greeting_message);
        int timeOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >= 0 && timeOfDay < 12) {
            greetingMessage.setText("Selamat Pagi,");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            greetingMessage.setText("Selamat Siang,");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            greetingMessage.setText("Selamat Sore,");
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            greetingMessage.setText("Selamat Malam,");
        }

        userPhoto = findViewById(R.id.userphoto);
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
                            if (HomeActivity.this.isDestroyed()) {
                                userPhoto.setBackgroundResource(R.drawable.profile_picture);
                            } else {
                                Glide.with(HomeActivity.this)
                                        .load(BitmapFactory.decodeFile(file.getAbsolutePath()))
                                        .error(R.drawable.profile_picture)
                                        .into(userPhoto);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(HomeActivity.this, "Failed to Load Image", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
                userName.setText(name);
            }

            public void onCancelled(DatabaseError databaseError) {
            }
        });

        icMateri = findViewById(R.id.ic_materi);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Fic_materi.png?alt=media&token=3d00dfed-fd8b-4687-b18f-a656f32d5185")
                .error(R.color.white)
                .into(icMateri);
        icMateri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent materi = new Intent(HomeActivity.this, MateriActivity.class);
                startActivity(materi);
            }
        });

        icLatihan = findViewById(R.id.ic_latihan);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Fic_latihan.png?alt=media&token=ac32d142-b406-40c5-b9a5-41fa19370864")
                .error(R.color.white)
                .into(icLatihan);
        icLatihan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent latihan = new Intent(HomeActivity.this, LatihanActivity.class);
                startActivity(latihan);
            }
        });

        icKamus = findViewById(R.id.ic_kamus);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Fic_translate.png?alt=media&token=8d3423d1-6729-466c-9353-9ee4dc7bf607")
                .error(R.color.white)
                .into(icKamus);
        icKamus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kamus = new Intent(HomeActivity.this, TranslateActivity.class);
                startActivity(kamus);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_discussion:
                        Intent discuss = new Intent(HomeActivity.this, DiscussionActivity.class);
                        startActivity(discuss);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_profile:
                        Intent profile = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(profile);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_settings:
                        Intent settings = new Intent(HomeActivity.this, SettingsActivity.class);
                        startActivity(settings);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }
                return false;
            }
        });

        viewFlipper = findViewById(R.id.view_flipper);
        flipperImages(0);
    }

    private void flipperImages(int i) {
        String imgUrl = null;
        for (i = 0; i <= 1; i++) {
            ImageView imageView = new ImageView(this);
            if (i == 0) {
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Fdiscuss_slide.png?alt=media&token=6ac2e6cd-df69-4a99-bdd5-10a2451fcdcb";
            } else {
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Fcritics_slide.png?alt=media&token=31f7eb4e-286f-4e5b-a246-d730b7ced1d4";
            }
            Glide.with(HomeActivity.this)
                    .load(imgUrl)
                    .error(R.color.white)
                    .into(imageView);
            viewFlipper.addView(imageView);
            viewFlipper.setFlipInterval(4000);
            viewFlipper.setAutoStart(true);
            viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
            viewFlipper.setOutAnimation(this, android.R.anim.slide_out_right);
            int finalI = i;
            imageView.setOnClickListener(v -> {
                if (finalI == 0) {
                    Intent group = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://chat.whatsapp.com/Dfk25RUiESwGijQitEzVMV"));
                    startActivity(group);
                } else if (finalI == 1) {
                    Intent instagram = new Intent(HomeActivity.this, CriticsActivity.class);
                    startActivity(instagram);
                }
            });
        }
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ingin menutup aplikasi ?").setCancelable(false).setPositiveButton((CharSequence) "Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                HomeActivity.this.finish();
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