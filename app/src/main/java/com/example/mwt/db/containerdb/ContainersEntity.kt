package com.example.mwt.db.containerdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "containers")
data class ContainersEntity(
        var name: String,
        var size: Int,
        var favorite: Boolean = false,
        var favPosition: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}


