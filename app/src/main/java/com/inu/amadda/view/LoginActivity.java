package com.inu.amadda.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.inu.amadda.R;

public class LoginActivity extends AppCompatActivity {

    private EditText et_login, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();
    }

    private void initialize() {
        et_login = findViewById(R.id.et_id);
        et_password = findViewById(R.id.et_password);
        ImageButton btn_login = findViewById(R.id.btn_login);
        ImageButton btn_join = findViewById(R.id.btn_join);

        btn_login.setOnClickListener(onClickListener);
        btn_join.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_login: {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                case R.id.btn_join: {

                }
            }
        }
    };
}
