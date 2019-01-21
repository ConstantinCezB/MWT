package com.example.mwt.fragments.tracker

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mwt.db.containerdb.ContainerDao
import com.example.mwt.db.containerdb.ContainersEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext


class TrackerViewModel(private val containerDao: ContainerDao) : ViewModel(), CoroutineScope {

    var progress = 100

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default


    fun getAllPosts(): LiveData<List<ContainersEntity>> {
        return containerDao.findAll()
    }

    fun getFavoriteContainers(): LiveData<List<ContainersEntity>> {
        return containerDao.findFavoriteContainers()
    }

    fun getNonFavoriteContainers(): LiveData<List<ContainersEntity>> {
        return containerDao.findContainers()
    }

    fun savePost(container: ContainersEntity) {
        launch{ containerDao.save(container) }
    }

    fun deletePost(container: ContainersEntity) {
        launch{ containerDao.delete(container) }
    }

    fun updatePost(container: ContainersEntity){
        launch{ containerDao.update(container) }
    }
}
