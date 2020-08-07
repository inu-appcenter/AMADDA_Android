package com.inu.amadda.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.database.ShareGroup;

import java.util.List;

public class GroupChoiceAdapter extends RecyclerView.Adapter<GroupChoiceAdapter.ViewHolder> {

    private List<ShareGroup> mList;

    public GroupChoiceAdapter(List<ShareGroup> list) {
        this.mList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View view_group_tag;
        TextView tv_group_name;
        CheckBox cb_group_choice;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            view_group_tag = itemView.findViewById(R.id.view_group_tag);
            tv_group_name = itemView.findViewById(R.id.tv_group_name);
            cb_group_choice = itemView.findViewById(R.id.cb_group_choice);
        }
    }

    @NonNull
    @Override
    public GroupChoiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupChoiceAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_choice, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupChoiceAdapter.ViewHolder holder, int position) {
        ShareGroup item = mList.get(position);
        holder.view_group_tag.setBackgroundColor(Color.parseColor(item.getColor()));
        holder.tv_group_name.setText(item.getGroup_name());
        holder.cb_group_choice.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
