package com.example.mwt.fragments.bmi

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mwt.db.bmiRecordsdb.BMIRecordDao
import com.example.mwt.db.bmiRecordsdb.BMIRecordEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class BMIViewModel(private val bmiRecordDao: BMIRecordDao) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default


    fun getAllPosts(): LiveData<List<BMIRecordEntity>> {
        return bmiRecordDao.findAll()
    }

    fun savePost(bmiRecordEntity: BMIRecordEntity) {
        launch { bmiRecordDao.save(bmiRecordEntity) }
    }

    fun deletePost(bmiRecordEntity: BMIRecordEntity) {
        launch { bmiRecordDao.delete(bmiRecordEntity) }
    }
}