package com.nirmal.baby.hovverification.features.hovVerification.interactor


import com.nirmal.baby.hovverification.database.AppDatabase
import com.nirmal.baby.hovverification.database.FaceDataEntity
import com.nirmal.baby.hovverification.features.hovVerification.entity.FaceResponse
import com.nirmal.baby.hovverification.network.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class HOVInteractor(private val apiService: ApiService, private val database: AppDatabase) {
    suspend fun fetchFaceData(imageFile: File): FaceResponse {
        // Convert text parameters to RequestBody with text/plain media type
        val params = mapOf(
            "api_key" to "v_pvPtPTUrLsKY5ntgenpa7vGz6foCol".toRequestBody("text/plain".toMediaType()),
            "api_secret" to "z630MZDZc2FfDBLDxHsG9eneya8AthG3".toRequestBody("text/plain".toMediaType()),
            "return_landmark" to "1".toRequestBody("text/plain".toMediaType()),
            "return_attributes" to "gender,age".toRequestBody("text/plain".toMediaType())
        )

        // Convert the image file to MultipartBody.Part with correct MIME type
        val imagePart = MultipartBody.Part.createFormData(
            "image_file", imageFile.name, imageFile.asRequestBody("image/jpeg".toMediaType())
        )

        return try {
            val response = apiService.detectFaces(params = params, imageFile = imagePart)

            // Save the response locally
            saveFaceDataLocally(response)

            // Return the response to the caller
            return response
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            throw Exception("HTTP ${e.code()} - ${e.message()} - ${errorBody ?: "No error details"}")
        }
    }

    private suspend fun saveFaceDataLocally(response: FaceResponse) {
        val faceData = FaceDataEntity(
            requestId = response.request_id,
            faceCount = response.face_num,
            timestamp = System.currentTimeMillis()
        )
        database.faceDataDao().insertFaceData(faceData)
    }
}