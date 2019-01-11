package com.example.mwt.db.dailylogdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "containers_add")
data class DailyLogEntity(
        val name: String,
        var amount: Int,
        val size: Int,
        val date: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}
