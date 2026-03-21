package com.example.ads

import retrofit2.http.GET
import retrofit2.http.Url

// 使用 Retrofit 调用 Worker（Android 常用网络库）
interface NetworkApiService {
    @GET
    suspend fun getMediaData(@Url url: String): NetworkResponse
}