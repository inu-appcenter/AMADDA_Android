package com.inu.amadda.view.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.inu.amadda.R;

public class ImageBottomSheetDialog extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_image, container, false);

        initialize(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        View bottomSheet = dialog.findViewById(R.id.design_bottom_sheet);
        bottomSheet.getLayoutParams().height = dpToPx(260);
    }

    private int dpToPx(int dp){
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void initialize(View view) {
        TextView tv_camera = view.findViewById(R.id.tv_camera);
        tv_camera.setOnClickListener(onClickListener);

        TextView tv_album = view.findViewById(R.id.tv_album);
        tv_album.setOnClickListener(onClickListener);

        TextView tv_default = view.findViewById(R.id.tv_default);
        tv_default.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.tv_camera:{

                break;
            }
            case R.id.tv_album:{

                break;
            }
            case R.id.tv_default:{

                break;
            }
        }
    };

}
