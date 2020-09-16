package com.inu.amadda.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.inu.amadda.R;
import com.inu.amadda.model.InviteUserData;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InviteAdapter extends RecyclerView.Adapter<InviteAdapter.ViewHolder> {

    private List<InviteUserData> mList;

    public InviteAdapter(List<InviteUserData> list) {
        this.mList = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imageView;
        TextView textView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.iv_invite);
            textView = itemView.findViewById(R.id.tv_invite);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InviteAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InviteUserData item = mList.get(position);
        holder.textView.setText(item.getId());
        Glide.with(holder.imageView.getContext()).load(item.getPath())
                .thumbnail(0.1f)
                .error(R.drawable.group_profile)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
