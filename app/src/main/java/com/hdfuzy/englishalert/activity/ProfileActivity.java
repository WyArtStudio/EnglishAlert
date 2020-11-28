package com.hdfuzy.englishalert.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.hdfuzy.englishalert.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ProfileActivity";
    private static final String USER = "users";
    private DatabaseReference reff;
    TextView userName, userMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);

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
                        Intent discuss = new Intent(ProfileActivity.this, DiscussionActivity.class);
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