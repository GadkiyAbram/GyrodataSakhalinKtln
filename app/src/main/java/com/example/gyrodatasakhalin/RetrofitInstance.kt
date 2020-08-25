package com.example.gyrodatasakhalin

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var API_KEY = ""

class RetrofitInstance {
    companion object{

        // Base URL
        val BASE_URL = "http://demotestapi.cloudapp.net/"

        // Interceptor
        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder().addInterceptor{ chain ->
            val original = chain.request()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                .header("Token", API_KEY) // <-- this is the important line

            val request = requestBuilder.build()
            chain.proceed(request)
        }
        val client = okHttpClient.build()


        // Instance of Retrofit
        fun getRetrofitInstance() : Retrofit{
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
        }
    }
}