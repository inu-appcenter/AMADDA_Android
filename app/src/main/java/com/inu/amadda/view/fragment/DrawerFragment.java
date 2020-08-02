package com.inu.amadda.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.inu.amadda.R;
import com.inu.amadda.adapter.GroupListAdapter;
import com.inu.amadda.database.AppDatabase;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.InvitationResponse;
import com.inu.amadda.database.ShareGroup;
import com.inu.amadda.model.SidebarData;
import com.inu.amadda.model.SidebarResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.PreferenceManager;
import com.inu.amadda.view.activity.AddShareGroupActivity;
import com.inu.amadda.view.activity.ManageInvitationActivity;
import com.inu.amadda.view.activity.SettingActivity;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawerFragment extends Fragment {

    private String token;
    private List<ShareGroup> groupList = new ArrayList<>();

    private GroupListAdapter adapter;
    private AppDatabase appDatabase;

    private CircleImageView iv_image;
    private TextView tv_invitation_number, tv_major, tv_name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer, container, false);

        token = PreferenceManager.getInstance().getSharedPreference(getContext(), Constant.Preference.TOKEN, null);
        appDatabase = AppDatabase.getInstance(getActivity());

        initialize(view);
        setRecyclerView(view);
        getSidebarInfo();
        getInvitationNumber();
        getGroupList();

        return view;
    }

    private void initialize(View view) {
        RelativeLayout rl_personal = view.findViewById(R.id.personal_schedule);
        RelativeLayout rl_invitation = view.findViewById(R.id.invitation_management);

        rl_personal.setOnClickListener(onClickListener);
        rl_invitation.setOnClickListener(onClickListener);

        ImageButton btn_share_group = view.findViewById(R.id.btn_share_group);
        ImageButton btn_setting = view.findViewById(R.id.btn_setting);

        btn_share_group.setOnClickListener(onClickListener);
        btn_setting.setOnClickListener(onClickListener);

        iv_image = view.findViewById(R.id.iv_image);
        tv_major = view.findViewById(R.id.tv_major);
        tv_name = view.findViewById(R.id.tv_name);
        tv_invitation_number = view.findViewById(R.id.tv_invitation_number);
    }

    private void setRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_shared_group);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new GroupListAdapter(groupList);
        recyclerView.setAdapter(adapter);
    }

    private void getSidebarInfo() {
        RetrofitInstance.getInstance().getService().GetSidebar(token).enqueue(new Callback<SidebarResponse>() {
            @Override
            public void onResponse(Call<SidebarResponse> call, Response<SidebarResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    SidebarResponse sidebarResponse = response.body();
                    if (sidebarResponse != null) {
                        if (sidebarResponse.success) {
                            SidebarData data = sidebarResponse.sidebar;
                            if (data.getPath() != null){
                                Glide.with(getContext()).load(data.getPath())
                                        .thumbnail(0.5f)
                                        .error(R.drawable.group_profile)
                                        .into(iv_image);
                            }
                            tv_major.setText(data.getMajor());
                            tv_name.setText(data.getName());
                        }
                        else {
                            Toast.makeText(getContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("DrawerFragment", sidebarResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("DrawerFragment", status + "");
                }
            }

            @Override
            public void onFailure(Call<SidebarResponse> call, Throwable t) {
                Toast.makeText(getContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("DrawerFragment", t.getMessage());
            }
        });
    }

    private void getInvitationNumber() {
        RetrofitInstance.getInstance().getService().GetInvitations(token).enqueue(new Callback<InvitationResponse>() {
            @Override
            public void onResponse(Call<InvitationResponse> call, Response<InvitationResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    InvitationResponse invitationResponse = response.body();
                    if (invitationResponse != null) {
                        if (invitationResponse.success) {
                            int num = invitationResponse.invitations.size();
                            if (num > 0){
                                tv_invitation_number.setVisibility(View.VISIBLE);
                                tv_invitation_number.setText(num + "");
                            }
                            else {
                                tv_invitation_number.setVisibility(View.INVISIBLE);
                            }
                        }
                        else {
                            Toast.makeText(getContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("DrawerFragment", invitationResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("DrawerFragment", status + "");
                }
            }

            @Override
            public void onFailure(Call<InvitationResponse> call, Throwable t) {
                Toast.makeText(getContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("DrawerFragment", t.getMessage());
            }
        });
    }

    private void getGroupList() {
        LiveData<List<ShareGroup>> list = appDatabase.groupDao().getAll();
        list.observe(this, groups -> {
            groupList.clear();
            groupList.addAll(groups);
            adapter.notifyDataSetChanged();
        });
    }

    private View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.personal_schedule:{
                break;
            }
            case R.id.invitation_management:{
                Intent intent = new Intent(getActivity(), ManageInvitationActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_share_group:{
                Intent intent = new Intent(getActivity(), AddShareGroupActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_setting:{
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            }
        }
    };

}