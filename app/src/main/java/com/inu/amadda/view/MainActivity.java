package com.inu.amadda.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.inu.amadda.R;
import com.inu.amadda.util.DateUtils;

public class MainActivity extends AppCompatActivity {

    private boolean changeToCalender = true;

    private FloatingActionButton fab;
    private Animation rotate_forward, rotate_backward;
    private PopupMenu popup;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);

        setToolbar();
        setFloatingActionButton();

        setDefaultView();
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
        title.setText(DateUtils.getToday());
    }

    private void setFloatingActionButton() {
        fab = findViewById(R.id.fab_add_schedule);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(onClickListener);
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

        boolean isTimetable = true;

        if (isTimetable){
            transaction.replace(R.id.fragment_container, new TimetableFragment()).commit();
        }
        else {
            transaction.replace(R.id.fragment_container, new CalendarFragment()).commit();
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
            case R.id.fab_add_schedule:{
                if (popup == null)
                    setPopupMenu(view);
                fab.startAnimation(rotate_forward);
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
        }
    };

    PopupMenu.OnMenuItemClickListener onMenuItemClickListener = menuItem -> {
        switch (menuItem.getItemId()) {
            case R.id.add_class:

                return true;
            case R.id.personal_schedule:
                Intent intent = new Intent(this, AddScheduleActivity.class);
                startActivity(intent);
                return true;
            case R.id.shared_schedule:

                return true;
            default:
                return false;
        }
    };

    PopupMenu.OnDismissListener onDismissListener = popupMenu -> fab.startAnimation(rotate_backward);

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
