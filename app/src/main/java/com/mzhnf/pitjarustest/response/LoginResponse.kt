package com.mzhnf.pitjarustest.response


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @Expose
    @SerializedName("message")
    val message: String,
    @Expose
    @SerializedName("status")
    val status: String,
    @Expose
    @SerializedName("stores")
    val stores: List<Store>
)