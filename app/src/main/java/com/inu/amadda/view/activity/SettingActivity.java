package com.inu.amadda.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.inu.amadda.R;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initialize();

    }

    private void initialize() {
        RelativeLayout rl_profile = findViewById(R.id.rl_profile);
        RelativeLayout rl_logout = findViewById(R.id.rl_logout);
        RelativeLayout rl_push = findViewById(R.id.rl_push);

        rl_profile.setOnClickListener(onClickListener);
        rl_logout.setOnClickListener(onClickListener);
        rl_push.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.rl_profile:{
                Intent intent = new Intent(this, EditProfileActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.rl_logout:{

                break;
            }
            case R.id.rl_push:{
                Intent intent = new Intent(this, PushSettingActivity.class);
                startActivity(intent);
                break;
            }
        }
    };

}
