package com.inu.amadda.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.database.AppDatabase;
import com.inu.amadda.database.ShareGroup;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.InvitationData;
import com.inu.amadda.model.RefusalModel;
import com.inu.amadda.model.SuccessResponse;
import com.inu.amadda.network.RetrofitInstance;
import com.inu.amadda.util.PreferenceManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder> {

    private List<InvitationData> mList;
    private Context mContext;
    private TextView tv_invitation_number, tv_message;
    private AppDatabase appDatabase;

    public InvitationAdapter(List<InvitationData> list, Context context, TextView textViewNumber, TextView textViewMessage) {
        this.mList = list;
        this.mContext = context;
        this.tv_invitation_number = textViewNumber;
        this.tv_message = textViewMessage;
        appDatabase = AppDatabase.getInstance(mContext);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View view_group_tag;
        TextView tv_group_name, tv_inviter, tv_memo, tv_refusal, tv_acceptance;
        LinearLayout ll_buttons;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            view_group_tag = itemView.findViewById(R.id.view_group_tag);
            tv_group_name = itemView.findViewById(R.id.tv_group_name);
            tv_inviter = itemView.findViewById(R.id.tv_inviter);
            tv_memo = itemView.findViewById(R.id.tv_memo);
            tv_refusal = itemView.findViewById(R.id.tv_refusal);
            tv_acceptance = itemView.findViewById(R.id.tv_acceptance);
            ll_buttons = itemView.findViewById(R.id.ll_buttons);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InvitationAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invitation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvitationData item = mList.get(position);
        //view_group_tag color
        holder.tv_group_name.setText(item.getGroup_name());
        holder.tv_inviter.setText(item.getInviter_name() + " " + item.getInviter_id());
        holder.tv_memo.setText(item.getMemo());
        holder.tv_refusal.setOnClickListener(view -> {
            refuseInvitation(item.getShare(), holder.ll_buttons);
        });
        holder.tv_acceptance.setOnClickListener(view -> {
            acceptInvitation(item, holder.ll_buttons);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void refuseInvitation(int share, LinearLayout layout) {
        String token = PreferenceManager.getInstance().getSharedPreference(mContext, Constant.Preference.TOKEN, null);
        RefusalModel model = new RefusalModel();
        model.share = share;

        RetrofitInstance.getInstance().getService().refuseInvitation(token, model).enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    SuccessResponse successResponse = response.body();
                    if (successResponse != null) {
                        if (successResponse.success) {
                            layout.setVisibility(View.GONE);
                            tv_invitation_number.setVisibility(View.INVISIBLE);
                            tv_message.setVisibility(View.VISIBLE);
                            tv_message.setText("거절되었습니다.");
                            tv_message.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff0000));
                        }
                        else {
                            Toast.makeText(mContext, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("InvitationAdapter", successResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(mContext, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(mContext, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("InvitationAdapter", status + "");
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                Toast.makeText(mContext, "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("InvitationAdapter", t.getMessage());
            }
        });
    }

    private void acceptInvitation(InvitationData data, LinearLayout layout) {
        String token = PreferenceManager.getInstance().getSharedPreference(mContext, Constant.Preference.TOKEN, null);

        RetrofitInstance.getInstance().getService().acceptInvitation(token, data.getShare()).enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(Call<SuccessResponse> call, Response<SuccessResponse> response) {
                int status = response.code();
                if (response.isSuccessful()) {
                    SuccessResponse successResponse = response.body();
                    if (successResponse != null) {
                        if (successResponse.success) {
                            layout.setVisibility(View.GONE);
                            tv_invitation_number.setVisibility(View.INVISIBLE);
                            tv_message.setVisibility(View.VISIBLE);
                            tv_message.setText("수락되었습니다.");
                            tv_message.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
                            new Thread(() -> {
                                appDatabase.groupDao().insert(new ShareGroup(data.getShare(), data.getGroup_name(), data.getMemo(), "#ff3b30"));
                                Log.d("InvitationAdapter", "Save group: " + data.getShare());
                            }).start();
                        }
                        else {
                            Toast.makeText(mContext, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            Log.d("InvitationAdapter", successResponse.message);
                        }
                    }
                    else {
                        Toast.makeText(mContext, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(mContext, "잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("InvitationAdapter", status + "");
                }
            }

            @Override
            public void onFailure(Call<SuccessResponse> call, Throwable t) {
                Toast.makeText(mContext, "인터넷 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
                Log.d("InvitationAdapter", t.getMessage());
            }
        });
    }

}
