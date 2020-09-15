package com.inu.amadda.view.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.inu.amadda.R;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.UserImageResponse;
import com.inu.amadda.model.UserProfileResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.PreferenceManager;
import com.inu.amadda.view.fragment.ImageBottomSheetDialog;
import com.yanzhenjie.album.Album;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final int EDIT = 100, DELETE = 200, NONE = 300;
    private int EDIT_MODE = NONE;
    private String token, imagePath = null;

    private CircleImageView iv_profile;
    private TextView tv_name, tv_major;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        token = PreferenceManager.getInstance().getSharedPreference(getApplicationContext(), Constant.Preference.TOKEN, null);

        initialize();
        setToolbar();
        getUserImage();
        getUserInfo();

    }

    private ImageBottomSheetDialog.OnDismissListener onDismissListener = new ImageBottomSheetDialog.OnDismissListener() {

        @Override
        public void onDismiss(DialogInterface dialogInterface) {
            Log.d("EditProfileActivity", "dismiss");
        }

        @Override
        public void onDismiss(String path) {
            if (path != null) {
                if (path.equals("default")) {
                    iv_profile.setImageResource(R.drawable.edit_profile);
                }
                else {
                    Album.getAlbumConfig()
                            .getAlbumLoader()
                            .load(iv_profile, path);
                }
                imagePath = path;
                Log.d("EditProfileActivity", imagePath);
            }
        }
    };

    private void initialize() {
        iv_profile = findViewById(R.id.iv_profile);

        ImageView btn_add_picture = findViewById(R.id.btn_add_picture);
        btn_add_picture.setOnClickListener(onClickListener);

        tv_name = findViewById(R.id.tv_name);

        tv_major = findViewById(R.id.tv_major);
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
        right_btn_text.setText("확인");
        right_btn_text.setOnClickListener(onClickListener);

        TextView title = toolbar.findViewById(R.id.toolbar_title);
        title.setText("프로필 편집");
    }

    private void getUserImage() {
        RetrofitInstance.getInstance().getService().GetUserImage(token).enqueue(new Callback<UserImageResponse>() {
            @Override
            public void onResponse(Call<UserImageResponse> call, Response<UserImageResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    UserImageResponse userImageResponse = response.body();
                    if (userImageResponse != null) {
                        if (userImageResponse.success) {
                            if (userImageResponse.path != null) {
                                Glide.with(EditProfileActivity.this)
                                        .load(userImageResponse.path)
                                        .error(R.drawable.edit_profile)
                                        .placeholder(R.drawable.edit_profile)
                                        .into(iv_profile);
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("EditProfileActivity", userImageResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("EditProfileActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<UserImageResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("EditProfileActivity", t.getMessage());
            }
        });
    }

    private void getUserInfo() {
        RetrofitInstance.getInstance().getService().GetUserProfile(token).enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call, Response<UserProfileResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    UserProfileResponse userProfileResponse = response.body();
                    if (userProfileResponse != null) {
                        if (userProfileResponse.success) {
                            tv_name.setText(userProfileResponse.user.getName());
                            tv_major.setText(userProfileResponse.user.getMajor());
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("EditProfileActivity", userProfileResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("EditProfileActivity", status + "");
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("EditProfileActivity", t.getMessage());
            }
        });
    }

    private void uploadUserImage() {
    }

    private void deleteUserImage() {
    }

    private void showBottomSheetDialog() {
        ImageBottomSheetDialog dialog = new ImageBottomSheetDialog();
        dialog.setOnDismissListener(onDismissListener);
        dialog.show(getSupportFragmentManager(), "ImageBottomSheetDialog");
    }

    private View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.toolbar_left_btn:{
                onBackPressed();
                break;
            }
            case R.id.toolbar_right_text:{
                if (EDIT_MODE == EDIT) {
                    uploadUserImage();
                }
                else if (EDIT_MODE == DELETE) {
                    deleteUserImage();
                }
                else {
                    finish();
                }
                break;
            }
            case R.id.btn_add_picture:{
                showBottomSheetDialog();
                break;
            }
        }
    };

}
