package com.example.mwt.db.achievementsdb

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AchievementsDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAll(Achievements: List<AchievementsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(Achievements: AchievementsEntity)

    @Update
    fun update(Achievements: AchievementsEntity)

    @Delete
    fun delete(Achievements: AchievementsEntity)

    @Query("SELECT * FROM achievements ORDER BY id DESC")
    fun findAll(): LiveData<List<AchievementsEntity>>

    @Query("DELETE FROM achievements")
    fun dropTable()
}
