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

//    @Query("SELECT * FROM ShareGroup WHERE idx = :idx")
//    LiveData<ShareGroup> getByIdx(int idx);
//
//    @Query("DELETE FROM ShareGroup WHERE idx = :idx")
//    void deleteByIdx(int idx);

    @Update
    void update(ShareGroup group);

    @Insert
    void insert(ShareGroup group);
}
