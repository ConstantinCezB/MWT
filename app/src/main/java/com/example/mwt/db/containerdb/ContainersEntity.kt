package com.example.mwt.db.containerdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "containers")
data class ContainersEntity(
        val name: String,
        val size: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}


