package com.example.gyrodatasakhalin.battery


import com.google.gson.annotations.SerializedName

data class BatteryItem(
    @SerializedName("Arrived")
    val arrived: String,
    @SerializedName("BatteryCondition")
    val batteryCondition: String,
    @SerializedName("BatteryStatus")
    val batteryStatus: String,
    @SerializedName("BoxNumber")
    val boxNumber: String,
    @SerializedName("CCD")
    val CCD: String,
    @SerializedName("Comment")
    val comment: String,
    @SerializedName("Container")
    val container: String,
    @SerializedName("Created_at")
    val createdAt: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Invoice")
    val invoice: String,
    @SerializedName("SerialOne")
    val serialOne: String,
    @SerializedName("SerialThr")
    val serialThr: String,
    @SerializedName("SerialTwo")
    val serialTwo: String,
    @SerializedName("Updated_at")
    val updatedAt: String
)