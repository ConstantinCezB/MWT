package com.example.mwt.containerdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Update

@Dao
interface ContainerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAll(containers: List<ContainersEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(container: ContainersEntity)

    @Update
    fun update(container: ContainersEntity)

    @Delete
    fun delete(container: ContainersEntity)

    @Query("SELECT * FROM containers")
//    @Query("SELECT * FROM ContainersEntity")
    fun findAll(): LiveData<List<ContainersEntity>>
}
