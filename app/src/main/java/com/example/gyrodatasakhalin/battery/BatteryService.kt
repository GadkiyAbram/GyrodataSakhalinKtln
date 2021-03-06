package com.example.gyrodatasakhalin.battery

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BatteryService {

    @GET("/BatteryServices/BatteryService.svc/getAllbatteries")
    suspend fun getBatteries() : Response<Battery>

    @GET("/BatteryServices/BatteryService.svc/getselectedbatteries")
    suspend fun getCustomBatteries(@Query("what") what : String,
                                   @Query("where") where : String ) : Response<Battery>
}