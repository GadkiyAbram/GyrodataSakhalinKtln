package com.example.gyrodatasakhalin.start

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.observe
import com.example.gyrodatasakhalin.API_KEY
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.auth.AuthModel
import com.example.gyrodatasakhalin.auth.AuthService
import com.example.gyrodatasakhalin.battery.BatteryActivity
import com.example.gyrodatasakhalin.ui.MainActivity
import com.example.gyrodatasakhalin.utils.AppPreferences
import kotlinx.android.synthetic.main.activity_start.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import retrofit2.Response

class StartActivity : AppCompatActivity() {

    private lateinit var authService: AuthService
    private lateinit var preferences: AppPreferences
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        preferences = AppPreferences(this)
        token = preferences.getToken().toString()
        Log.i("TOKEN", token)

        authService = RetrofitInstance.getRetrofitInstance()
            .create(AuthService::class.java)


        val validated = validateToken()
        Log.i("LOGGING", "from OnCreate ${validated.toString()}")

//        getToken()
        btnLogin.setOnClickListener {

            if (validated){
                startMainActivity()
                Log.i("LOGGING", "Starting MainActivity")
            }else{
                getToken()
                Log.i("LOGGING", "Requesting New Token")
            }
        }
    }

    private fun validateToken(): Boolean {

        var result: Boolean = false

        val postResponse : LiveData<Response<Boolean>> = liveData {
            val response = authService.testToken(token)
            emit(response)
        }
        postResponse.observe(this, Observer {
            result = it.body()!!
            Log.i("LOGGING", "From validateToken ${result.toString()}")
        })
        
        return result
    }

    private fun getToken(){

        pbHorizontal.bringToFront()
        pbHorizontal.visibility = View.VISIBLE

//        var token = ""

        val user = etUserLogin.text.toString()
        val password = etUserPassword.text.toString()
        val userToAuth = AuthModel(user, password)
        val postResponse : LiveData<Response<String>> = liveData {
            val response = authService.getToken(userToAuth)
            emit(response)
        }

        postResponse.observe(this, Observer {
            val receivedToken = it.body()
            if (receivedToken != null){
                token = receivedToken.toString()
                preferences.setToken(token!!)
                API_KEY = preferences.getToken().toString()
                Log.i("API_KEY", API_KEY)
                startMainActivity()
            }
        })
    }

    private fun startMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        if (pbHorizontal.visibility == View.VISIBLE){
            pbHorizontal.visibility = View.GONE
            startActivity(intent)
        }
    }
}