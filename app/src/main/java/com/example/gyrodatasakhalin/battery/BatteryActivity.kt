package com.example.gyrodatasakhalin.battery

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
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
import kotlinx.android.synthetic.main.progress_bar.*
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

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

        edSearch.addTextChangedListener(object : TextWatcher{

            var where: String = ""

            override fun afterTextChanged(searchQuery: Editable?) {

                if (rbSerialButton.isChecked){where = "Serial"}
                if (rbStatusButton.isChecked){where = "Status"}
                if (rbCCDButton.isChecked){where = "CCD"}
                if (rbInvoiceButton.isChecked){where = "Invoice"}

                Handler().postDelayed({
                    search(searchQuery, where)
                }, 1500)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            private fun search(editable: Editable?, searchWhere: String){
                var searchWhat : String = edSearch.text.toString()
                getBatteries(API_KEY, editable.toString(), searchWhere)
            }

        })
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
            API_KEY = token
            Log.i("BATTERY", API_KEY)
            getBatteries(API_KEY, "", "")
        })
        return token
    }

    private fun getBatteries(token: String, what: String, where: String){

        pbWaiting.bringToFront()
        pbWaiting.visibility = View.VISIBLE

        val responseLiveData : LiveData<Response<Battery>> = liveData {
            val response = retService.getCustomBatteries(what, where)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val batteryList = it.body()?.listIterator()
            var batteries = ArrayList<BatteryItem>()
            if (batteryList != null){

                if (pbWaiting != null && pbWaiting.isShown){
                    pbWaiting.visibility = View.GONE
                }

                while (batteryList.hasNext()){
                    val batteryItem = batteryList.next()
                    batteries.add(batteryItem)
                }
                batteryRecyclerView.adapter = BatteryAdapter(batteries, applicationContext)
                (batteryRecyclerView.adapter as BatteryAdapter).notifyDataSetChanged()
                batteryRecyclerView.layoutManager = LinearLayoutManager(this)
                batteryRecyclerView.setHasFixedSize(true)
            }
        })
    }
}