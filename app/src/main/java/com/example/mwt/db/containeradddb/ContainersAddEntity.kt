package com.example.mwt.db.containeradddb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "containers_add")
data class ContainersAddEntity(
        val name: String,
        val amount: Int,
        val size: Int,
        val date: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
        protected set
}
