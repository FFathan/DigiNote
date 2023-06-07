package com.example.capstoneprojectm3.apihandler

import com.example.capstoneprojectm3.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private const val BASE_URL : String = "https://story-api.dicoding.dev/v1/"
        fun getApiService(): ApiService {
            val loggingInterceptor = getLoggingInterceptor()

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = getRetrofit(client, BASE_URL)
            return retrofit.create(ApiService::class.java)
        }

        fun getApiService(authToken: String): ApiService {
            val loggingInterceptor = getLoggingInterceptor()

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request()
                    val requestHeaders = request.newBuilder()
                        .addHeader("Authorization", "Bearer $authToken")
                        .build()
                    chain.proceed(requestHeaders)
                })
                .build()

            val retrofit = getRetrofit(client, BASE_URL)
            return retrofit.create(ApiService::class.java)
        }

        private fun getLoggingInterceptor(): HttpLoggingInterceptor {
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            return loggingInterceptor
        }

        private fun getRetrofit(client: OkHttpClient, baseUrl: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }
    }
}