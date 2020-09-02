package com.example.gyrodatasakhalin

import com.example.gyrodatasakhalin.battery.BatteryItem
import com.example.gyrodatasakhalin.utils.AppPreferences
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

var API_KEY = ""
var BSERIALS = ArrayList<String>()

class RetrofitInstance {
    companion object{

        // Base URL
        val BASE_URL = "http://demotestapi.cloudapp.net/"

        // Interceptor
        val interceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor{ chain ->
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