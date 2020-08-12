package com.inu.amadda.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.model.ScheduleData;

import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {

    private List<ScheduleData> mList;
    private Context mContext;

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
        return new ScheduleListAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_schedule_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleListAdapter.ViewHolder holder, int position) {
        ScheduleData item = mList.get(position);
//        holder.view_group_tag.setBackgroundColor(Color.parseColor("#ffffff"));
        holder.tv_name.setText(item.getSchedule_name());
//        holder.tv_time.setText(item.getStart() + " 안녕");
        holder.tv_location.setText(item.getLocation());
        holder.tv_memo.setText(item.getMemo());
        holder.cv_schedule.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
