package com.example.mwt.db.dateprogressdb

import androidx.lifecycle.LiveData
import androidx.room.*

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

    @Query("SELECT * FROM dateProgress WHERE itemType IN (:type) ORDER BY id DESC")
    fun findAll(type: String): LiveData<List<DateProgressEntity>>

    @Query("DELETE FROM dateProgress WHERE itemType IN (:type)")
    fun deleteAll(type: String)

}
