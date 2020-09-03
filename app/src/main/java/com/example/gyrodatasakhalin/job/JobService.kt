package com.example.gyrodatasakhalin.job

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface JobService {

    @GET("/JobServices/JobService.svc/GetCustomJobData")
    suspend fun getCustomJobs(@Query("what") what : String,
                              @Query("where") where : String) : Response<Job>

    @GET("/JobServices/JobService.svc/GetAllDataForJobCreate")
    suspend fun getAllDataForJobCreate() : Response<List<List<String>>>

    @POST("/JobServices/JobService.svc/AddNewJob")
    suspend fun addNewJob(@Body job: JobItem) : Response<Int>
}