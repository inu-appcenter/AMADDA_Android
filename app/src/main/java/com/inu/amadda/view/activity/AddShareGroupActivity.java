package com.inu.amadda.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.adapter.InviteAdapter;
import com.inu.amadda.database.AppDatabase;
import com.inu.amadda.database.ShareGroup;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.AddGroupModel;
import com.inu.amadda.model.AddGroupResponse;
import com.inu.amadda.model.InviteUserData;
import com.inu.amadda.model.SearchUserResponse;
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

public class AddShareGroupActivity extends AppCompatActivity {

    private boolean isExpanded = false, isFirst = true;
    private List<String> idList = new ArrayList<>();
    private List<InviteUserData> inviteList = new ArrayList<>();
    private String token, user, pickColor = null;

    private InviteAdapter adapter;
    private AppDatabase appDatabase;

    private EditText et_group_name, et_invite, et_memo;
    private TextView tv_invite, tv_group_color;
    private ImageView iv_invite;
    private ExpandableLayout expandable_invite;
    private ColorPicker colorPicker;
    private View view_group_tag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_group);

        initialize();
        setToolbar();
        setRecyclerView();

        token = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null);
        appDatabase = AppDatabase.getInstance(this);

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
        view_group_tag = findViewById(R.id.view_group_tag);
        tv_group_color = findViewById(R.id.tv_group_color);

        et_memo = findViewById(R.id.et_memo);

        TextView btn_make = findViewById(R.id.btn_make);
        btn_make.setOnClickListener(onClickListener);
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_invite);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new InviteAdapter(inviteList);
        recyclerView.setAdapter(adapter);
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

    private void searchUser() {
        RetrofitInstance.getInstance().getService().SearchUser(token, user).enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    SearchUserResponse searchUserResponse = response.body();
                    if (searchUserResponse != null) {
                        if (searchUserResponse.success) {
                            InviteUserData userData = new InviteUserData();
                            userData.setId(searchUserResponse.users.get(0).getId());
                            userData.setName(searchUserResponse.users.get(0).getName());
                            userData.setPath(searchUserResponse.users.get(0).getPath());
                            inviteList.add(userData);
                            adapter.notifyDataSetChanged();
                            et_invite.setText("");
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("AddShareGroupActivity", searchUserResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(status == 404) {
                    Toast.makeText(getApplicationContext(), "존재하지 않는 회원입니다.", Toast.LENGTH_SHORT).show();
                    Log.d("AddShareGroupActivity", status + "");
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("AddShareGroupActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("AddShareGroupActivity", t.getMessage());
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
                            tv_group_color.setTextColor(ContextCompat.getColor(AddShareGroupActivity.this, R.color.color_6a6a6a));
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

    private void sendGroupInfo() {
        AddGroupModel addGroupModel = new AddGroupModel();
        addGroupModel.setGroupName(et_group_name.getText().toString());
        for (int i = 0; i  < inviteList.size(); i++){
            idList.add(inviteList.get(i).getId());
        }
        addGroupModel.setList(idList);
        addGroupModel.setMemo(et_memo.getText().toString());

        RetrofitInstance.getInstance().getService().MakeGroup(token, addGroupModel).enqueue(new Callback<AddGroupResponse>() {
            @Override
            public void onResponse(Call<AddGroupResponse> call, Response<AddGroupResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    AddGroupResponse addGroupResponse = response.body();
                    if (addGroupResponse != null) {
                        if (addGroupResponse.success) {
                            finish();
                            new Thread(() -> {
                                appDatabase.groupDao().insert(new ShareGroup(addGroupResponse.share, et_group_name.getText().toString(),
                                        et_memo.getText().toString(), pickColor));
                                Log.d("AddShareGroupActivity", "Save group: " + addGroupResponse.share);
                            }).start();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("AddShareGroupActivity", addGroupResponse.message);
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
            public void onFailure(Call<AddGroupResponse> call, Throwable t) {
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
            case R.id.btn_invite:{
                user = et_invite.getText().toString();
                if (user.length() == 9){
                    searchUser();
                }
                else{
                    Toast.makeText(getApplicationContext(), "학번 9자리를 정확히 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.rl_group_color:{
                setColorPicker();
                break;
            }
            case R.id.btn_make:{
                if (pickColor == null){
                    Toast.makeText(getApplicationContext(), "그룹 컬러를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendGroupInfo();
                }
                break;
            }
        }
    };

}
