package com.example.gyrodatasakhalin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonToken
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.gyrodatasakhalin.API_KEY
import com.example.gyrodatasakhalin.battery.Battery
import com.example.gyrodatasakhalin.battery.BatteryService
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.auth.AuthModel
import com.example.gyrodatasakhalin.auth.AuthService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var authService: AuthService
    private lateinit var retService: BatteryService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retService = RetrofitInstance.getRetrofitInstance()
            .create(BatteryService::class.java)
//
        authService = RetrofitInstance.getRetrofitInstance()
            .create(AuthService::class.java)

//        getToken()
//            apiRequests()


    }

    private fun apiRequests(){
        CoroutineScope(Main).launch {
            async {
                getToken()
                getBatteries(API_KEY)
            }.await()
        }
    }

    private fun getToken() : String{
        var token = ""

        val user = "admin@admin.com"
        val password = "12341234"
        val userToAuth = AuthModel(user, password)
        val postResponse : LiveData<Response<String>> = liveData {
            val response = authService.getToken(userToAuth)
            emit(response)
        }

        postResponse.observe(this, Observer {
            val receivedToken = it.body()
            token = receivedToken.toString()
            text_view.text = token
            API_KEY = token
            Log.i("MainActivity", API_KEY)
            getBatteries(API_KEY)
        })
        return token

    }

    private fun getBatteries(token: String){
        val responseLiveData : LiveData<Response<Battery>> = liveData {
            val response = retService.getCustomBatteries("", "")
//            Log.i("BATTERY", response.toString())
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val batteryList = it.body()?.listIterator()
            if (batteryList != null){
                while (batteryList.hasNext()){
                    val batteryItem = batteryList.next()
                    Log.i("MainActivity", batteryItem.serialOne)
                }
            }
        })
    }
}