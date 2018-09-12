package com.example.mwt.containerdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "containers")
data class ContainersEntity(
        val name: String,
        val size: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
        protected set
}


