package com.example.mwt.fragments.tracker

import com.example.mwt.db.containerdb.ContainerDao
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mwt.db.containerdb.ContainersEntity
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.CoroutineContext


class TrackerViewModel(private val containerDao: ContainerDao) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default


    fun getAllPosts(): LiveData<List<ContainersEntity>> {
        return containerDao.findAll()
    }

    fun savePost(container: ContainersEntity) {
        launch{ containerDao.save(container) }
    }

    fun deletePost(container: ContainersEntity) {
        launch{ containerDao.delete(container) }
    }
}
