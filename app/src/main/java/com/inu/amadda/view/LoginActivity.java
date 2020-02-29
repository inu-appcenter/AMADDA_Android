package com.inu.amadda.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.inu.amadda.R;
import com.inu.amadda.model.LoginResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.util.PreferenceManager;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText et_id, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();
    }

    private void initialize() {
        et_id = findViewById(R.id.et_id);
        et_password = findViewById(R.id.et_password);
        ImageButton btn_login = findViewById(R.id.btn_login);
        ImageButton btn_join = findViewById(R.id.btn_join);

        btn_login.setOnClickListener(onClickListener);
        btn_join.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.btn_login: {
                if (!et_id.getText().toString().isEmpty() && !et_password.getText().toString().isEmpty()) {
                    requestLogin(et_id.getText().toString(), et_password.getText().toString());
                }
                else {
                    Toast.makeText(this, "아이디 및 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
            case R.id.btn_join: {
                //TODO 일시 처리
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

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
                        if (loginResponse.success.equals("true")) {
                            saveUserInfo(id, pw, loginResponse.token);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("LoginActivity", loginResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (status == 400){
                    Toast.makeText(getApplicationContext(), "아이디 및 비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("LoginActivity", t.getMessage());
            }
        });
    }

    private void saveUserInfo(String id, String pw, String token){
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.ID, id);
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.PASSWORD, pw);
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, token);
        Log.d("LoginActivity", "ID: " + id + ", PASSWORD: " + pw + ", TOKEN: " + token);
    }
}
