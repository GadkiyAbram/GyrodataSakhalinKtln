package com.example.gyrodatasakhalin.fragments.battery

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.gyrodatasakhalin.BSERIALS
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.battery.BatteryItem
import com.example.gyrodatasakhalin.battery.BatteryService
import com.example.gyrodatasakhalin.utils.validation.BatteryValidation
import kotlinx.android.synthetic.main.activity_add_battery.*
import kotlinx.android.synthetic.main.battery_condition_switch.*
import kotlinx.android.synthetic.main.progress_bar.*
import retrofit2.Response

private lateinit var retService: BatteryService
private lateinit var errorsBatteryArray: MutableMap<String, String>

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

            for(error in errorsBatteryArray){
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

        errorsBatteryArray = mutableMapOf<String, String>()

        val batteryValidation = BatteryValidation()

        if(getBattery(edBatterySerial1.text.toString())){
            errorsBatteryArray.put("Exists", "This Battery already in DB")
        }
        

        if (!batteryValidation.checkSerial(edBatterySerial1.text.toString())){
            errorsBatteryArray.put("Serial1", "Invalid Serial One")
        }

        if (edBatteryBox.text.toString() == "0"){
            errorsBatteryArray.put("Box", "Box Number couldn't be 0")
        }

        if (!edBatteryInvoice.text.isEmpty()){
            if (!batteryValidation.checkInvoice(edBatteryInvoice.text.toString())){
                errorsBatteryArray.put("Invoice", "Invalid Invoice")
            }
        }

        if (!edBatteryCCD.text.isEmpty()){
            if (!batteryValidation.checkCcd(edBatteryCCD.text.toString())){
                errorsBatteryArray.put("CCD", "Invalid CCD")
            }
        }

        if (!errorsBatteryArray.isEmpty()){
            granted = false
        }

        Log.i("VALID", "${errorsBatteryArray.size}")
        return granted
    }

    private fun getBattery(what: String) : Boolean{

        var exists = false

        if (BSERIALS.contains(what)){exists = true}

        return exists
    }
}