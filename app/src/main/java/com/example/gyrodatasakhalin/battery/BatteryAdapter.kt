package com.example.gyrodatasakhalin.battery

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gyrodatasakhalin.R
import kotlinx.android.synthetic.main.battery_item.view.*
import kotlinx.android.synthetic.main.battery_row.view.*

class BatteryAdapter(private val batteryList: List<BatteryItem>, context: Context) : RecyclerView.Adapter<BatteryAdapter.BatteryViewHolder>() {

    private val context: Context = context

    class BatteryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val batteryCondition: TextView = itemView.tvCondition
        val serialOne: TextView = itemView.serial_one
        val batteryArrived: TextView = itemView.battery_arrived
        val batteryInvoicePrev: TextView = itemView.battery_invoice
        val mainBatteryLayout: LinearLayout = itemView.mainBatteryLayout
        val expandableBatteryLayout: LinearLayout = itemView.expandableBatteryLayout
        //Precise Battery Data
        val batterySerialTwo: TextView = itemView.findViewById(R.id.tvSerNum2)
        val batterySerialThr: TextView = itemView.findViewById(R.id.tvSerNum3)
        val batteryInvoice: TextView = itemView.findViewById(R.id.tvBatteryInvoice)
        val batteryCCDs: TextView = itemView.findViewById(R.id.tvBatteryCCD)
        val batteryManufactured: TextView = itemView.findViewById(R.id.tvManuf)
        val batteryLocation: TextView = itemView.findViewById(R.id.tvBatteryLocation)
        val batteryComments: TextView = itemView.findViewById(R.id.tvBatteryComments)
    }

    private fun getContext() : Context {
        return context
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatteryViewHolder {
        val batteryView = LayoutInflater.from(context).inflate(R.layout.battery_item,
            parent, false)

        return BatteryViewHolder(batteryView)
    }

    override fun getItemCount(): Int {
        return batteryList.size
    }

    override fun onBindViewHolder(holder: BatteryViewHolder, position: Int) {
        val currentBattery = batteryList[position]
        // Preview Battery Data
        holder.serialOne.text = currentBattery.serialOne
        holder.batteryArrived.text = currentBattery.arrived
        holder.batteryCondition.text = getCondition(currentBattery.batteryCondition)
        holder.batteryInvoicePrev.text = "Invoice: ${currentBattery.invoice}"
        val circleColor : GradientDrawable = holder.batteryCondition.background.current as GradientDrawable
        val color : Int = getConditionColor(currentBattery.batteryCondition)
        Log.i("COLOR", color.toString())
        circleColor.setColor(color)

        // Precise Battery Data
        holder.batterySerialTwo.text = currentBattery.serialTwo
        holder.batterySerialThr.text = currentBattery.serialThr
        holder.batteryInvoice.text = currentBattery.invoice
        //TODO - refactor CCDs
        holder.batteryCCDs.text = currentBattery.CCD
        holder.batteryManufactured.text = currentBattery.arrived
        holder.batteryLocation.text = currentBattery.batteryStatus
        holder.batteryComments.text = currentBattery.comment

        val isExpandable: Boolean = batteryList[position].expandable
        holder.expandableBatteryLayout.visibility = if (isExpandable) View.VISIBLE else View.GONE

        holder.mainBatteryLayout.setOnClickListener {
            var currentJob = batteryList[position]
            currentJob.expandable = !currentJob.expandable
            notifyItemChanged(position)
        }

    }

    private fun getCondition(condition: String) : String {
        var conditionRes : String = "U"
        if (condition.equals("New")){
            conditionRes = "N"
        }
        return conditionRes
    }

    private fun getConditionColor(condition : String) : Int {
        var color : Int = 0
        if (condition == null){
            color = R.color.defaultColor
        }else{
            when (condition){
                "New" -> color = R.color.newBattColor

                "Used" -> color = R.color.usedBattColor
            }
        }
        Log.i("COLOR", color.toString())
        return ContextCompat.getColor(getContext(), color)
    }
}