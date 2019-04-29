package com.example.mwt.db.bmiRecordsdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bmi_records")
data class BMIRecordEntity(
        val bmi: Float,
        val date: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
