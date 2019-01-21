package com.example.mwt.fragments.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mwt.db.dailylogdb.DailyLogDao
import com.example.mwt.db.dailylogdb.DailyLogEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


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

    fun updatePost(containerAdd: DailyLogEntity){
        launch { dailyLogDao.update(containerAdd) }
    }
}