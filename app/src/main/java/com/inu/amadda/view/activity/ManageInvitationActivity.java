package com.inu.amadda.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.adapter.InvitationAdapter;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.InvitationData;
import com.inu.amadda.model.InvitationResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageInvitationActivity extends AppCompatActivity {

    private String token;
    private List<InvitationData> invitationList = new ArrayList<>();

    private InvitationAdapter adapter;

    private LinearLayout ll_blank;
    private TextView tv_invitation_number, tv_message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_invitation);

        token = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null);

        setToolbar();
        initialize();
        setRecyclerView();
        getInvitation();

    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppCompatImageButton left_btn = toolbar.findViewById(R.id.toolbar_left_btn);
        left_btn.setImageResource(R.drawable.back);
        left_btn.setOnClickListener(onClickListener);

        AppCompatImageButton right_btn_image = toolbar.findViewById(R.id.toolbar_right_image);
        right_btn_image.setVisibility(View.GONE);

        TextView right_btn_text = toolbar.findViewById(R.id.toolbar_right_text);
        right_btn_text.setVisibility(View.GONE);

        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText("초대 관리");
    }

    private void initialize() {
        ll_blank = findViewById(R.id.ll_blank);

        tv_invitation_number = findViewById(R.id.tv_invitation_number);
        tv_message = findViewById(R.id.tv_message);
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_invitation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new InvitationAdapter(invitationList, tv_invitation_number, tv_message);
        recyclerView.setAdapter(adapter);
    }

    private void getInvitation() {
        RetrofitInstance.getInstance().getService().GetInvitations(token).enqueue(new Callback<InvitationResponse>() {
            @Override
            public void onResponse(Call<InvitationResponse> call, Response<InvitationResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    InvitationResponse invitationResponse = response.body();
                    if (invitationResponse != null) {
                        if (invitationResponse.success) {
                            if (invitationResponse.invitations.size() > 0){
                                ll_blank.setVisibility(View.INVISIBLE);
                                tv_invitation_number.setVisibility(View.VISIBLE);
                                tv_invitation_number.setText(String.valueOf(invitationResponse.invitations.size()));

                                invitationList.clear();
                                invitationList.addAll(invitationResponse.invitations);
                                adapter.notifyDataSetChanged();
                            }
                            else {
                                ll_blank.setVisibility(View.VISIBLE);
                                tv_invitation_number.setVisibility(View.INVISIBLE);
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("ManageInvitationActivity", invitationResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("ManageInvitationActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<InvitationResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("ManageInvitationActivity", t.getMessage());
            }
        });
    }

    private View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.toolbar_left_btn:{
                onBackPressed();
                break;
            }
        }
    };

}
