package com.example.mwt.fragments.drinkingstatistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mwt.db.dateprogressdb.DateProgressDao
import com.example.mwt.db.dateprogressdb.DateProgressEntity
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext

class DrinkingStatisticsViewModel(private val dateProgressDao: DateProgressDao) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default


    fun getAllPosts(): LiveData<List<DateProgressEntity>> {
        return dateProgressDao.findAll()
    }

    fun savePost(dateProgress: DateProgressEntity) {
        launch{ dateProgressDao.save(dateProgress) }
    }

    fun deletePost(dateProgress: DateProgressEntity) {
        launch{ dateProgressDao.delete(dateProgress) }
    }
}
