package com.example.mwt.db.dateprogressdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dateProgress")
data class dateProgressEntity(
        val date: String,
        val progress: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
        protected set
}
