package com.example.mwt.db.bmiRecords

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface BMIRecordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAll(BMIRecord: List<BMIRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(BMIRecord: BMIRecordEntity)

    @Update
    fun update(BMIRecord: BMIRecordEntity)

    @Delete
    fun delete(BMIRecord: BMIRecordEntity)

    @Query("SELECT * FROM bmi_records ORDER BY id DESC")
    fun findAll(): LiveData<List<BMIRecordEntity>>

    @Query("DELETE FROM bmi_records")
    fun dropTable()
}
