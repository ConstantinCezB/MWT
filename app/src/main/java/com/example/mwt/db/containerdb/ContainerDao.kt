package com.example.mwt.db.containerdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Update

@Dao
interface ContainerDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAll(containers: List<ContainersEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(container: ContainersEntity)

    @Update
    fun update(container: ContainersEntity)

    @Delete
    fun delete(container: ContainersEntity)

    @Query("SELECT * FROM containers")
    fun findAll(): LiveData<List<ContainersEntity>>

    @Query("SELECT * FROM containers WHERE favorite = 1 ORDER BY favPosition ASC")
    fun findFavoriteContainers() : LiveData<List<ContainersEntity>>

    @Query("SELECT * FROM containers WHERE favorite = 0 ORDER BY id ASC")
    fun findContainers(): LiveData<List<ContainersEntity>>
}
