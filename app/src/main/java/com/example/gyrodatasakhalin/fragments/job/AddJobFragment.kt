package com.example.gyrodatasakhalin.fragments.job

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.job.JobService
import kotlinx.android.synthetic.main.activity_job_add.*
import kotlinx.android.synthetic.main.progress_bar.*
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var jobService: JobService

class AddJobFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_add_job, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobService = RetrofitInstance.getRetrofitInstance()
            .create(JobService::class.java)

        getInitialJobData()
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
}