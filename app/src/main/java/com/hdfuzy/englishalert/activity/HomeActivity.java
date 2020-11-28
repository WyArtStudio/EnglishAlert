package com.hdfuzy.englishalert.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
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

        userName = findViewById(R.id.username);
        TextView greetingMessage = (TextView) findViewById(R.id.greeting_message);
        int timeOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(timeOfDay >= 0 && timeOfDay < 12){
            greetingMessage.setText("Selamat Pagi,");
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            greetingMessage.setText("Selamat Siang,");
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            greetingMessage.setText("Selamat Sore,");
        }else if(timeOfDay >= 21 && timeOfDay < 24){
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
                            Glide.with(HomeActivity.this)
                                    .load(BitmapFactory.decodeFile(file.getAbsolutePath()))
                                    .error(R.drawable.add_photo)
                                    .into(userPhoto);
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
                .load("https://firebasestorage.googleapis.com/v0/b/ismart-official.appspot.com/o/Glide%2Ficon_materi.png?alt=media&token=5aae40b2-9577-483e-8027-feec0d2f29a3")
                .error(R.color.white)
                .into(icMateri);

        icLatihan = findViewById(R.id.ic_latihan);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/ismart-official.appspot.com/o/Glide%2Ficon_latihan.png?alt=media&token=5b0a1fae-726b-44cf-9ae0-4da8da253be6")
                .error(R.color.white)
                .into(icLatihan);

        icKamus = findViewById(R.id.ic_kamus);
        Glide.with(this)
                .load("https://firebasestorage.googleapis.com/v0/b/ismart-official.appspot.com/o/Glide%2Ficon_kamus.png?alt=media&token=78ede07f-fe21-4d20-9677-d8a610833de4")
                .error(R.color.white)
                .into(icKamus);

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

    private void flipperImages(int position) {
        String imgUrl = null;
        for (int i = 0; i <= 1; i++) {
            ImageView imageView = new ImageView(this);
            if (i == 0) {
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/ismart-official.appspot.com/o/Glide%2Fgroup_slider.png?alt=media&token=ec032768-5f6a-47a1-957a-7748870aad4e";
            } else if (i == 1) {
                imgUrl = "https://firebasestorage.googleapis.com/v0/b/ismart-official.appspot.com/o/Glide%2Ffollow_slider.png?alt=media&token=023703a9-e5e3-4251-bb9c-46c38cb09b1e";
            }
            Glide.with(HomeActivity.this)
                    .load(imgUrl)
                    .error(R.color.white)
                    .into(imageView);
            viewFlipper.addView(imageView);
            viewFlipper.setFlipInterval(4000);
            viewFlipper.setAutoStart(true);
            viewFlipper.setInAnimation(this, android.R.anim.slide_in_left);
            viewFlipper.setOutAnimation(this, android.R.anim.slide_in_left);
            imageView.setOnClickListener(v -> {
                if (position == 0) {
                    Intent group = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://chat.whatsapp.com/IjgAtgU5upQJMLNqRMRbKX"));
                    startActivity(group);
                } else if (position == 1) {
                    Intent instagram = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://www.instagram.com/ismartlearning.id/"));
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