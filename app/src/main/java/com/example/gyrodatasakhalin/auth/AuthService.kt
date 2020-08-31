package com.example.gyrodatasakhalin.auth

import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    @POST("/AuthServices/AuthService.svc/authenticate")
    suspend fun getToken(@Body authModel : AuthModel) : Response<String>

    @POST("/AuthServices/AuthService.svc/TestToken")
    suspend fun testToken(@Body token: String) : Response<Boolean>
}