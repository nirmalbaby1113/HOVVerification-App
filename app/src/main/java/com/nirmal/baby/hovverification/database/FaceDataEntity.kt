package com.nirmal.baby.hovverification.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "face_data")
data class FaceDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val requestId: String,
    val faceCount: Int,
    val timestamp: Long
)
