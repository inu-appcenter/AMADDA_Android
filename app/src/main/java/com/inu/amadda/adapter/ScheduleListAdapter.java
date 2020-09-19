package com.inu.amadda.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.database.AppDatabase;
import com.inu.amadda.etc.Constant;
import com.inu.amadda.model.ScheduleData;
import com.inu.amadda.util.DateUtils;
import com.inu.amadda.util.PreferenceManager;
import com.inu.amadda.view.activity.EditScheduleActivity;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {

    private List<ScheduleData> mList;
    private Context mContext;
    private String color = null;

    private AppDatabase appDatabase;

    public ScheduleListAdapter(List<ScheduleData> list) {
        this.mList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CardView cv_schedule;
        View view_group_tag;
        TextView tv_name, tv_time, tv_location, tv_memo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            cv_schedule = itemView.findViewById(R.id.cv_schedule);
            view_group_tag = itemView.findViewById(R.id.view_group_tag);
            if (color != null)
                view_group_tag.setBackgroundColor(Color.parseColor(color));
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_memo = itemView.findViewById(R.id.tv_memo);
        }
    }

    @NonNull
    @Override
    public ScheduleListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        appDatabase = AppDatabase.getInstance(mContext);
        int share = mList.get(0).getShare();
        if (share > 0){
            new Thread(() -> {
                color = appDatabase.groupDao().getColorByKey(share);
            }).start();
        }
        else {
            color = PreferenceManager.getInstance().getSharedPreference(mContext, Constant.Preference.COLOR, null);
        }
        return new ScheduleListAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_schedule_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleListAdapter.ViewHolder holder, int position) {
        ScheduleData item = mList.get(position);
        holder.tv_name.setText(item.getSchedule_name());
        holder.tv_time.setText(formatDateTimeString(item.getStart()) + "에서\n" +formatDateTimeString(item.getEnd()) + "까지");
        holder.tv_location.setText(item.getLocation());
        holder.tv_memo.setText(item.getMemo());
        holder.cv_schedule.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, EditScheduleActivity.class);
            if (item.getShare() > 0)
                intent.putExtra("share", item.getShare());
            intent.putExtra("number", item.getNumber());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private String formatDateTimeString(String string){
        LocalDateTime dateTime = LocalDateTime.parse(string, DateTimeFormatter.ofPattern(DateUtils.dateTimeFormat));
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일 (E) HH:mm", Locale.KOREAN));
    }

}
