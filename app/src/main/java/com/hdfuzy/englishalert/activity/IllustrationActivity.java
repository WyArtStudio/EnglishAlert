package com.hdfuzy.englishalert.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.tabs.TabLayout;
import com.hdfuzy.englishalert.R;
import com.hdfuzy.englishalert.adapter.IntroViewPagerAdapter;
import com.hdfuzy.englishalert.model.ScreenItem;

import java.util.ArrayList;
import java.util.List;

public class IllustrationActivity extends AppCompatActivity {
    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    ConstraintLayout btnNext, btnGetStarted;
    Animation btnAnimation;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illustration);

        // Check Shared Preferences
        if (restorePrefData()) {
            Intent login = new Intent(IllustrationActivity.this, LoginActivity.class);
            startActivity(login);
            finish();
        }

        // Initialize
        btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);
        screenPager = findViewById(R.id.screen_viewpager);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_getstarted);

        // View Pager Item
        List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Belajar Lebih Mudah", "Belajar dimanapun, kapanpun, tanpa batasan.", "https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Fimg1.png?alt=media&token=03b626b0-bdbe-4f9b-9da6-8f37d08056a0"));
        mList.add(new ScreenItem("Latihan Soal", "Latihan soal komperhensif agar lebih mudah memahami materi.", "https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Fimg2.png?alt=media&token=aff59222-6c87-45b7-9de5-87d8f3f88a67"));
        mList.add(new ScreenItem("Capai Targetmu", "Mulai raih mimpimu sekarang!", "https://firebasestorage.googleapis.com/v0/b/englishalert-839f3.appspot.com/o/Glide%2Fimg3.png?alt=media&token=471cce19-2ed8-4107-8067-1b1c236a4c81"));
        introViewPagerAdapter = new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);
        tabIndicator.setupWithViewPager(screenPager);

        // Listener for btnNext
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();

                if (position < mList.size()) {
                    tabIndicator.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                    btnGetStarted.setVisibility(View.GONE);
                    position++;
                    screenPager.setCurrentItem(position);
                } else if (position == mList.size() - 1) {
                    loadLastScreen();
                }

            }
        });

        // Listener for Indicator
        tabIndicator.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1) {
                    loadLastScreen();
                } else if (tab.getPosition() < mList.size()) {
                    tabIndicator.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                    btnGetStarted.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Listener for btnGetStarted
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(IllustrationActivity.this, LoginActivity.class);
                startActivity(login);
                savePrefsData();
                finish();
            }
        });

    }

    private boolean restorePrefData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore = pref.getBoolean("isIntroOpened", false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefsData() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();
    }

    private void loadLastScreen() {
        tabIndicator.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        btnGetStarted.setVisibility(View.VISIBLE);
        btnGetStarted.setAnimation(btnAnimation);
    }

}