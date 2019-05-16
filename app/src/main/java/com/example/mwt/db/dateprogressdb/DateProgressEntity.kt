package com.example.mwt.db.dateprogressdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dateProgress")
data class DateProgressEntity(
        val date: String,
        val progress: Int,
        val itemType: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
