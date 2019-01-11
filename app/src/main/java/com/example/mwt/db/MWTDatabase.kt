package com.example.mwt.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mwt.db.dailylogdb.DailyLogDao
import com.example.mwt.db.dailylogdb.DailyLogEntity
import com.example.mwt.db.containerdb.ContainerDao
import com.example.mwt.db.containerdb.ContainersEntity
import com.example.mwt.db.dateprogressdb.DateProgressDao
import com.example.mwt.db.dateprogressdb.DateProgressEntity


@Database(entities = [ContainersEntity::class, DateProgressEntity::class, DailyLogEntity::class], version = 1, exportSchema = false)
abstract class MWTDatabase : RoomDatabase() {

    abstract fun containerDao(): ContainerDao

    abstract fun dateProgressDao(): DateProgressDao

    abstract fun dailyLogDao(): DailyLogDao

    companion object {
        val CONTAINERS: List<ContainersEntity> = listOf(ContainersEntity("Glass", 250, favorite = true), ContainersEntity("Water Bottle", 500, favorite = true))
    }
}
