package com.mzhnf.pitjarustest.ui.login

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.viewModels
import com.mzhnf.pitjarustest.R
import com.mzhnf.pitjarustest.databinding.ActivityLoginBinding
import com.mzhnf.pitjarustest.repository.StoreRepository
import com.mzhnf.pitjarustest.ui.home.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    var progressDialog: Dialog? = null
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()

        binding.btnLogin.setOnClickListener {
            var username = binding.username.text.toString()
            var password = binding.password.text.toString()

            if (username.isNullOrEmpty()) {
                binding.username.error = "Silahkan Masukkan Username Anda"
                binding.username.requestFocus()
            } else if (password.isNullOrEmpty()) {
                binding.password.error = "Silahkan Masukkan Password Anda"
                binding.password.requestFocus()
            } else {
                performLogin(username, password)
            }
        }
        // Mengambil SharedPreferences default
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this)

        // Mengambil nilai username dari SharedPreferences
        val savedUsername = sharedPreferences.getString("username", "")

        // Mengisi field username dengan nilai yang disimpan (jika ada)
        binding.username.setText(savedUsername)

        // Mengatur listener untuk checkbox
        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            // Simpan username saat checkbox di-centang
            if (isChecked) {
                val enteredUsername = binding.username.text.toString()
                sharedPreferences.edit().putString("username", enteredUsername).apply()
            } else {
                // Hapus username saat checkbox di-uncheck
                sharedPreferences.edit().remove("username").apply()
            }
        }

    }

    private fun initView() {
        progressDialog = Dialog(this)
        val dialogLayout = layoutInflater.inflate(R.layout.dialog_loader, null)

        progressDialog?.let {
            it.setContentView(dialogLayout)
            it.setCancelable(false)
            it.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
    }

    @SuppressLint("CheckResult")
    private fun performLogin(username: String, password: String) {
        progressDialog?.show()

        loginViewModel.login(username, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                progressDialog?.dismiss()

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.status == "success") {
                        val stores = responseBody.stores

                        CoroutineScope(Dispatchers.IO).launch {
                            val storeRepository = StoreRepository(this@LoginActivity)
                            storeRepository.saveStoreToDatabase(stores)
                        }

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login Gagal", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login Gagal", Toast.LENGTH_SHORT).show()
                }
            }, { error ->
                progressDialog?.dismiss()
                Toast.makeText(this@LoginActivity, "Error: ${error.message}", Toast.LENGTH_SHORT)
                    .show()
            })
    }


}