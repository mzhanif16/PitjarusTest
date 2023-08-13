package com.mzhnf.pitjarustest.viewmodel

import androidx.lifecycle.ViewModel
import com.mzhnf.pitjarustest.model.response.LoginResponse
import com.mzhnf.pitjarustest.repository.LoginRepository
import io.reactivex.Observable
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val repository = LoginRepository()
    fun login(username: String, password: String): Observable<Response<LoginResponse>> {
        return repository.performLogin(username, password)
    }
}