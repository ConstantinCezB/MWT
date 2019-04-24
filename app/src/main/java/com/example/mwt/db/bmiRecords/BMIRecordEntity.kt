package com.example.mwt.db.bmiRecords

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bmi_records")
data class BMIRecordEntity(
        val name: String,
        var amount: Int,
        val size: Int,
        val date: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

}
