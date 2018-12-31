package com.example.mwt.db.dailylogdb

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface DailyLogDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAll(containers: List<DailyLogEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(container: DailyLogEntity)

    @Update
    fun update(container: DailyLogEntity)

    @Delete
    fun delete(container: DailyLogEntity)

    @Query("SELECT * FROM containers_add ORDER BY id DESC")
    fun findAll(): LiveData<List<DailyLogEntity>>

    @Query("DELETE FROM containers_add")
    fun dropTable()
}
