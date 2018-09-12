package com.example.mwt.fragments.tracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mwt.containerdb.ContainerDao
import com.example.mwt.containerdb.ContainerDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import androidx.lifecycle.LiveData
import com.example.mwt.containerdb.ContainersEntity


class TrackerViewModel(application: Application) : AndroidViewModel(application) {

    private val containerDao: ContainerDao = ContainerDatabase.getInstance(application).containerDao()
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()


    fun getAllPosts(): LiveData<List<ContainersEntity>> {
        return containerDao.findAll()
    }

    fun savePost(container: ContainersEntity) {
        executorService.execute { containerDao.save(container) }
    }

    fun deletePost(container: ContainersEntity) {
        executorService.execute { containerDao.delete(container) }
    }
}
