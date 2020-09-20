package com.inu.amadda.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.inu.amadda.R;
import com.inu.amadda.adapter.GroupListAdapter;
import com.inu.amadda.database.AppDatabase;
import com.inu.amadda.database.ShareGroup;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.InvitationResponse;
import com.inu.amadda.model.SidebarData;
import com.inu.amadda.model.SidebarResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.DateUtils;
import com.inu.amadda.util.PreferenceManager;
import com.inu.amadda.view.fragment.CalendarFragment;
import com.inu.amadda.view.fragment.TimetableFragment;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private boolean changeToCalender = true;

    private CircleImageView btn_add_schedule;
    private Animation rotate_forward, rotate_backward;
    private PopupMenu popup;
    private DrawerLayout drawerLayout;

    private String token;
    private List<ShareGroup> groupList = new ArrayList<>();

    private GroupListAdapter adapter;
    private AppDatabase appDatabase;

    private CircleImageView iv_image;
    private TextView tv_invitation_number, tv_major, tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        token = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null);
        appDatabase = AppDatabase.getInstance(this);

        drawerLayout = findViewById(R.id.drawer_layout);

        setToolbar();
        setFloatingActionButton();

        setDefaultView();

        initialize();
        setRecyclerView();
        getSidebarInfo();
        getInvitationNumber();
        getGroupList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppDatabase.destroyInstance();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppCompatImageButton left_btn = toolbar.findViewById(R.id.toolbar_left_btn);
        left_btn.setImageResource(R.drawable.side_menu);
        left_btn.setOnClickListener(onClickListener);

        AppCompatImageButton right_btn_image = toolbar.findViewById(R.id.toolbar_right_image);
        right_btn_image.setImageResource(R.drawable.calendar);
        right_btn_image.setOnClickListener(onClickListener);

        TextView right_btn_text = toolbar.findViewById(R.id.toolbar_right_text);
        right_btn_text.setVisibility(View.GONE);

        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText(DateUtils.getTitleString(LocalDate.now()));
    }

    private void setFloatingActionButton() {
        btn_add_schedule = findViewById(R.id.btn_add_schedule);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        btn_add_schedule.setOnClickListener(onClickListener);
    }

    private void setPopupMenu(View view) {
        popup = new PopupMenu(this, view);
        popup.inflate(R.menu.menu_add_schedule);
        popup.setOnMenuItemClickListener(onMenuItemClickListener);
        popup.setOnDismissListener(onDismissListener);
    }

    private void setDefaultView(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        boolean isTimetable = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.DEFAULT_VIEW,
                Constant.DefaultView.TIMETABLE);

        if (isTimetable){
            transaction.replace(R.id.fragment_container, new TimetableFragment()).commit();
            changeToCalender = true;
        }
        else {
            transaction.replace(R.id.fragment_container, new CalendarFragment()).commit();
            changeToCalender = false;
        }
    }

    private void changeView(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (changeToCalender){
            transaction.replace(R.id.fragment_container, new CalendarFragment()).commit();
            changeToCalender = false;
        }
        else {
            transaction.replace(R.id.fragment_container, new TimetableFragment()).commit();
            changeToCalender = true;
        }
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.btn_add_schedule:{
                if (popup == null)
                    setPopupMenu(view);
                btn_add_schedule.startAnimation(rotate_forward);
                popup.show();
                break;
            }
            case R.id.toolbar_right_image:{
                changeView();
                break;
            }
            case R.id.toolbar_left_btn:{
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            }
            case R.id.personal_schedule:{
                Intent intent = new Intent(this, ScheduleListActivity.class);
                intent.putExtra("share", -1);
                startActivity(intent);
                break;
            }
            case R.id.invitation_management:{
                Intent intent = new Intent(this, ManageInvitationActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_share_group:{
                Intent intent = new Intent(this, AddShareGroupActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_setting:{
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            }
        }
    };

    PopupMenu.OnMenuItemClickListener onMenuItemClickListener = menuItem -> {
        Intent intent;
        switch (menuItem.getItemId()) {
            case R.id.add_class:
                intent = new Intent(this, AddTimetableActivity.class);
                startActivity(intent);
                return true;
            case R.id.personal_schedule:
                intent = new Intent(this, AddScheduleActivity.class);
                intent.putExtra("isPersonal", true);
                startActivity(intent);
                return true;
            case R.id.shared_schedule:
                intent = new Intent(this, AddScheduleActivity.class);
                intent.putExtra("isPersonal", false);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    };

    PopupMenu.OnDismissListener onDismissListener = popupMenu -> btn_add_schedule.startAnimation(rotate_backward);

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initialize() {
        RelativeLayout rl_personal = findViewById(R.id.personal_schedule);
        RelativeLayout rl_invitation = findViewById(R.id.invitation_management);

        rl_personal.setOnClickListener(onClickListener);
        rl_invitation.setOnClickListener(onClickListener);

        ImageButton btn_share_group = findViewById(R.id.btn_share_group);
        ImageButton btn_setting = findViewById(R.id.btn_setting);

        btn_share_group.setOnClickListener(onClickListener);
        btn_setting.setOnClickListener(onClickListener);

        iv_image = findViewById(R.id.iv_image);
        tv_major = findViewById(R.id.tv_major);
        tv_name = findViewById(R.id.tv_name);
        tv_invitation_number = findViewById(R.id.tv_invitation_number);
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_shared_group);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
                                Glide.with(MainActivity.this).load(data.getPath())
                                        .thumbnail(0.5f)
                                        .error(R.drawable.group_profile)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(iv_image);
                            }
                            tv_major.setText(data.getMajor());
                            tv_name.setText(data.getName());
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("MainActivity", sidebarResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<SidebarResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", t.getMessage());
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
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("MainActivity", invitationResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<InvitationResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("MainActivity", t.getMessage());
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

}
