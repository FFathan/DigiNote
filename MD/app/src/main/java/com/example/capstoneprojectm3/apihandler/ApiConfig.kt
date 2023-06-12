package com.example.capstoneprojectm3.apihandler

import com.example.capstoneprojectm3.BuildConfig
import com.example.capstoneprojectm3.apihandler.mock.MockApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private const val BASE_URL : String = "https://backend5-dot-diginote-final.et.r.appspot.com/"
        fun getApiService(): ApiService {
            val loggingInterceptor = getLoggingInterceptor()

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = getRetrofit(client, BASE_URL)
            return retrofit.create(ApiService::class.java)
        }
        fun mockGetApiService(): MockApiService {
            return MockApiService()
        }
        fun mockGetApiService(authToken: String): MockApiService {
            return MockApiService()
        }

        fun getApiService(authToken: String): ApiService {
            val loggingInterceptor = getLoggingInterceptor()

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request()
                    val requestHeaders = request.newBuilder()
                        .addHeader("Authorization", authToken)
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