package com.example.mwt.db.dateprogressdb

import com.example.mwt.db.containerdb.ContainersEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Update

@Dao
interface dateProgressDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAll(containers: List<ContainersEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(container: ContainersEntity)

    @Update
    fun update(container: ContainersEntity)

    @Delete
    fun delete(container: ContainersEntity)

    @Query("SELECT * FROM dateProgress")
    fun findAll(): LiveData<List<ContainersEntity>>
}
