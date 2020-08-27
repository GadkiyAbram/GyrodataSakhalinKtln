package com.example.gyrodatasakhalin.tool

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ToolService {

    @GET("/toolservices/toolservice.svc/GetCustomItems")
    suspend fun getCustomItems(@Query("what") what : String,
                                   @Query("where") where : String ) : Response<Tool>
}