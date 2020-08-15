package com.inu.amadda.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.adapter.InviteAdapter;
import com.inu.amadda.database.AppDatabase;
import com.inu.amadda.database.ShareGroup;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.InviteUserData;
import com.inu.amadda.model.MemberResponse;
import com.inu.amadda.model.RefusalModel;
import com.inu.amadda.model.SuccessResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.PreferenceManager;
import com.inu.amadda.util.Utils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditShareGroupActivity extends AppCompatActivity {

    private boolean isExpanded = false;
    private List<InviteUserData> inviteList = new ArrayList<>();
    private String token, color = null, pickColor = null, saveColor;
    private int share = -1;

    private InviteAdapter adapter;
    private AppDatabase appDatabase;
    private ShareGroup group;

    private EditText et_group_name, et_memo;
    private TextView tv_invite, tv_group_color;
    private ImageView iv_invite;
    private ExpandableLayout expandable_invite;
    private ColorPicker colorPicker;
    private View view_group_tag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_group);

        token = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null);
        appDatabase = AppDatabase.getInstance(this);

        getShareData();
        initialize();
        setToolbar();
        setRecyclerView();

    }

    private void getShareData() {
        Intent intent = getIntent();
        share = intent.getIntExtra("share", -1);

        getGroupInfo();
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
        right_btn_text.setText("완료");
        right_btn_text.setOnClickListener(onClickListener);

        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText("공유 그룹 수정");
    }

    private void initialize() {
        et_group_name = findViewById(R.id.et_group_name);

        RelativeLayout rl_invite = findViewById(R.id.rl_invite);
        rl_invite.setOnClickListener(onClickListener);
        tv_invite = findViewById(R.id.tv_invite);
        iv_invite = findViewById(R.id.iv_invite);
        iv_invite.setImageResource(R.drawable.down);

        expandable_invite = findViewById(R.id.expandable_invite);
        RelativeLayout rl_add_invitee = findViewById(R.id.rl_add_invitee);
        rl_add_invitee.setVisibility(View.GONE);

        RelativeLayout rl_group_color = findViewById(R.id.rl_group_color);
        rl_group_color.setOnClickListener(onClickListener);
        view_group_tag = findViewById(R.id.view_group_tag);
        tv_group_color = findViewById(R.id.tv_group_color);

        et_memo = findViewById(R.id.et_memo);

        TextView btn_make = findViewById(R.id.btn_make);
        btn_make.setOnClickListener(onClickListener);
        btn_make.setBackgroundResource(R.drawable.leave_group);
        btn_make.setText("그룹 나가기");
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_invite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new InviteAdapter(inviteList);
        recyclerView.setAdapter(adapter);

        setInvitees();
    }

    private void getGroupInfo() {
        new Thread(() -> {
            group = appDatabase.groupDao().getShareGroup(share).get(0);
            setInitialData();
        }).start();
    }

    private void setInitialData() {
        et_group_name.setText(group.getGroup_name());

        tv_invite.setText(PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.ID, null));
        tv_invite.setTextColor(ContextCompat.getColor(this, R.color.color_707070));

        color = group.getColor();
        if (color != null){
            view_group_tag.setBackgroundColor(Color.parseColor(color));
            view_group_tag.setVisibility(View.VISIBLE);
            tv_group_color.setTextColor(ContextCompat.getColor(this, R.color.color_6a6a6a));
            tv_group_color.setText(String.valueOf(color));
        }

        et_memo.setText(group.getMemo());
    }

    private void setExpandable() {
        if (isExpanded) {
            expandable_invite.collapse();
            iv_invite.setImageResource(R.drawable.down);
            isExpanded = false;
        }
        else {
            expandable_invite.expand();
            iv_invite.setImageResource(R.drawable.up);
            isExpanded = true;
        }
    }

    private void setInvitees() {
        tv_invite.setText(PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.ID, null));
        tv_invite.setTextColor(ContextCompat.getColor(this, R.color.color_707070));

        RetrofitInstance.getInstance().getService().GetGroupMembers(token, share).enqueue(new Callback<MemberResponse>() {
            @Override
            public void onResponse(Call<MemberResponse> call, Response<MemberResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    MemberResponse memberResponse = response.body();
                    if (memberResponse != null) {
                        if (memberResponse.success) {
                            InviteUserData userData = new InviteUserData();
                            userData.setId(memberResponse.members.get(0).getId());
                            userData.setName(memberResponse.members.get(0).getName());
                            userData.setPath(memberResponse.members.get(0).getPath());
                            inviteList.add(userData);
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("EditShareGroupActivity", memberResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("EditShareGroupActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<MemberResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("EditShareGroupActivity", t.getMessage());
            }
        });
    }

    private void setColorPicker() {
        colorPicker = new ColorPicker(this);
        colorPicker
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position,int colorChoice) {
                        if (colorChoice != 0){
                            view_group_tag.setBackgroundColor(colorChoice);
                            view_group_tag.setVisibility(View.VISIBLE);
                            tv_group_color.setTextColor(ContextCompat.getColor(EditShareGroupActivity.this, R.color.color_6a6a6a));
                            pickColor = Utils.toStringColor(colorChoice);
                            tv_group_color.setText(pickColor);
                        }
                        ((ViewGroup) colorPicker.getDialogViewLayout().getParent()).removeView(colorPicker.getDialogViewLayout());
                    }

                    @Override
                    public void onCancel(){
                        ((ViewGroup) colorPicker.getDialogViewLayout().getParent()).removeView(colorPicker.getDialogViewLayout());
                    }
                })
                .setTitle("그룹 컬러 선택")
                .setColors(R.array.color_picker_array)
                .setRoundColorButton(true);
        colorPicker.show();
    }

    private void saveGroupInfo() {
        saveColor = pickColor != null ? pickColor : color;
        new Thread(() -> {
            appDatabase.groupDao().update(new ShareGroup(share, et_group_name.getText().toString(),
                    et_memo.getText().toString(), saveColor));
            Log.d("EditShareGroupActivity", "Save group: " + share);
        }).start();
        finish();
    }

    private void leaveGroup() {
        RefusalModel model = new RefusalModel();
        model.share = share;
        RetrofitInstance.getInstance().getService().LeaveGroup(token, model).enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    SuccessResponse successResponse = response.body();
                    if (successResponse != null) {
                        if (successResponse.success) {
                            new Thread(() -> {
                                appDatabase.groupDao().deleteByKey(share);
                                Log.d("EditShareGroupActivity", "Delete group: " + share);
                            }).start();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("EditShareGroupActivity", successResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("EditShareGroupActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("EditShareGroupActivity", t.getMessage());
            }
        });
    }

    private View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.toolbar_left_btn:{
                onBackPressed();
                break;
            }
            case R.id.toolbar_right_text:{
                if (color == null && pickColor == null){
                    Toast.makeText(getApplicationContext(), "그룹 컬러를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveGroupInfo();
                }
                break;
            }
            case R.id.rl_invite:{
                setExpandable();
                break;
            }
            case R.id.rl_group_color:{
                setColorPicker();
                break;
            }
            case R.id.btn_make:{
                leaveGroup();
                break;
            }
        }
    };

}
