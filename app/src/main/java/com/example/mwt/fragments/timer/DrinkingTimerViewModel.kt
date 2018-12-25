package com.example.mwt.fragments.timer

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mwt.db.containeradddb.ContainerAddDao
import com.example.mwt.db.containeradddb.ContainersAddEntity
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext


class DrinkingTimerViewModel(private val containerAddDao: ContainerAddDao) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default


    fun getAllPosts(): LiveData<List<ContainersAddEntity>> {
        return containerAddDao.findAll()
    }

    fun savePost(containerAdd: ContainersAddEntity) {
        launch{ containerAddDao.save(containerAdd) }
    }

    fun deletePost(containerAdd: ContainersAddEntity) {
        launch{ containerAddDao.delete(containerAdd) }
    }
}