package com.example.gyrodatasakhalin.fragments.battery

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gyrodatasakhalin.API_KEY
import com.example.gyrodatasakhalin.BSERIALS
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.battery.Battery
import com.example.gyrodatasakhalin.battery.BatteryAdapter
import com.example.gyrodatasakhalin.battery.BatteryItem
import com.example.gyrodatasakhalin.battery.BatteryService
import com.example.gyrodatasakhalin.start.StartActivity
import com.example.gyrodatasakhalin.utils.validation.BatteryValidation
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_battery.*
import kotlinx.android.synthetic.main.activity_battery.*
import kotlinx.android.synthetic.main.battery_condition_switch.*
import kotlinx.android.synthetic.main.progress_bar.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import retrofit2.Response

private lateinit var retService: BatteryService
private lateinit var errorsArray: MutableMap<String, String>

class AddBatteryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_battery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retService = RetrofitInstance.getRetrofitInstance()
            .create(BatteryService::class.java)

        addBatteryButton.setOnClickListener {
            addBattery()
        }
    }

    private fun addBattery(){
        val serialOne = edBatterySerial1.text.toString()
        val serialTwo = edBatterySerial2.text.toString()
        val serialThr = edBatterySerial3.text.toString()
        val batteryCondition = if (toggleConditionSwitch.isChecked) "New" else "Used"
        val batteryArrived = edBatteryArrived.text.toString()
        val batteryStatus = edBatteryLocation.text.toString()
        val batteryInvoice = edBatteryInvoice.text.toString()
        val batteryCCD = edBatteryCCD.text.toString()
        val batteryContainer = edBatteryContainer.text.toString()
        val batteryComment = edBatteryComments.text.toString()
        val batteryBoxNumber = edBatteryBox.text.toString()

        val battery = BatteryItem(
            batteryArrived,
            batteryCondition,
            batteryStatus,
            batteryBoxNumber,
            batteryCCD,
            batteryComment,
            batteryContainer,
            "",
            0,
            batteryInvoice,
            serialOne,
            serialTwo,
            serialThr,
            "",
            false
        )

        if (validateBattery()){

            pbWaiting.bringToFront()
            pbWaiting.visibility = View.VISIBLE

            val responseLiveData : LiveData<Response<Int>> = liveData {
                val response = retService.addBattery(battery)
                emit(response)
            }
            responseLiveData.observe(this@AddBatteryFragment, Observer {
                val result = it.body()
                if(result != null){
                    if (pbWaiting != null && pbWaiting.isShown){
                        pbWaiting.visibility = View.GONE
                        clearEditTextViews()
                    }
                }
                Log.i("RESULT", "Serial 1: ${serialOne}")
                if (pbWaiting != null && pbWaiting.isShown){
                    pbWaiting.visibility = View.GONE
                }
                Toast.makeText(context!!.applicationContext, "Battery ${serialOne} added", Toast.LENGTH_SHORT).show()
            })
        }else{
            var errorBuilder = StringBuffer()

            for(error in errorsArray){
                errorBuilder.append(error.value)
                    .append("\n")
            }

            var errorDialog = AlertDialog.Builder(activity)
            errorDialog.setTitle("Uncertainties")
            errorDialog.setMessage(errorBuilder.toString())
            errorDialog.show()
        }
    }

    private fun clearEditTextViews(){
        edBatterySerial1.text.clear()
        edBatterySerial2.text.clear()
        edBatterySerial3.text.clear()
        edBatteryArrived.text.clear()
        edBatteryLocation.text.clear()
        edBatteryInvoice.text.clear()
        edBatteryCCD.text.clear()
        edBatteryContainer.text.clear()
        edBatteryComments.text.clear()
        edBatteryBox.text.clear()
    }

    private fun validateBattery(): Boolean{
        var granted = true

        errorsArray = mutableMapOf<String, String>()

        val batteryValidation = BatteryValidation()

        if(getBattery(edBatterySerial1.text.toString())){
            errorsArray.put("Exists", "This Battery already in DB")
        }
        

        if (!batteryValidation.checkSerial(edBatterySerial1.text.toString())){
            errorsArray.put("Serial1", "Invalid Serial One")
        }

        if (edBatteryBox.text.toString() == "0"){
            errorsArray.put("Box", "Box Number couldn't be 0")
        }

        if (!edBatteryInvoice.text.isEmpty()){
            if (!batteryValidation.checkInvoice(edBatteryInvoice.text.toString())){
                errorsArray.put("Invoice", "Invalid Invoice")
            }
        }

        if (!edBatteryCCD.text.isEmpty()){
            if (!batteryValidation.checkCcd(edBatteryCCD.text.toString())){
                errorsArray.put("CCD", "Invalid CCD")
            }
        }

        if (!errorsArray.isEmpty()){
            granted = false
        }


        Log.i("VALID", "${errorsArray.size}")
        return granted
    }

    private fun getBattery(what: String) : Boolean{

        var exists = false

        if (BSERIALS.contains(what)){exists = true}

        return exists
    }
}