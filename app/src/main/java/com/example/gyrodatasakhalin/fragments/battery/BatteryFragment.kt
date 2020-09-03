package com.example.gyrodatasakhalin.fragments.battery

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
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gyrodatasakhalin.API_KEY
import com.example.gyrodatasakhalin.BSERIALS
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.battery.Battery
import com.example.gyrodatasakhalin.battery.BatteryAdapter
import com.example.gyrodatasakhalin.battery.BatteryItem
import com.example.gyrodatasakhalin.battery.BatteryService
import kotlinx.android.synthetic.main.activity_battery.*
import kotlinx.android.synthetic.main.progress_bar.*
import retrofit2.Response

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var retService: BatteryService

/**
 * A simple [Fragment] subclass.
 * Use the [BatteryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BatteryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_battery, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        BSERIALS = ArrayList<String>()

        retService = RetrofitInstance.getRetrofitInstance()
            .create(BatteryService::class.java)

        // Add swipe

        getBatteries(API_KEY, "", "")

        searchBatteryByKey(edSearchBattery)
    }

    private fun searchBatteryByKey(search: EditText){
        search.addTextChangedListener(object : TextWatcher {

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

            private fun search(searchWhat: Editable?, searchWhere: String){
                var searchWhat : String = searchWhat.toString()
                getBatteries(API_KEY, searchWhat, searchWhere)
            }

        })
    }

    private fun getBatteries(token: String, what: String, where: String){

        pbWaiting.bringToFront()
        pbWaiting.visibility = View.VISIBLE

        var batteries = ArrayList<BatteryItem>()

        val responseLiveData : LiveData<Response<Battery>> = liveData {
            val response = retService.getCustomBatteries(what, where)
            Log.i("BATT", "Response: ${response.body()}")
            emit(response)

        }

        responseLiveData.observe(this@BatteryFragment, Observer {
            val batteryList = it.body()?.listIterator()

            if (batteryList != null){


                if (pbWaiting != null && pbWaiting.isShown){
                    pbWaiting.visibility = View.GONE
                }

                while (batteryList.hasNext()){
                    val batteryItem = batteryList.next()
                    batteries.add(batteryItem)
                    BSERIALS.add(batteryItem.serialOne)
                }
                Log.i("BATT", batteries.size.toString())

                updateUI(batteries)
            }
        })
    }

    private fun updateUI(batteryArray: ArrayList<BatteryItem>){
        batteryRecyclerView.adapter = BatteryAdapter(batteryArray, context!!.applicationContext)
        (batteryRecyclerView.adapter as BatteryAdapter).notifyDataSetChanged()
        batteryRecyclerView.layoutManager = LinearLayoutManager(context!!.applicationContext)
        batteryRecyclerView.setHasFixedSize(true)
    }
}