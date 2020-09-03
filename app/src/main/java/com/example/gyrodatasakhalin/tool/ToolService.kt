package com.example.gyrodatasakhalin.tool

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ToolService {

    @GET("/toolservices/toolservice.svc/GetCustomItems")
    suspend fun getCustomItems(@Query("what") what : String,
                                   @Query("where") where : String ) : Response<Tool>

    @GET("/toolservices/toolservice.svc/GetItemsComponents")
    suspend fun getItemsComponents() : Response<List<String>>

    @POST("/toolservices/toolservice.svc/AddNewItem")
    suspend fun addNewItem(@Body gwdItemToInsert: ToolItem) : Response<Int>
}