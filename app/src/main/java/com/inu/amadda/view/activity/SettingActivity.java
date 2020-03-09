package com.inu.amadda.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.inu.amadda.R;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        setToolbar();
        initialize();

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppCompatImageButton left_btn = toolbar.findViewById(R.id.toolbar_left_btn);
        left_btn.setImageResource(R.drawable.cancel);
        left_btn.setOnClickListener(onClickListener);

        AppCompatImageButton right_btn_image = toolbar.findViewById(R.id.toolbar_right_image);
        right_btn_image.setVisibility(View.GONE);

        TextView right_btn_text = toolbar.findViewById(R.id.toolbar_right_text);
        right_btn_text.setVisibility(View.GONE);

        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText("설정");
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
            case R.id.toolbar_left_btn:{
                onBackPressed();
                break;
            }
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
