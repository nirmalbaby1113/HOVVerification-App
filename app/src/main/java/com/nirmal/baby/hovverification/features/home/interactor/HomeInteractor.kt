package com.nirmal.baby.hovverification.features.home.interactor

import com.nirmal.baby.hovverification.database.FaceDataDao
import com.nirmal.baby.hovverification.features.home.entity.HomeEntity

class HomeInteractor(private val faceDataDao: FaceDataDao) {
    suspend fun fetchFaceData(): List<HomeEntity> {
        val faceDataList = faceDataDao.getAllFaceData()
        return faceDataList.map {
            HomeEntity(
                requestId = it.requestId,
                faceCount = it.faceCount,
                timestamp = it.timestamp
            )
        }
    }
}