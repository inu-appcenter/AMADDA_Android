package com.inu.amadda.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.database.ShareGroup;
import com.inu.amadda.view.activity.ScheduleListActivity;

import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

    private List<ShareGroup> mList;
    private Context mContext;

    public GroupListAdapter(List<ShareGroup> list) {
        this.mList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View view_group_tag;
        TextView tv_group_name;
        RelativeLayout rl_group_list;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            view_group_tag = itemView.findViewById(R.id.view_group_tag);
            tv_group_name = itemView.findViewById(R.id.tv_group_name);
            rl_group_list = itemView.findViewById(R.id.rl_group_list);
        }
    }

    @NonNull
    @Override
    public GroupListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new GroupListAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_group_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.ViewHolder holder, int position) {
        ShareGroup item = mList.get(position);
        holder.view_group_tag.setBackgroundColor(Color.parseColor(item.getColor()));
        holder.tv_group_name.setText(item.getGroup_name());
        holder.rl_group_list.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, ScheduleListActivity.class);
            intent.putExtra("share", item.getShare());
            intent.putExtra("group_name", item.getGroup_name());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
