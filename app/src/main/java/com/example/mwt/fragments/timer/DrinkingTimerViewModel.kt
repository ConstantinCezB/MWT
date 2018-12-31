package com.example.mwt.fragments.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mwt.db.dailylogdb.DailyLogDao
import com.example.mwt.db.dailylogdb.DailyLogEntity
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext


class DrinkingTimerViewModel(private val dailyLogDao: DailyLogDao) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default


    fun getAllPosts(): LiveData<List<DailyLogEntity>> {
        return dailyLogDao.findAll()
    }

    fun savePost(containerAdd: DailyLogEntity) {
        launch{ dailyLogDao.save(containerAdd) }
    }

    fun deletePost(containerAdd: DailyLogEntity) {
        launch{ dailyLogDao.delete(containerAdd) }
    }
}