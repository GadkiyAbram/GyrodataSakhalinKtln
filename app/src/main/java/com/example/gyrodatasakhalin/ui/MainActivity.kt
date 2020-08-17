package com.example.gyrodatasakhalin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.gyrodatasakhalin.battery.Battery
import com.example.gyrodatasakhalin.battery.BatteryService
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.auth.AuthModel
import com.example.gyrodatasakhalin.auth.AuthService
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        authService = RetrofitInstance.getRetrofitInstance()
            .create(AuthService::class.java)

        getToken()


//        val responseLiveData : LiveData<Response<Battery>> = liveData {
//            val response = retService.getBatteries()
//            Log.i("BATTERY", response.toString())
//            emit(response)
//        }
//
//        responseLiveData.observe(this, Observer {
//            val batteryList = it.body()?.listIterator()
//            if (batteryList != null){
//                while (batteryList.hasNext()){
//                    val batteryItem = batteryList.next()
//                    Log.i("BATTERY", batteryItem.serialOne)
//                }
//            }
//        })
    }

    private fun getToken(){
        val user = "admin@admin.com"
        val password = "12341234"
        val userToAuth = AuthModel(user, password)
        val postResponse : LiveData<Response<String>> = liveData {
            val response = authService.getToken(userToAuth)
            emit(response)
        }

        postResponse.observe(this, Observer {
            val receivedToken = it.body()
            text_view.text = receivedToken
        })
    }
}