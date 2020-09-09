package com.example.gyrodatasakhalin.fragments.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gyrodatasakhalin.JNUMBERS
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.TITEMSASSETS
import com.example.gyrodatasakhalin.battery.Battery
import com.example.gyrodatasakhalin.battery.BatteryItem
import com.example.gyrodatasakhalin.battery.BatteryService
import com.example.gyrodatasakhalin.job.Job
import com.example.gyrodatasakhalin.job.JobAdapter
import com.example.gyrodatasakhalin.job.JobItem
import com.example.gyrodatasakhalin.job.JobService
import com.example.gyrodatasakhalin.tool.Tool
import com.example.gyrodatasakhalin.tool.ToolAdapter
import com.example.gyrodatasakhalin.tool.ToolItem
import com.example.gyrodatasakhalin.tool.ToolService
import com.example.gyrodatasakhalin.ui.NetworkConnection
import kotlinx.android.synthetic.main.activity_job.*
import kotlinx.android.synthetic.main.activity_tool.*
import kotlinx.android.synthetic.main.fab_layout.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.progress_bar.*
import retrofit2.Response

private lateinit var toolService: ToolService
private lateinit var batteryService: BatteryService
private lateinit var jobService: JobService

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolService = RetrofitInstance.getRetrofitInstance().create(ToolService::class.java)
        batteryService = RetrofitInstance.getRetrofitInstance().create(BatteryService::class.java)
        jobService = RetrofitInstance.getRetrofitInstance().create(JobService::class.java)


        getDashboardData("", "")

    }

    private fun getDashboardData(what: String, where: String){
        var tools = ArrayList<String>()
        var batteries = ArrayList<String>()
        var jobs = ArrayList<String>()

        // getting battery data
        val responseBatteryLiveData : LiveData<Response<Battery>> = liveData {
            val response = batteryService.getCustomBatteries(what, where)
            emit(response)
        }

        responseBatteryLiveData.observe(this@DashboardFragment, Observer {
            val batteryList = it.body()?.listIterator()
            batteries_updated.text = "No data"
            batteries_total.text = "No data"
            if (batteryList != null){

                while (batteryList.hasNext()){
                    val batteryItem = batteryList.next()
                    batteries.add(batteryItem.updatedAt)
                }
            }
            batteries.sort()
            val updatedBatteriesLast = batteries.get(batteries.size - 1)
            val batteriesTotal = batteries.size.toString()
            if (updatedBatteriesLast != null && batteriesTotal != null){
                batteries_updated.text = updatedBatteriesLast
                batteries_total.text = batteriesTotal
            }
        })

        // getting tool data
        val responseToolLiveData : LiveData<Response<Tool>> = liveData {
            val response = toolService.getCustomItems(what, where)
            emit(response)
        }

        responseToolLiveData.observe(this, Observer {
            val toolList = it.body()?.listIterator()
            tools_updated.text = "No data"
            items_total.text = "No data"
            if (toolList != null){
                while (toolList.hasNext()){
                    val toolItem = toolList.next()
                    tools.add(toolItem.updatedAt)
                    // filling ITEMSASSETS map for further search
                    TITEMSASSETS.put(toolItem.asset, toolItem.item)
                }
                val updatedToolsLast = tools.get(tools.size - 1)
                val toolsTotal = tools.size.toString()
                if (updatedToolsLast != null && toolsTotal != null){
                    tools_updated.text = updatedToolsLast
                    items_total.text = toolsTotal
                }
                Log.i("ADD", "MAP ITEMS SIZE: ${TITEMSASSETS.size}")
                Log.i("ADD", "ARRAY ITEMS SIZE: ${tools.size}")
            }
        })

        // getting job data
        val responseJobLiveData : LiveData<Response<Job>> = liveData {
            val response = jobService.getCustomJobs(what, where)
            emit(response)
        }

        responseJobLiveData.observe(this, Observer {
            val jobList = it.body()?.listIterator()
            jobs_updated.text = "No data"
            jobs_total.text = "No data"
            if (jobList != null){

                while (jobList.hasNext()){
                    val jobItem = jobList.next()
                    jobs.add(jobItem.updatedAt)
                    JNUMBERS.add(jobItem.jobNumber)
                }
                val updatedJobList = jobs.get(jobs.size - 1)
                val jobsTotal = jobs.size.toString()
                if(updatedJobList != null && jobsTotal != null){
                    jobs_updated.text = updatedJobList
                    jobs_total.text = jobsTotal
                }
            }
        })
    }
}