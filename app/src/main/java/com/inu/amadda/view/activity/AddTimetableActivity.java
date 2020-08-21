package com.inu.amadda.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.inu.amadda.model.ClassResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.timetable.Schedule;
import com.inu.amadda.timetable.Time;
import com.inu.amadda.timetable.TimetableView;
import com.inu.amadda.util.PreferenceManager;

import org.threeten.bp.LocalTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTimetableActivity extends AppCompatActivity implements ClassAdapter.OnSelectListener {

    private List<ClassData> classList = new ArrayList<>();
    private List<ClassData> addList = new ArrayList<>();

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

    @Override
    public void onSelect(ClassData data) {
        addClassSticker(data);
    }

    private void addClassSticker(ClassData data) {
        String day = data.getDay();
        LocalTime startTime = StringToLocalTime(data.getStart());
        LocalTime endTime = StringToLocalTime(data.getEnd());

        if (checkOverlapping(day, startTime, endTime)){
            ArrayList<Schedule> schedules = new ArrayList<>();

            Schedule schedule = new Schedule();
            schedule.setClassTitle(data.getLecture());
            schedule.setClassPlace(data.getRoom());
            schedule.setProfessorName(data.getProfessor());
            schedule.setStartTime(new Time(startTime.getHour(),startTime.getMinute()));
            schedule.setEndTime(new Time(endTime.getHour(),endTime.getMinute()));
            switch (day){
                case "월":
                    schedule.setDay(0);
                    break;
                case "화":
                    schedule.setDay(1);
                    break;
                case "수":
                    schedule.setDay(2);
                    break;
                case "목":
                    schedule.setDay(3);
                    break;
                case "금":
                    schedule.setDay(4);
                    break;
                default:
                    break;
            }

            schedules.add(schedule);
            timetable.addClass(schedules);

            addList.add(data);
        }
        else {
            Toast.makeText(getApplicationContext(), "시간이 겹쳐서 추가하지 못했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkOverlapping(String day, LocalTime startTime, LocalTime endTime){
        boolean isPossible = true;
        for (int i = 0; i < addList.size(); i++){
            LocalTime currentStartTime = StringToLocalTime(addList.get(i).getStart());
            LocalTime currentEndTime = StringToLocalTime(addList.get(i).getEnd());
            if (day.equals(addList.get(i).getDay())){
                if (((startTime.equals(currentStartTime) || startTime.isAfter(currentStartTime)) &&
                        (startTime.equals(currentEndTime) || startTime.isBefore(currentEndTime))) ||
                        ((endTime.equals(currentStartTime) || endTime.isAfter(currentStartTime)) &&
                                (endTime.equals(currentEndTime) || endTime.isBefore(currentEndTime)))){
                    isPossible = false;
                }
            }
        }
        return isPossible;
    }

    private LocalTime StringToLocalTime(String string){
        return LocalTime.parse(string, DateTimeFormatter.ofPattern("HH:mm"));
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

        adapter = new ClassAdapter(classList, this);
        recyclerView.setAdapter(adapter);
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
        et_search.setOnEditorActionListener(onEditorActionListener);

        ImageView iv_erase = findViewById(R.id.iv_erase);
        iv_erase.setOnClickListener(onClickListener);
    }

    private void saveTimetable() {
        PreferenceManager.getInstance().putSharedPreference(getApplicationContext(), Constant.Preference.TIMETABLE, timetable.createSaveData());
    }

    TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchClass();
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(et_search.getWindowToken(), 0);
                }
                handled = true;
            }
            return handled;
        }
    };

    private void searchClass() {
        String token = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null);
        String name = et_search.getText().toString();

        RetrofitInstance.getInstance().getService().GetClasses(token, name).enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(Call<ClassResponse> call, Response<ClassResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    ClassResponse classResponse = response.body();
                    if (classResponse != null) {
                        if (classResponse.success) {
                            classList.clear();
                            classList.addAll(classResponse.subjects);
                            adapter.notifyDataSetChanged();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("AddTimetableActivity", classResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("AddTimetableActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<ClassResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("AddTimetableActivity", t.getMessage());
            }
        });
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
                classList.clear();
                adapter.notifyDataSetChanged();
                break;
            }
        }
    };

}