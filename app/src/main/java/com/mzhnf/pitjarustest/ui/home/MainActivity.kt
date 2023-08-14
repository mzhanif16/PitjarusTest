package com.mzhnf.pitjarustest.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mzhnf.pitjarustest.R
import com.mzhnf.pitjarustest.databinding.ActivityMainBinding
import com.mzhnf.pitjarustest.ui.toko.ListTokoActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.tvToko.setOnClickListener {
            val intent = Intent(this,ListTokoActivity::class.java)
            startActivity(intent)
        }

    }
}