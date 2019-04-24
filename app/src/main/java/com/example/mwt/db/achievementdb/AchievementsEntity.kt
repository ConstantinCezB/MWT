package com.example.mwt.db.achievementdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementsEntity(
        val name: String,
        var amount: Int,
        val size: Int,
        val date: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}
