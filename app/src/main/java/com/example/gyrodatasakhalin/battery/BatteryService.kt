package com.example.gyrodatasakhalin.battery

import com.example.gyrodatasakhalin.battery.Battery
import retrofit2.Response
import retrofit2.http.GET

interface BatteryService {

    @GET("/BatteryServices/BatteryService.svc/getAllbatteries")
    suspend fun getBatteries() : Response<Battery>
}