package com.mzhnf.pitjarustest.ui.toko

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Update
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mzhnf.pitjarustest.R
import com.mzhnf.pitjarustest.database.StoreEntity
import com.mzhnf.pitjarustest.databinding.ActivityListTokoBinding
import com.mzhnf.pitjarustest.model.dummy.TokoModel
import com.mzhnf.pitjarustest.repository.StoreRepository
import com.mzhnf.pitjarustest.ui.detailtoko.DetailTokoActivity
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Locale

class ListTokoActivity : AppCompatActivity(), OnMapReadyCallback, ListTokoAdapter.ItemAdapterCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityListTokoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val currentDate = getCurrentDate()

        binding.tvDate.text = currentDate

    }

    private fun getCurrentDate(): String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Memeriksa izin lokasi
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            updateMapAndShowStores()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

//            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
//                if (location != null) {
//                    val currentLatLng = LatLng(location.latitude, location.longitude)
//                    Log.d("currentt",currentLatLng.toString())
//
//                    // Mendapatkan data toko dari database
//                    val storeRepository = StoreRepository(this)
//                    val storeList = runBlocking { storeRepository.getStoresFromDatabase() }
//                    var adapter = ListTokoAdapter(storeList, currentLatLng,this)
//                    var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
//                    binding.rvListToko.layoutManager = layoutManager
//                    binding.rvListToko.adapter = adapter
//
//                    // Menambahkan marker untuk setiap toko pada peta
//                    for (store in storeList) {
//                        val latitude = store.latitude ?: 0.0
//                        val longitude = store.longitude ?: 0.0
//                        val storeLatLng = LatLng(latitude,longitude)
//
//                        val distance = calculateDistance(currentLatLng, storeLatLng)
//                        val distanceText = "%.2f km".format(distance) // Format jarak ke dua desimal
//
//                        val markerOptions = MarkerOptions()
//                            .position(storeLatLng)
//                            .title(store.storeName) // Anda dapat mengganti ini dengan informasi yang sesuai
//                            .snippet("Jarak: $distanceText")
//                        mMap.addMarker(markerOptions)
//
//                        store.distance = distanceText
//                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLatLng, 15f))
//                    }
//                    googleMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
//                }
//            }

    }

    override fun onResume() {
        super.onResume()
    }

    fun onBackPressed(view: View){
        onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true
                    updateMapAndShowStores()
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateMapAndShowStores() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                Log.d("currentt",currentLatLng.toString())

                // Mendapatkan data toko dari database
                val storeRepository = StoreRepository(this)
                val storeList = runBlocking { storeRepository.getStoresFromDatabase() }
                var adapter = ListTokoAdapter(storeList, currentLatLng,this)
                var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false)
                binding.rvListToko.layoutManager = layoutManager
                binding.rvListToko.adapter = adapter

                // Menambahkan marker untuk setiap toko pada peta
                for (store in storeList) {
                    val latitude = store.latitude ?: 0.0
                    val longitude = store.longitude ?: 0.0
                    val storeLatLng = LatLng(latitude,longitude)

                    val distance = calculateDistance(currentLatLng, storeLatLng)
                    val distanceText = "%.2f km".format(distance) // Format jarak ke dua desimal

                    val markerOptions = MarkerOptions()
                        .position(storeLatLng)
                        .title(store.storeName) // Anda dapat mengganti ini dengan informasi yang sesuai
                        .snippet("Jarak: $distanceText")
                    mMap.addMarker(markerOptions)

                    store.distance = distanceText
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLatLng, 15f))
                }
                mMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
            }
        }
    }


    private fun calculateDistance(currentLatLng: LatLng, storeLatLng: LatLng): Double {
        val R = 6371 // Radius of the earth in km
        val latDistance = Math.toRadians(storeLatLng.latitude - currentLatLng.latitude)
        val lonDistance = Math.toRadians(storeLatLng.longitude - currentLatLng.longitude)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(currentLatLng.latitude)) * Math.cos(Math.toRadians(storeLatLng.latitude)) *
                Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
    }
    override fun onClick(v: View, data: StoreEntity) {
        val intent = Intent(this,DetailTokoActivity::class.java)
        intent.putExtra("store_name",data.storeName)
        intent.putExtra("store_code",data.storeCode)
        intent.putExtra("area_name",data.areaName)
        intent.putExtra("channel_name",data.channelName)
        intent.putExtra("address",data.address)
        intent.putExtra("region_name",data.regionName)
        intent.putExtra("distance",data.distance)
        intent.putExtra("longitude",data.longitude)
        Log.d("longitude",data.longitude.toString())
        Log.d("latitude",data.latitude.toString())
        intent.putExtra("latitude",data.latitude)
        startActivity(intent)
    }

}