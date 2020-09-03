package com.example.gyrodatasakhalin.job


import com.google.gson.annotations.SerializedName

data class JobItem(
    @SerializedName("Battery")
    val battery: String,
    @SerializedName("Bullplug")
    val bullplug: String,
    @SerializedName("CirculationHours")
    val circulationHours: Float,
    @SerializedName("ClientName")
    val clientName: String,
    @SerializedName("Comment")
    val comment: String,
    @SerializedName("Container")
    val container: String,
    @SerializedName("ContainerArrived")
    val containerArrived: String,
    @SerializedName("ContainerLeft")
    val containerLeft: String,
    @SerializedName("Created_at")
    val createdAt: String,
    @SerializedName("eng_one_arrived")
    val engOneArrived: String,
    @SerializedName("eng_one_left")
    val engOneLeft: String,
    @SerializedName("eng_two_arrived")
    val engTwoArrived: String,
    @SerializedName("eng_two_left")
    val engTwoLeft: String,
    @SerializedName("EngineerOne")
    val engineerOne: String,
    @SerializedName("EngineerTwo")
    val engineerTwo: String,
    @SerializedName("GDP")
    val gDP: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Issues")
    val issues: String,
    @SerializedName("JobNumber")
    val jobNumber: String,
    @SerializedName("MaxTemp")
    val maxTemp: String,
    @SerializedName("Modem")
    val modem: String,
    @SerializedName("ModemVersion")
    val modemVersion: String,
    @SerializedName("OldCirculation")
    val oldCirculation: Int,
    @SerializedName("Rig")
    val rig: String,
    @SerializedName("Updated_at")
    val updatedAt: String,

    var expandable: Boolean = false
)