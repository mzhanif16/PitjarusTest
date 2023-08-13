package com.mzhnf.pitjarustest.network


import com.mzhnf.pitjarustest.model.response.LoginResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Endpoint {
    @FormUrlEncoded
    @POST("login/loginTest")
    fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): Observable<Response<LoginResponse>>
}