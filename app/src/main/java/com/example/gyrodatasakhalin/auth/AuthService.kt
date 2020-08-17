package com.example.gyrodatasakhalin.auth

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.POST
import retrofit2.http.Part

interface AuthService {

    @POST("/AuthServices/AuthService.svc/authenticate")
    suspend fun getToken(@Body authModel : AuthModel) : Response<String>
}