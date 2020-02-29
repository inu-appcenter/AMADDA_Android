package com.inu.amadda.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.inu.amadda.R;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.util.PreferenceManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Handler handler = new Handler();
        if (checkFirstLogin()) {
            handler.postDelayed(this::goToLogin, 2000);
        } else {
            handler.postDelayed(this::goToMain, 2000);
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean checkFirstLogin() {
        String id = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.ID, null);
        String pw = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.PASSWORD, null);

        return id == null || pw == null;
    }

}
