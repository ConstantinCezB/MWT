package com.example.mwt

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mwt.db.MWTDatabase
import com.example.mwt.fragments.tracker.TrackerViewModel
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val MWTModule = module {
    single {
        Room.databaseBuilder(androidContext(), MWTDatabase::class.java, "Container.db")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        GlobalScope.launch {
                            get<MWTDatabase>().containerDao().saveAll(MWTDatabase.CONTAINERS)
                        }
                    }
                })
                .build()
    }

    single {
        get<MWTDatabase>().containerDao()
    }

    viewModel {
        TrackerViewModel(get())
    }
}
