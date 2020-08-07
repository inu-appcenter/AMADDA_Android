package com.inu.amadda.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ShareGroupDao {
    @Query("SELECT * FROM ShareGroup ORDER BY share")
    LiveData<List<ShareGroup>> getAll();

    @Query("SELECT * FROM ShareGroup ORDER BY share")
    List<ShareGroup> getList();

    @Query("DELETE FROM ShareGroup WHERE share = :share")
    void deleteByKey(int share);

    @Update
    void update(ShareGroup group);

    @Insert
    void insert(ShareGroup group);
}
