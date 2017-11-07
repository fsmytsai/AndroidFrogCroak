package com.tsaiweb.frogcroak;

import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends MySharedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sp_Settings = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean IsNeverIntro = sp_Settings.getBoolean("IsNeverIntro", false);
        if (IsNeverIntro) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MainFrameLayout, new HomeFragment(), "HomeFragment")
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.MainFrameLayout, new IntroductionFragment(), "IntroductionFragment")
                    .commit();
        }
    }
}
