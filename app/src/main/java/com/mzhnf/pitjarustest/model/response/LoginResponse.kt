package com.mzhnf.pitjarustest.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @Expose
    @SerializedName("stores")
    val stores: List<Store>,
    @Expose
    @SerializedName("status")
    val status: String,
    @Expose
    @SerializedName("message")
    val message: String
    )