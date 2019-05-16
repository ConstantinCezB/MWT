package com.example.mwt.fragments.drinkingstatistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mwt.db.dateprogressdb.DateProgressDao
import com.example.mwt.db.dateprogressdb.DateProgressEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DrinkingStatisticsViewModel(private val dateProgressDao: DateProgressDao) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default


    fun getAllPosts(type: String): LiveData<List<DateProgressEntity>> {
        return dateProgressDao.findAll(type)
    }

    fun deleteAllPosts(type: String){
        launch { dateProgressDao.deleteAll(type) }
    }

    fun savePost(dateProgress: DateProgressEntity) {
        launch { dateProgressDao.save(dateProgress) }
    }

    fun deletePost(dateProgress: DateProgressEntity) {
        launch { dateProgressDao.delete(dateProgress) }
    }
}
