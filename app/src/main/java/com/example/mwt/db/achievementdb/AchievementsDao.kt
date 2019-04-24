package com.example.mwt.db.achievementdb

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface AchievementsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAll(achievements: List<AchievementsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(achievements: AchievementsEntity)

    @Update
    fun update(achievements: AchievementsEntity)

    @Delete
    fun delete(achievements: AchievementsEntity)

    @Query("SELECT * FROM achievements ORDER BY id DESC")
    fun findAll(): LiveData<List<AchievementsEntity>>

    @Query("DELETE FROM achievements")
    fun dropTable()
}