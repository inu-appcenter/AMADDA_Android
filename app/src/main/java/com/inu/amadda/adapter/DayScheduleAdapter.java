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
import com.inu.amadda.model.DaySchedule;
import com.inu.amadda.timetable.Schedule;
import com.inu.amadda.timetable.Time;
import com.inu.amadda.view.activity.EditClassActivity;
import com.inu.amadda.view.activity.EditScheduleActivity;

import java.util.List;

public class DayScheduleAdapter extends RecyclerView.Adapter<DayScheduleAdapter.ViewHolder> {

    private List<DaySchedule> mList;
    private Context mContext;

    public DayScheduleAdapter(List<DaySchedule> list) {
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
    public DayScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new DayScheduleAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_schedule_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DayScheduleAdapter.ViewHolder holder, int position) {
        DaySchedule item = mList.get(position);

        if (item.getColor() != null)
            holder.view_group_tag.setBackgroundColor(Color.parseColor(item.getColor()));

        holder.tv_name.setText(item.getSchedule_name());

        holder.tv_time.setText(item.getStart() + " ~ " + item.getEnd());

        holder.tv_location.setText(item.getLocation());

        if (item.getMemo() == null || item.getMemo().length() == 0) {
            holder.tv_memo.setVisibility(View.GONE);
        }
        else {
            holder.tv_memo.setText(item.getMemo());
        }

        holder.cv_schedule.setOnClickListener(view -> {
            if (item.isSchedule()) {
                Intent intent = new Intent(mContext, EditScheduleActivity.class);
                if (item.getShare() > 0)
                    intent.putExtra("share", item.getShare());
                intent.putExtra("number", item.getNumber());
                mContext.startActivity(intent);
            }
            else {
                Intent intent = new Intent(mContext, EditClassActivity.class);
                intent.putExtra("class", item.getClassData());
                intent.putExtra("idx", item.getNumber());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
