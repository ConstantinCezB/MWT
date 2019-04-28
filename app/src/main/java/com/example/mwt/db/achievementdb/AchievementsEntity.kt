package com.example.mwt.db.achievementdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achievements")
data class AchievementsEntity(
        val itemHeader: String,
        val itemDateValue: String,
        val itemType: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
