package com.inu.amadda.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.inu.amadda.R;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.LoginResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.PreferenceManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    private String id, pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Handler handler = new Handler();
        if (checkFirstLogin()) {
            handler.postDelayed(this::goToLogin, 1600);
        } else {
            requestLogin(id, pw);
        }
    }

    private boolean checkFirstLogin() {
        id = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.ID, null);
        pw = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.PASSWORD, null);
        String token = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null);

        return id == null || pw == null || token == null;
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void requestLogin(String id, String pw){
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("passwd", pw);

        RetrofitInstance.getInstance().getService().Login(map).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {
                        if (loginResponse.success) {
                            saveUserInfo(id, pw, response.headers().get("token"));
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            goToLogin();
                            Log.d("SplashActivity", loginResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                        goToLogin();
                    }
                }
                else if (status == 400){
                    Toast.makeText(getApplicationContext(), "아이디 및 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    goToLogin();
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    goToLogin();
                    Log.d("SplashActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                goToLogin();
                Log.d("SplashActivity", t.getMessage());
            }
        });
    }

    private void saveUserInfo(String id, String pw, String token){
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.ID, id);
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.PASSWORD, pw);
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, token);
        Log.d("SplashActivity", "ID: " + id + ", PASSWORD: " + pw + ", TOKEN: " + token);
    }
}
