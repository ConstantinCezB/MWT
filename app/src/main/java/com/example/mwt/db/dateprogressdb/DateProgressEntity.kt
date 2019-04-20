package com.example.mwt.db.dateprogressdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dateProgress")
data class DateProgressEntity(
        val date: String,
        val progress: Float
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
