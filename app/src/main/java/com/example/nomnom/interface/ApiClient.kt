package com.example.nomnom.`interface`

import com.example.nomnom.interceptor.OAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://platform.fatsecret.com/"
    private const val CONSUMER_KEY = "ee5eb44ffc054ba18a8667068d1c6280"
    private const val CONSUMER_SECRET = "545357baca0b42a8a175807d1e4b070f"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val oauthInterceptor = OAuthInterceptor(CONSUMER_KEY, CONSUMER_SECRET)

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(oauthInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()

    val api: FatSecretApiService = retrofit.create(FatSecretApiService::class.java)
}
