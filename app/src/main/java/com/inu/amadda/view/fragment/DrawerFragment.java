package com.inu.amadda.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.inu.amadda.R;
import com.inu.amadda.view.activity.AddShareGroupActivity;
import com.inu.amadda.view.activity.SettingActivity;

public class DrawerFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer, container, false);

        initialize(view);

        return view;
    }

    private void initialize(View view) {
        ImageButton btn_share_group = view.findViewById(R.id.btn_share_group);
        ImageButton btn_setting = view.findViewById(R.id.btn_setting);

        btn_share_group.setOnClickListener(onClickListener);
        btn_setting.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
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