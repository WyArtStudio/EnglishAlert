package com.hdfuzy.englishalert.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hdfuzy.englishalert.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    FrameLayout changeProfile, changePassword, criticsSuggestion, faq, aboutUs, help;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        changeProfile = findViewById(R.id.change_profile);
        changePassword = findViewById(R.id.change_password);
        criticsSuggestion = findViewById(R.id.critics_and_suggestion);
        faq = findViewById(R.id.faq);
        aboutUs = findViewById(R.id.about_us);
        help = findViewById(R.id.help);

        changeProfile.setOnClickListener(this);
        changePassword.setOnClickListener(this);
        criticsSuggestion.setOnClickListener(this);
        faq.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        help.setOnClickListener(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent home = new Intent(SettingsActivity.this, HomeActivity.class);
                        startActivity(home);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_discussion:
                        Intent discuss = new Intent(SettingsActivity.this, ArticleActivity.class);
                        startActivity(discuss);
                        overridePendingTransition(0, 0);
                        finish();
                        break;
                    case R.id.navigation_profile:
                        Intent profile = new Intent(SettingsActivity.this, ProfileActivity.class);
                        startActivity(profile);
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
                SettingsActivity.this.finish();
                System.exit(0);
            }
        }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_profile:
                Intent profile = new Intent(SettingsActivity.this, ChangeProfileActivity.class);
                startActivity(profile);
                break;
            case R.id.change_password:
                Intent password = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                startActivity(password);
                break;
            case R.id.critics_and_suggestion:
                Intent critics = new Intent(SettingsActivity.this, CriticsActivity.class);
                startActivity(critics);
                break;
            case R.id.faq:
                Intent faq = new Intent(SettingsActivity.this, FaqActivity.class);
                startActivity(faq);
                break;
            case R.id.about_us:
                Intent aboutUs = new Intent(SettingsActivity.this, AboutUsActivity.class);
                startActivity(aboutUs);
                break;
            case R.id.help:
                Intent group = new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://wa.me/6281413543830?text=Halo+admin%2C+saya+membutuhkan+sedikit+bantuan."));
                startActivity(group);
                break;
        }
    }
}