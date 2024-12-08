package com.nirmal.baby.hovverification.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FaceDataDao {
    @Insert
    suspend fun insertFaceData(faceData: FaceDataEntity)

    @Query("SELECT * FROM face_data ORDER BY timestamp DESC")
    suspend fun getAllFaceData(): List<FaceDataEntity>
}