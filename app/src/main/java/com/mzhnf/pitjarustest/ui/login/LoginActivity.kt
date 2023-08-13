package com.mzhnf.pitjarustest.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mzhnf.pitjarustest.R
import com.mzhnf.pitjarustest.databinding.ActivityLoginBinding
import com.mzhnf.pitjarustest.ui.home.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}