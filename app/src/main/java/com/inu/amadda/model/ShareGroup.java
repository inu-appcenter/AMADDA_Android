package com.inu.amadda.model;

public class ShareGroup {
    public int share;
    public String group_name;
    public String memo;

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

    public ShareGroup(int share, String group_name, String memo) {
        this.share = share;
        this.group_name = group_name;
        this.memo = memo;
    }
}
