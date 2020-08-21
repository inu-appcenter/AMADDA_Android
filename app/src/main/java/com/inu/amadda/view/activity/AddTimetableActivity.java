package com.inu.amadda.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.inu.amadda.R;
import com.inu.amadda.adapter.ClassAdapter;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.ClassData;
import com.inu.amadda.timetable.TimetableView;
import com.inu.amadda.util.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class AddTimetableActivity extends AppCompatActivity {

    private List<ClassData> classList = new ArrayList<>();

    private ClassAdapter adapter;

    private TimetableView timetable;
    private EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable);

        getTimetable();
        setRecyclerView();
        setToolbar();
        setBottomSheet();
        initialize();

    }

    private void getTimetable() {
        timetable = findViewById(R.id.timetable);

        String data = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TIMETABLE, null);
        if (data != null){
            timetable.load(data);
        }
    }

    private void setRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ClassAdapter(classList);
        recyclerView.setAdapter(adapter);

//        classList.add(new ClassData("아이덴티티디자인", "정휘인", "월 ", "12:00", "03:00", "16호관 213호"));
//        adapter.notifyDataSetChanged();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        AppCompatImageButton left_btn = toolbar.findViewById(R.id.toolbar_left_btn);
        left_btn.setOnClickListener(onClickListener);

        TextView right_btn_add = toolbar.findViewById(R.id.toolbar_right_add);
        right_btn_add.setOnClickListener(onClickListener);

        TextView right_btn_finish = toolbar.findViewById(R.id.toolbar_right_finish);
        right_btn_finish.setOnClickListener(onClickListener);
    }

    private void setBottomSheet() {
        CoordinatorLayout coordinatorLayout = findViewById(R.id.coordinatorLayout);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void initialize() {
        et_search = findViewById(R.id.et_search);

        ImageView iv_erase = findViewById(R.id.iv_erase);
        iv_erase.setOnClickListener(onClickListener);
    }

    private void saveTimetable() {
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.TIMETABLE, timetable.createSaveData());
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()) {
            case R.id.toolbar_left_btn: {
                onBackPressed();
                break;
            }
            case R.id.toolbar_right_add: {
                //AddClassActivity
                break;
            }
            case R.id.toolbar_right_finish: {
                saveTimetable();
                break;
            }
            case R.id.iv_erase: {
                et_search.setText("");
                break;
            }
        }
    };

}