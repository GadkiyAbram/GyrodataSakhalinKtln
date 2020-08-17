package com.example.gyrodatasakhalin.battery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gyrodatasakhalin.API_KEY
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.auth.AuthModel
import com.example.gyrodatasakhalin.auth.AuthService
import kotlinx.android.synthetic.main.activity_battery.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

class BatteryActivity : AppCompatActivity() {

    private lateinit var authService: AuthService
    private lateinit var retService: BatteryService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_battery)

        retService = RetrofitInstance.getRetrofitInstance()
            .create(BatteryService::class.java)

        authService = RetrofitInstance.getRetrofitInstance()
            .create(AuthService::class.java)

        getToken()
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
//            text_view.text = token
            API_KEY = token
            Log.i("BATTERY", API_KEY)
            getBatteries(API_KEY)
        })
        return token

    }

    private fun getBatteries(token: String){
        val responseLiveData : LiveData<Response<Battery>> = liveData {
            val response = retService.getCustomBatteries("", "")
            Log.i("BATTERY", response.toString())
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val batteryList = it.body()?.listIterator()
            var batteries = ArrayList<BatteryItem>()
            if (batteryList != null){
                while (batteryList.hasNext()){
                    val batteryItem = batteryList.next()
                    batteries.add(batteryItem)
                    Log.i("BATTERY", batteryItem.serialOne)
                }
                batteryRecyclerView.adapter = BatteryAdapter(batteries)
                batteryRecyclerView.layoutManager = LinearLayoutManager(this)
                batteryRecyclerView.setHasFixedSize(true)
            }
        })
    }
}