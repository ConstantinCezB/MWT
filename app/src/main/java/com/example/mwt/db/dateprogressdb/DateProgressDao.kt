package com.example.mwt.db.dateprogressdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Update

@Dao
interface DateProgressDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAll(dateProgresses: List<DateProgressEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(dateProgress: DateProgressEntity)

    @Update
    fun update(dateProgress: DateProgressEntity)

    @Delete
    fun delete(dateProgress: DateProgressEntity)

    @Query("SELECT * FROM dateProgress")
    fun findAll(): LiveData<List<DateProgressEntity>>
}
