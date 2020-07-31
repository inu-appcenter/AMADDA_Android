package com.inu.amadda.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.model.ShareGroup;

import java.util.List;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.ViewHolder> {

    private List<ShareGroup> mList;

    public GroupListAdapter(List<ShareGroup> list) {
        this.mList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View view_group_tag;
        TextView tv_group_name;
        LinearLayout ll_group_list;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            view_group_tag = itemView.findViewById(R.id.view_group_tag);
            tv_group_name = itemView.findViewById(R.id.tv_group_name);
            ll_group_list = itemView.findViewById(R.id.ll_group_list);
        }
    }

    @NonNull
    @Override
    public GroupListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupListAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.ViewHolder holder, int position) {
        ShareGroup item = mList.get(position);
//        holder.view_group_tag.setBackgroundResource(R.color.color_ff0000);
        holder.tv_group_name.setText(item.getGroup_name());
        holder.ll_group_list.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
