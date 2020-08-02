package com.inu.amadda.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ShareGroup {
    @PrimaryKey
    public int share;
    public String group_name;
    public String memo;
    public int color;

    public int getShare() {
        return share;
    }

    public void setShare(int share) {
        this.share = share;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public ShareGroup(int share, String group_name, String memo, int color) {
        this.share = share;
        this.group_name = group_name;
        this.memo = memo;
        this.color = color;
    }
}
