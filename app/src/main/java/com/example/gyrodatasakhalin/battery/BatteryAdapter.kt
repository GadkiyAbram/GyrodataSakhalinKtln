package com.example.gyrodatasakhalin.battery

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.gyrodatasakhalin.R
import kotlinx.android.synthetic.main.battery_item.view.*

class BatteryAdapter(private val batteryList: List<BatteryItem>, context: Context) : RecyclerView.Adapter<BatteryAdapter.BatteryViewHolder>() {

    private val context: Context = context

    class BatteryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val batteryCondition: TextView = itemView.tvCondition
        val serialOne: TextView = itemView.serial_one
        val batteryCCD: TextView = itemView.battery_arrived
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
        holder.serialOne.text = currentBattery.serialOne
        holder.batteryCCD.text = currentBattery.CCD
        holder.batteryCondition.text = getCondition(currentBattery.batteryCondition)
        val circleColor : GradientDrawable = holder.batteryCondition.background.current as GradientDrawable
        val color : Int = getConditionColor(currentBattery.batteryCondition)
        Log.i("COLOR", color.toString())
        circleColor.setColor(color)

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