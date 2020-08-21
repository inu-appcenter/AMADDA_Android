package com.inu.amadda.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inu.amadda.R;
import com.inu.amadda.model.ClassData;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    private List<ClassData> mList;
    private Context mContext;

    private OnSelectListener mOnSelectListener;

    public interface OnSelectListener{
        void onSelect(ClassData data);
    }

    public ClassAdapter(List<ClassData> list, OnSelectListener onSelectListener) {
        this.mList = list;
        this.mOnSelectListener = onSelectListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout rl_class;
//        LinearLayout ll_second;
        TextView tv_name, tv_professor, tv_day, tv_time_place;
//        TextView tv_day2, tv_time_place2;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            rl_class = itemView.findViewById(R.id.rl_class);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_professor = itemView.findViewById(R.id.tv_professor);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_time_place = itemView.findViewById(R.id.tv_time_place);
//            ll_second = itemView.findViewById(R.id.ll_second);
//            tv_day2 = itemView.findViewById(R.id.tv_day2);
//            tv_time_place2 = itemView.findViewById(R.id.tv_time_place2);

        }
    }

    @NonNull
    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ClassAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_class, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClassAdapter.ViewHolder holder, int position) {
        ClassData item = mList.get(position);
        holder.tv_name.setText(item.getLecture());
        holder.tv_professor.setText(item.getProfessor());
        holder.tv_day.setText(item.getDay());
        holder.tv_time_place.setText(item.getStart() + "~" + item.getEnd() + " (" + item.getRoom() + ")");
        holder.rl_class.setOnClickListener(view -> {
            mOnSelectListener.onSelect(item);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

}
