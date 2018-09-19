package com.example.mwt.db

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mwt.db.containerdb.ContainerDao
import com.example.mwt.db.containerdb.ContainersEntity
import java.util.concurrent.Executors


@Database(entities = [ContainersEntity::class], version = 1)
abstract class MWTDatabase : RoomDatabase() {

    abstract fun containerDao(): ContainerDao

    companion object {
        // TODO: This down here, sir, is why you need DI.

        private var INSTANCE: MWTDatabase? = null
        private val sLock = Any()
        private val CONTAINERS: List<ContainersEntity> = listOf(ContainersEntity("Glass", 250), ContainersEntity("Water Bottle", 500))

        fun getInstance(context: Context): MWTDatabase {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, MWTDatabase::class.java, "Container.db")
                            .allowMainThreadQueries() // TODO: Please don't
                            .addCallback(object : RoomDatabase.Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)

                                    // FIXME: You shouldn't do this. If you're going to make an executor then you should inject it throughout your whole app.
                                    Executors.newSingleThreadExecutor().execute { getInstance(context).containerDao().saveAll(CONTAINERS) }
                                }
                            }).build()

                }
                return INSTANCE as MWTDatabase
            }
        }
    }
}
