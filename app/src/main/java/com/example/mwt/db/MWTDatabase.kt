package com.example.mwt.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mwt.db.containerdb.ContainerDao
import com.example.mwt.db.containerdb.ContainersEntity
import java.util.concurrent.Executors
import kotlin.concurrent.thread


@Database(entities = [ContainersEntity::class], version = 1, exportSchema = false)
abstract class MWTDatabase : RoomDatabase() {

    abstract fun containerDao(): ContainerDao

    companion object {
        val CONTAINERS: List<ContainersEntity> = listOf(ContainersEntity("Glass", 250), ContainersEntity("Water Bottle", 500))
    }
}
