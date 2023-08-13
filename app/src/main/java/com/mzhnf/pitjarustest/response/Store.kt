package com.mzhnf.pitjarustest.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Store(
    @Expose
    @SerializedName("account_id")
    val accountId: String,
    @Expose
    @SerializedName("account_name")
    val accountName: String,
    @Expose
    @SerializedName("address")
    val address: String,
    @Expose
    @SerializedName("area_id")
    val areaId: String,
    @Expose
    @SerializedName("area_name")
    val areaName: String,
    @Expose
    @SerializedName("channel_id")
    val channelId: String,
    @Expose
    @SerializedName("channel_name")
    val channelName: String,
    @Expose
    @SerializedName("dc_id")
    val dcId: String,
    @Expose
    @SerializedName("dc_name")
    val dcName: String,
    @Expose
    @SerializedName("latitude")
    val latitude: String,
    @Expose
    @SerializedName("longitude")
    val longitude: String,
    @Expose
    @SerializedName("region_id")
    val regionId: String,
    @Expose
    @SerializedName("region_name")
    val regionName: String,
    @Expose
    @SerializedName("store_code")
    val storeCode: String,
    @Expose
    @SerializedName("store_id")
    val storeId: String,
    @Expose
    @SerializedName("store_name")
    val storeName: String,
    @Expose
    @SerializedName("subchannel_id")
    val subchannelId: String,
    @Expose
    @SerializedName("subchannel_name")
    val subchannelName: String
)