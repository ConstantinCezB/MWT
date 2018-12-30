package com.example.mwt.db.containerdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "containers")
data class ContainersEntity(
        val name: String,
        val size: Int,
        val favorite: Boolean = false,
        val favPosition: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}


