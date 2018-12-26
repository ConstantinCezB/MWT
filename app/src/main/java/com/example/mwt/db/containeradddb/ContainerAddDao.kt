package com.example.mwt.db.containeradddb

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mwt.db.containerdb.ContainersEntity



@Dao
interface ContainerAddDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveAll(containers: List<ContainersAddEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(container: ContainersAddEntity)

    @Update
    fun update(container: ContainersAddEntity)

    @Delete
    fun delete(container: ContainersAddEntity)

    @Query("SELECT * FROM containers_add ORDER BY id DESC")
    fun findAll(): LiveData<List<ContainersAddEntity>>
}
