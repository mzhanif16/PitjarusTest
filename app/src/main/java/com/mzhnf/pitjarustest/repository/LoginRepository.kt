package com.mzhnf.pitjarustest.repository

import com.mzhnf.pitjarustest.model.response.LoginResponse
import com.mzhnf.pitjarustest.network.RetrofitClient
import io.reactivex.Observable
import retrofit2.Response

class LoginRepository {
    fun performLogin(username:String, password:String): Observable<Response<LoginResponse>> {
        return RetrofitClient.apiService.login(username,password)
    }
}