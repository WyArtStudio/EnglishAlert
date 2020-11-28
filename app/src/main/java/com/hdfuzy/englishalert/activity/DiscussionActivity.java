package com.hdfuzy.englishalert.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hdfuzy.englishalert.R;

public class DiscussionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_discussion);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent home = new Intent(DiscussionActivity.this, HomeActivity.class);
                        startActivity(home);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_profile:
                        Intent profile = new Intent(DiscussionActivity.this, ProfileActivity.class);
                        startActivity(profile);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_settings:
                        Intent settings = new Intent(DiscussionActivity.this, SettingsActivity.class);
                        startActivity(settings);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ingin menutup aplikasi ?").setCancelable(false).setPositiveButton((CharSequence) "Ya", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DiscussionActivity.this.finish();
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