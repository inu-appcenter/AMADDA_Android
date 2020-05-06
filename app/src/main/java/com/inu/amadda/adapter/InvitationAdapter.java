package com.inu.amadda.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.model.InvitationData;

import java.util.List;

public class InvitationAdapter extends RecyclerView.Adapter<InvitationAdapter.ViewHolder> {

    private List<InvitationData> mList;

    public InvitationAdapter(List<InvitationData> list) {
        this.mList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        View view_group_tag;
        TextView tv_group_name, tv_inviter, tv_memo;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            view_group_tag = itemView.findViewById(R.id.view_group_tag);
            tv_group_name = itemView.findViewById(R.id.tv_group_name);
            tv_inviter = itemView.findViewById(R.id.tv_inviter);
            tv_memo = itemView.findViewById(R.id.tv_memo);
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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
