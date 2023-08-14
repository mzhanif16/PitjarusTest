package com.mzhnf.pitjarustest.ui.detailtoko

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.mzhnf.pitjarustest.R
import com.mzhnf.pitjarustest.databinding.ActivityDetailTokoBinding
import com.mzhnf.pitjarustest.ui.toko.ListTokoActivity

class DetailTokoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTokoBinding
    private var storeName: String = ""
    private var storeCode: String = ""
    private var areaName: String =""
    private var channelName: String = ""
    private var address: String = ""
    private var regionName: String = ""
    private var distance: String = ""
    private var longitude: Double = 0.00
    private var latitude: Double = 0.00
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val CAMERA_REQUEST_CODE = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = intent.extras
        if(bundle != null){
            storeName = bundle.getString("store_name","")
            storeCode = bundle.getString("store_code","")
            areaName = bundle.getString("area_name","")
            channelName = bundle.getString("channel_name","")
            address = bundle.getString("address","")
            regionName = bundle.getString("region_name","")
            distance = bundle.getString("distance","")
            longitude = bundle.getDouble("longitude",0.00)
            latitude = bundle.getDouble("latitude",0.00)
        }

        binding.tvStoreName.text = storeName
        binding.tvStoreCode.text = storeCode
        binding.tvAreaName.text = areaName
        binding.tvChannelName.text = channelName
        binding.tvAddress.text = address
        binding.tvRegionName.text = regionName

        binding.btnVisit.setOnClickListener {
            val longitudeString = binding.tvLongitude.text.toString().replace("Long :","")
            val latitudeString = binding.tvLatitude.text.toString().replace("Lat :","")
            val longitudeCurrent = longitudeString.toDouble()
            val latitudeCurrent = latitudeString.toDouble()
            val storeLatLng = LatLng(latitude,longitude)
            Log.d("store",storeLatLng.toString())
            val currentLatLng = LatLng(latitudeCurrent, longitudeCurrent)
            Log.d("data", currentLatLng.toString())
            val distance = calculateDistance(currentLatLng,storeLatLng)
            Log.d("distance", distance.toString())
            if (distance < 100){
                Toast.makeText(this,"Berhasil",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Jarak Terlalu Jauh, Lokasi Belum Sesuai",Toast.LENGTH_SHORT).show()
            }
        }

        binding.ivCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                // Jika izin kamera sudah diberikan, buka kamera
                fetchCurrentLocation()
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            } else {
                // Jika izin kamera belum diberikan, minta izin
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
            }
        }

        binding.btnReset.setOnClickListener {
            // Reset nilai tvLongitude dan tvLatitude
            binding.tvLongitude.text = "Long : 0.00"
            binding.tvLatitude.text = "Lat : 0.00"
        }

        binding.ivCurrentLoct.setOnClickListener {
            fetchCurrentLocation()
        }

        binding.btnNovisit.setOnClickListener {
            val intent = Intent(this,ListTokoActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.ivDirection.setOnClickListener {
            val longitudeString = binding.tvLongitude.text.toString().replace("Long :","")
            val latitudeString = binding.tvLatitude.text.toString().replace("Lat :","")
            val longitudeCurrent = longitudeString.toDouble()
            val latitudeCurrent = latitudeString.toDouble()
            val currentLatLng = LatLng(latitudeCurrent, longitudeCurrent)
            val storeLatLng = LatLng(latitude, longitude)
            val uri = "http://maps.google.com/maps?saddr=${currentLatLng.latitude},${currentLatLng.longitude}&daddr=${storeLatLng.latitude},${storeLatLng.longitude}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.setPackage("com.google.android.apps.maps") // Spesifikasikan Google Maps sebagai aplikasi yang akan dijalankan
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "Aplikasi Google Maps tidak ditemukan", Toast.LENGTH_SHORT).show()
            }
        }

        // Inisialisasi FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Permintaan izin lokasi saat ini
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Mendapatkan lokasi saat ini

        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding.ivHasil.setImageBitmap(imageBitmap)
        }
    }
    fun onBackPressed(view: View){
        onBackPressed()
    }


    private fun calculateDistance(currentLatLng: LatLng, storeLatLng: LatLng): Double {
        val R = 6371 * 1000 // Radius of the earth in meters (6371 km)
        val latDistance = Math.toRadians(storeLatLng.latitude - currentLatLng.latitude)
        val lonDistance = Math.toRadians(storeLatLng.longitude - currentLatLng.longitude)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(currentLatLng.latitude)) * Math.cos(Math.toRadians(storeLatLng.latitude)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
    }

    // Metode untuk mendapatkan lokasi saat ini
    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                val longitude = location.longitude.toString()
                val latitude = location.latitude.toString()
                binding.tvLongitude.text = "Long : $longitude"
                binding.tvLatitude.text = "Lat : $latitude"
            }
        }
    }

}