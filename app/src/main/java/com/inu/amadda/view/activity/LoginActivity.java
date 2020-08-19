package com.inu.amadda.view.activity;

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
import com.inu.amadda.database.AppDatabase;
import com.inu.amadda.database.ShareGroup;
import com.inu.amadda.model.GroupData;
import com.inu.amadda.model.GroupResponse;
import com.inu.amadda.model.SuccessResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.util.PreferenceManager;
import com.inu.amadda.util.Utils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private AppDatabase appDatabase;

    private EditText et_id, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        appDatabase = AppDatabase.getInstance(this);

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
                break;
            }
            case R.id.btn_join: {
                //TODO 일시 처리
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            }
        }
    };

    private void requestLogin(String id, String pw){
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id);
        map.put("passwd", pw);

        RetrofitInstance.getInstance().getService().Login(map).enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    SuccessResponse successResponse = response.body();
                    if (successResponse != null) {
                        if (successResponse.success) {
                            saveUserInfo(id, pw, response.headers().get("token"));
                            getGroups(response.headers().get("token"));
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("LoginActivity", successResponse.message);
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
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("LoginActivity", t.getMessage());
            }
        });
    }

    private void getGroups(String token) {
        RetrofitInstance.getInstance().getService().GetGroups(token).enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    GroupResponse groupResponse = response.body();
                    if (groupResponse != null) {
                        if (groupResponse.success) {
                            setLocalDatabase(groupResponse.groups);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "공유 그룹을 가져올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("LoginActivity", groupResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "공유 그룹을 가져올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "공유 그룹을 가져올 수 없습니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("LoginActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("LoginActivity", t.getMessage());
            }
        });

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setLocalDatabase(List<GroupData> groups) {
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.COLOR, Utils.getRandomColor(this));

        new Thread(() -> {
            for(int i = 0; i < groups.size(); i++){
                appDatabase.groupDao().insert(new ShareGroup(groups.get(i).getShare(), groups.get(i).getGroup_name(),
                        groups.get(i).getMemo(), Utils.getRandomColor(this)));
                Log.d("LoginActivity", "Save group: " + groups.get(i).getShare());
            }
        }).start();
    }

    private void saveUserInfo(String id, String pw, String token){
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.ID, id);
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.PASSWORD, pw);
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, token);
        Log.d("LoginActivity", "ID: " + id + ", PASSWORD: " + pw + ", TOKEN: " + token);
    }
}
