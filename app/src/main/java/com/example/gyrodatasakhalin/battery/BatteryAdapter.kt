package com.example.gyrodatasakhalin.battery

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gyrodatasakhalin.R
import kotlinx.android.synthetic.main.battery_item.view.*

class BatteryAdapter(private val batteryList: List<BatteryItem>) : RecyclerView.Adapter<BatteryAdapter.BatteryViewHolder>() {



    class BatteryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val batteryView: ImageView = itemView.battery_view
        val serailOne: TextView = itemView.serial_one
        val batteryCCD: TextView = itemView.battery_ccd
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BatteryViewHolder {
        val batteryView = LayoutInflater.from(parent.context).inflate(R.layout.battery_item,
            parent, false)

        return BatteryViewHolder(batteryView)
    }

    override fun getItemCount(): Int {
        return batteryList.size
    }

    override fun onBindViewHolder(holder: BatteryViewHolder, position: Int) {
        val currentBattery = batteryList[position]

        holder.serailOne.text = currentBattery.serialOne
        holder.batteryCCD.text = currentBattery.CCD


    }
}