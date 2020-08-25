package com.example.gyrodatasakhalin.job

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gyrodatasakhalin.R
import kotlinx.android.synthetic.main.job_item.view.*
import kotlinx.android.synthetic.main.job_row.view.*
import kotlinx.android.synthetic.main.progress_bar.view.*

class JobAdapter(private val jobList: List<JobItem>, context: Context) : RecyclerView.Adapter<JobAdapter.JobViewHolder>(){

    private val context: Context = context

    class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clientLogo: TextView = itemView.tvClientLogo
        val client: TextView = itemView.tvJobItemClient
        val jobNumber: TextView = itemView.tvJobItemJobNumber
        val tool: TextView = itemView.findViewById(R.id.tvJobItemTool)
        val circHrs: TextView = itemView.tvJobItemCircHrs
        val mainJobLayout: LinearLayout = itemView.mainJobLayout
        val expandableJobLayout: LinearLayout = itemView.expandableJobLayout
        //Precise data
        val jobBattery: TextView = itemView.findViewById(R.id.tvBattery)
        val jobModem: TextView = itemView.findViewById(R.id.tvModem)
        val jobTool: TextView = itemView.findViewById(R.id.tvTool)
        val jobBbp: TextView = itemView.findViewById(R.id.tvBbp)
        val jobModemVersion: TextView = itemView.findViewById(R.id.tvModemVersion)
        val jobCirculation: TextView = itemView.findViewById(R.id.tvCircHrs)
        val jobMaxTemp: TextView = itemView.findViewById(R.id.tvMaxTmp)
        val jobEngineerOne: TextView = itemView.findViewById(R.id.tvSurvEng1)
        val jobEngineerTwo: TextView = itemView.findViewById(R.id.tvSurvEng2)
        val jobEngineerOneArr: TextView = itemView.findViewById(R.id.tvEng1Arr)
        val jobEngineerTwoArr: TextView = itemView.findViewById(R.id.tvEng2Arr)
        val jobEngineerOneLft: TextView = itemView.findViewById(R.id.tvEng1Lft)
        val jobEngineerTwoLft: TextView = itemView.findViewById(R.id.tvEng2Lft)
        val jobContainer: TextView = itemView.findViewById(R.id.tvContainer)
        val jobContainerArr: TextView = itemView.findViewById(R.id.tvContainerArr)
        val jobContainerLft: TextView = itemView.findViewById(R.id.tvContainerLft)
        val jobComments: TextView = itemView.findViewById(R.id.tvComments)
        val jobIssues: TextView = itemView.findViewById(R.id.tvIssues)
    }

    private fun getContext() : Context {
        return context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val jobView = LayoutInflater.from(context).inflate(
            R.layout.job_item,
            parent, false)

        return JobAdapter.JobViewHolder(jobView)
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val currentJob = jobList[position]
        // Data on Job Preview
        holder.jobNumber.text = currentJob.jobNumber
        holder.tool.text = currentJob.gDP
        holder.circHrs.text = currentJob.circulationHours.toString()
        holder.client.text = currentJob.clientName
        holder.clientLogo.text = clientInfo(currentJob.clientName)

        //Data on expanded Job View
        holder.jobBattery.text = currentJob.battery
        holder.jobModem.text = currentJob.modem
        holder.jobTool.text = currentJob.gDP

        holder.jobBbp.text = currentJob.bullplug
        holder.jobModemVersion.text = currentJob.modemVersion
        holder.jobCirculation.text = currentJob.circulationHours.toString()
        holder.jobMaxTemp.text = currentJob.maxTemp
        holder.jobEngineerOne.text = currentJob.engineerOne
        holder.jobEngineerTwo.text = currentJob.engineerTwo
        holder.jobEngineerOneArr.text = currentJob.engOneArrived
        holder.jobEngineerTwoArr.text = currentJob.engTwoArrived
        holder.jobEngineerOneLft.text = currentJob.engOneLeft
        holder.jobEngineerTwoLft.text = currentJob.engTwoLeft
        holder.jobContainer.text = currentJob.container
        holder.jobContainerArr.text = currentJob.containerArrived
        holder.jobContainerLft.text = currentJob.containerLeft
        holder.jobIssues.text = currentJob.issues
        holder.jobComments.text = currentJob.comment

        val circleColor: GradientDrawable = holder.clientLogo.background.current as GradientDrawable
        val color: Int = getClientColor(currentJob.clientName)
        circleColor.setColor(color)

        val isExpandable: Boolean = jobList[position].expandable
        holder.expandableJobLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.mainJobLayout.setOnClickListener {
            var currentJob = jobList[position]
            currentJob.expandable = !currentJob.expandable
            notifyItemChanged(position)
        }
    }

    private fun getClientColor(client: String) : Int {
        var color: Int = 0
        if (client == null){
            color = R.color.defaultColor
        }else{
            when (client){
                "Schlumberger D&M" -> color = R.color.slbClient

                "Halliburton" -> color =  R.color.halClient
            }
        }
        return ContextCompat.getColor(getContext(), color)
    }

    private fun clientInfo(client: String) : String {
        var clientToSet: String = ""
        when(client){
            "Schlumberger D&M" -> clientToSet = "SLB"
            "Halliburton" -> clientToSet = "HAL"
        }
        return clientToSet
    }
}