package com.nirmal.baby.hovverification.network

import com.nirmal.baby.hovverification.features.hovVerification.entity.FaceResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {
    @Multipart
    @POST("facepp/v3/detect")
    @Headers(
        "Postman-Token: calculated-uuid",
        "User-Agent: PostmanRuntime/7.42.0",
        "Accept: */*",
        "Accept-Encoding: gzip, deflate, br",
        "Connection: keep-alive"
    )
    suspend fun detectFaces(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>, // For text parameters
        @Part imageFile: MultipartBody.Part // For the image
    ): FaceResponse

    companion object {
        private const val BASE_URL = "https://api-us.faceplusplus.com/"

        fun create(): ApiService {
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(ApiService::class.java)
        }
    }
}

