package com.example.gyrodatasakhalin.fragments.job

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.gyrodatasakhalin.JNUMBERS
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.job.JobItem
import com.example.gyrodatasakhalin.job.JobService
import com.example.gyrodatasakhalin.utils.validation.JobValidation
import kotlinx.android.synthetic.main.activity_job_add.*
import kotlinx.android.synthetic.main.progress_bar.*
import retrofit2.Response
import java.lang.Exception

private lateinit var jobService: JobService
private lateinit var errorsJobArray: MutableMap<String, String>

class AddJobFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_job, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobService = RetrofitInstance.getRetrofitInstance()
            .create(JobService::class.java)

        getInitialJobData()

        addJobButton.setOnClickListener {
            addJob()
        }
    }

    private fun getInitialJobData(){

        pbWaiting.bringToFront()
        pbWaiting.visibility = View.VISIBLE

        val responseLiveData : LiveData<Response<List<List<String>>>> = liveData {
            val response = jobService.getAllDataForJobCreate()
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val jobList = it.body()?.listIterator()
            var jobData = ArrayList<ArrayList<String>>()
            var clients = ArrayList<String>()
            var gdp = ArrayList<String>()
            var modems = ArrayList<String>()
            var bbps = ArrayList<String>()
            var batteries = ArrayList<String>()
            var engs = ArrayList<String>()
            if (jobList != null){

                if (pbWaiting != null && pbWaiting.isShown){
                    pbWaiting.visibility = View.GONE
                }

                while (jobList.hasNext()){
                    val jobItem = jobList.next()
                    jobData.add(jobItem as ArrayList<String>)
                    Log.i("JOB", jobItem.toString())
                }
                clients = jobData.get(0)
                spinnerClient.adapter = ArrayAdapter<String>(context!!.applicationContext, R.layout.spinner_clients, clients)
                Log.i("JOB", "Clients: ${clients.toString()}")
                gdp = jobData.get(1)
                spinnerGDP.adapter = ArrayAdapter<String>(context!!.applicationContext, R.layout.spinner_items, gdp)
                Log.i("JOB", "GDPs: ${gdp.toString()}")
                modems = jobData.get(2)
                spinnerModem.adapter = ArrayAdapter<String>(context!!.applicationContext, R.layout.spinner_items, modems)
                Log.i("JOB", "Modems: ${modems.toString()}")
                bbps = jobData.get(3)
                spinnerBullplug.adapter = ArrayAdapter<String>(context!!.applicationContext, R.layout.spinner_items, bbps)
                Log.i("JOB", "BBPs: ${bbps.toString()}")
                engs = jobData.get(4)
                spinnerEngOne.adapter = ArrayAdapter<String>(context!!.applicationContext, R.layout.spinner_items, engs)
                spinnerEngTwo.adapter = ArrayAdapter<String>(context!!.applicationContext, R.layout.spinner_items, engs)
                Log.i("JOB", "Batteries: ${engs.toString()}")
                batteries = jobData.get(5)
                spinnerBattery.adapter = ArrayAdapter<String>(context!!.applicationContext, R.layout.spinner_items, batteries)
                Log.i("JOB", "ENGs: ${batteries.toString()}")
            }
        })
    }

    private fun addJob(){
        val id = 0
        val jobNumber = edJobJobNumber.text.toString()
        val clientName = spinnerClient.selectedItem.toString()
        val gdp = spinnerGDP.selectedItem.toString()
        val modem = spinnerModem.selectedItem.toString()
        val bbp = spinnerBullplug.selectedItem.toString()
        val battery = spinnerBattery.selectedItem.toString()
        val circulation = convertCirculation(edJobCirculation.text.toString())
        val oldCirculation = 0
        val modemVer = edJobModemVer.text.toString()
        val maxTemp = edJobMaxTemp.text.toString()
        val engOne = spinnerEngOne.selectedItem.toString()
        val engTwo = spinnerEngTwo.selectedItem.toString()
        val engOneArr = edJobEngOneArrived.text.toString()
        val engTwoArr = edJobEngTwoArrived.text.toString()
        val engOneLeft = edJobEngOneLeft.text.toString()
        val engTwoLeft = edJobEngTwoLeft.text.toString()
        val container = edJobContainer.text.toString()
        val containerArr = edJobContArrived.text.toString()
        val containerLeft = edJobContLeft.text.toString()
        val rig = edJobRig.text.toString()

//        TODO() - refactor this
        val issues = "No"
        val comment = edJobComments.text.toString()
        val created = ""
        val updated = ""
        val expandable = false

        val job = JobItem(battery,
            bbp,
            circulation,
            clientName,
            comment,
            container,
            containerArr,
            containerLeft,
            created,
            engOneArr,
            engOneLeft,
            engTwoArr,
            engTwoLeft,
            engOne,
            engTwo,
            gdp,id,
            issues,
            jobNumber,
            maxTemp,
            modem,
            modemVer,
            oldCirculation,
            rig,
            updated,
            expandable)

        Log.i("JOB", "Job created: ${job}")

//        val responseLiveData : LiveData<Response<Int>> = liveData {
//            val response = jobService.addNewJob(job)
//            emit(response)
//        }
//        responseLiveData.observe(this@AddJobFragment, Observer {
//            val result = it.body()
//            if (result != null) {
//                if (pbWaiting != null && pbWaiting.isShown) {
//                    pbWaiting.visibility = View.GONE
////                    clearEditTextViews()
//                }
//            }
//            Log.i("JOB", "Job Number: ${jobNumber} && ${result}")
//            if (pbWaiting != null && pbWaiting.isShown) {
//                pbWaiting.visibility = View.GONE
//            }
//            Toast.makeText(
//                context!!.applicationContext,
//                "Job ${jobNumber} added",
//                Toast.LENGTH_SHORT
//            ).show()
//            getInitialJobData()     // refresh the initial job data
//        })

        if(validateJob()){
            val responseLiveData : LiveData<Response<Int>> = liveData {
                val response = jobService.addNewJob(job)
                emit(response)
            }
            responseLiveData.observe(this@AddJobFragment, Observer {
                val result = it.body()
                if (result != null) {
                    if (pbWaiting != null && pbWaiting.isShown) {
                        pbWaiting.visibility = View.GONE
//                    clearEditTextViews()
                    }
                }
                Log.i("RESULT", "Job Number 1: ${jobNumber} && ${result}")
                if (pbWaiting != null && pbWaiting.isShown) {
                    pbWaiting.visibility = View.GONE
                }
                Toast.makeText(
                    context!!.applicationContext,
                    "Job ${jobNumber} added",
                    Toast.LENGTH_SHORT
                ).show()
            })
        }else{
            var errorBuilder = StringBuffer()

            for(error in errorsJobArray){
                errorBuilder.append(error.value)
                    .append("\n")
            }

            var errorDialog = AlertDialog.Builder(activity)
            errorDialog.setTitle("Uncertainties")
            errorDialog.setMessage(errorBuilder.toString())
            errorDialog.show()
        }
    }

    private fun validateJob(): Boolean{
        var granted = true

        errorsJobArray = mutableMapOf<String, String>()

        val jobValidation = JobValidation()

        if(checkJobExists(edJobJobNumber.text.toString())){
            errorsJobArray["Exists"] = "Job already in DB"
        }

        if (!jobValidation.checkJobNumber(edJobJobNumber.text.toString())){
            errorsJobArray["JobNumber"] = "Invalid Job Number"
        }

        if(!jobValidation.checkModemVersion(edJobModemVer.text.toString())){
            errorsJobArray["ModemVersion"] = "Invalid Modem Version"
        }

        if(!jobValidation.checkMaxTemp(edJobMaxTemp.text.toString())){
            errorsJobArray["MaxTemp"] = "Invalid Max Temperature"
        }

        if (!(convertCirculation(edJobCirculation.text.toString()) is Float)){
            errorsJobArray["Circulation"] = "Circulation should be a number"
        }

        if (!errorsJobArray.isEmpty()){
            granted = false
        }


        Log.i("VALID", "${errorsJobArray.size}")
        return granted
    }

    fun checkJobExists(jobNumber: String): Boolean{
        var exists = false

        if (JNUMBERS.contains(jobNumber)){exists = true}

        return exists
    }

    fun convertCirculation(circulation: String): Float{
        var circulationConverted: Float = 0F
        try {
            circulationConverted = circulation.toFloat()
        }catch (e: Exception){
            e.printStackTrace()
        }
        return circulationConverted
    }
}