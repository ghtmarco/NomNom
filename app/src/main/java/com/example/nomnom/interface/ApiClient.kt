package com.example.nomnom.`interface`

import com.example.nomnom.BuildConfig
import com.example.nomnom.interceptor.OAuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://platform.fatsecret.com/"
    private val CONSUMER_KEY = BuildConfig.FATSECRET_CONSUMER_KEY
    private val CONSUMER_SECRET = BuildConfig.FATSECRET_CONSUMER_SECRET

    private val logging = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
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
