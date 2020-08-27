package com.example.gyrodatasakhalin.tool


import com.google.gson.annotations.SerializedName

data class ToolItem(
    @SerializedName("Arrived")
    val arrived: String,
    @SerializedName("Asset")
    val asset: String,
    @SerializedName("Box")
    val box: String,
    @SerializedName("CCD")
    val cCD: String,
    @SerializedName("Circulation")
    val circulation: Int,
    @SerializedName("Comment")
    val comment: String,
    @SerializedName("Container")
    val container: Any,
    @SerializedName("Created_at")
    val createdAt: String,
    @SerializedName("Id")
    val id: Int,
    @SerializedName("Invoice")
    val invoice: String,
    @SerializedName("Item")
    val item: String,
    @SerializedName("ItemImage")
    val itemImage: String,
    @SerializedName("ItemStatus")
    val itemStatus: String,
    @SerializedName("NameRus")
    val nameRus: String,
    @SerializedName("PositionCCD")
    val positionCCD: String,
    @SerializedName("Updated_at")
    val updatedAt: String,

    var expandable: Boolean = false
)