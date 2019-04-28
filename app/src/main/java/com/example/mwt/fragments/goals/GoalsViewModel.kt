package com.example.mwt.fragments.goals

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mwt.db.achievementdb.AchievementsDao
import com.example.mwt.db.achievementdb.AchievementsEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class GoalsViewModel(private val achievementsDao: AchievementsDao) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default


    fun getAllPosts(): LiveData<List<AchievementsEntity>> {
        return achievementsDao.findAll()
    }

    fun savePost(achievementsEntity: AchievementsEntity) {
        launch{ achievementsDao.save(achievementsEntity) }
    }

    fun deletePost(achievementsEntity: AchievementsEntity) {
        launch{ achievementsDao.delete(achievementsEntity) }
    }
}
