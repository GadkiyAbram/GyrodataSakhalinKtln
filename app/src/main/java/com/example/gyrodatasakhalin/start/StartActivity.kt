package com.example.gyrodatasakhalin.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.gyrodatasakhalin.API_KEY
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.auth.AuthModel
import com.example.gyrodatasakhalin.auth.AuthService
import com.example.gyrodatasakhalin.battery.BatteryActivity
import kotlinx.android.synthetic.main.activity_start.*
import retrofit2.Response

class StartActivity : AppCompatActivity() {

    private lateinit var authService: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        authService = RetrofitInstance.getRetrofitInstance()
            .create(AuthService::class.java)

//        getToken()
        btnLogin.setOnClickListener {
            getToken()
        }
    }

    private fun getToken(){

        pbHorizontal.bringToFront()
        pbHorizontal.visibility = View.VISIBLE

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
            if (receivedToken != null){
                token = receivedToken.toString()
                API_KEY = token
                Log.i("BATTERY", API_KEY)
                val intent = Intent(this, BatteryActivity::class.java)
                if (pbHorizontal.visibility == View.VISIBLE){
                    pbHorizontal.visibility = View.GONE
                    startActivity(intent)
                }
            }
        })

    }
}