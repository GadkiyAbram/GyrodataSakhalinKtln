package com.example.gyrodatasakhalin.fragments.job

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
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
import com.example.gyrodatasakhalin.job.Job
import com.example.gyrodatasakhalin.R
import com.example.gyrodatasakhalin.RetrofitInstance
import com.example.gyrodatasakhalin.job.JobAdapter
import com.example.gyrodatasakhalin.job.JobItem
import kotlinx.android.synthetic.main.activity_job.*
import kotlinx.android.synthetic.main.progress_bar.*
import retrofit2.Response

private lateinit var jobService: com.example.gyrodatasakhalin.job.JobService

class JobFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_job, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

            jobService = RetrofitInstance.getRetrofitInstance()
                .create(com.example.gyrodatasakhalin.job.JobService::class.java)

        getJobs(API_KEY, "", "")

        searchJobByKey(edSearchJob)
    }

    private fun getJobs(token: String, what: String, where: String){

        pbWaiting.bringToFront()
        pbWaiting.visibility = View.VISIBLE

        val responseLiveData : LiveData<Response<Job>> = liveData {
            val response = jobService.getCustomJobs(what, where)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val jobList = it.body()?.listIterator()
            var jobs = ArrayList<JobItem>()
            if (jobList != null){

                if (pbWaiting != null && pbWaiting.isShown){
                    pbWaiting.visibility = View.GONE
                }

                while (jobList.hasNext()){
                    val jobItem = jobList.next()
                    jobs.add(jobItem)
                }
                jobRecyclerView.adapter = JobAdapter(jobs, context!!.applicationContext)
                (jobRecyclerView.adapter as JobAdapter).notifyDataSetChanged()
                jobRecyclerView.layoutManager = LinearLayoutManager(context!!.applicationContext)
                jobRecyclerView.setHasFixedSize(true)
            }
        })
    }

    private fun searchJobByKey(search: EditText){
        search.addTextChangedListener(object : TextWatcher {

            var where: String = ""

            override fun afterTextChanged(searchQuery: Editable?) {
                if (radioJobNumButton.isChecked){ where = "JobNumber" }
                if (radioJobGDPButton.isChecked){ where = "GDP" }
                if (radioJobModemButton.isChecked){ where = "Modem" }
                if (radioJobBPButton.isChecked){ where = "Bullplug" }
                if (radioJobBatteryButton.isChecked){ where = "Battery" }

                Handler().postDelayed({
                    search(searchQuery, where)
                }, 1500)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            private fun search(searchWhat: Editable?, searchWhere: String){
                var searchWhat : String = searchWhat.toString()
                getJobs(API_KEY, searchWhat, searchWhere)
            }

        })
    }
}