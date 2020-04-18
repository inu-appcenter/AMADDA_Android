package com.inu.amadda.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.inu.amadda.R;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.AddGroupModel;
import com.inu.amadda.model.SuccessResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.PreferenceManager;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddShareGroupActivity extends AppCompatActivity {

    private boolean isExpanded = false, isFirst = true;
    private List<String> list = new ArrayList<>();

    private EditText et_group_name, et_invite, et_memo;
    private TextView tv_invite, tv_group_color;
    private ImageView iv_invite;
    private ExpandableLayout expandable_invite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_group);

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
        title.setText("공유 그룹 생성");
    }

    private void initialize() {
        et_group_name = findViewById(R.id.et_group_name);

        RelativeLayout rl_invite = findViewById(R.id.rl_invite);
        rl_invite.setOnClickListener(onClickListener);
        tv_invite = findViewById(R.id.tv_invite);
        iv_invite = findViewById(R.id.iv_invite);

        expandable_invite = findViewById(R.id.expandable_invite);
        et_invite = findViewById(R.id.et_invite);
        ImageButton btn_invite = findViewById(R.id.btn_invite);
        btn_invite.setOnClickListener(onClickListener);

        RelativeLayout rl_group_color = findViewById(R.id.rl_group_color);
        rl_group_color.setOnClickListener(onClickListener);
        tv_group_color = findViewById(R.id.tv_group_color);

        et_memo = findViewById(R.id.et_memo);

        TextView btn_make = findViewById(R.id.btn_make);
        btn_make.setOnClickListener(onClickListener);
    }

    private void setExpandable() {
        if (isExpanded) {
            expandable_invite.collapse();
            iv_invite.setImageResource(R.drawable.down);
            isExpanded = false;
        }
        else {
            if (isFirst){
                tv_invite.setText(PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.ID, null));
                tv_invite.setTextColor(ContextCompat.getColor(this, R.color.color_707070));
                isFirst = false;
            }
            expandable_invite.expand();
            iv_invite.setImageResource(R.drawable.up);
            isExpanded = true;
        }
    }

    private void sendGroupInfo() {
        AddGroupModel addGroupModel = new AddGroupModel();
        addGroupModel.setGroupName(et_group_name.getText().toString());
        addGroupModel.setList(list);
        addGroupModel.setMemo(et_memo.getText().toString());

        String token = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null);

        RetrofitInstance.getInstance().getService().MakeGroup(token, addGroupModel).enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    SuccessResponse successResponse = response.body();
                    if (successResponse != null) {
                        if (successResponse.success) {
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("AddShareGroupActivity", successResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("AddShareGroupActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("AddShareGroupActivity", t.getMessage());
            }
        });
    }

    private View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.toolbar_left_btn:{
                onBackPressed();
                break;
            }
            case R.id.rl_invite:{
                setExpandable();
                break;
            }
            case R.id.btn_make:{
                sendGroupInfo();
                break;
            }
        }
    };

}
