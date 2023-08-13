package com.mzhnf.pitjarustest.ui.toko

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        val storeRepository = StoreRepository(this)
        val storeList = runBlocking { storeRepository.getStoresFromDatabase() }

    }

    private fun getCurrentDate(): String{
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @SuppressLint("NotifyDataSetChanged")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Memeriksa izin lokasi
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)

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
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
//                googleMap.addMarker(MarkerOptions().position(currentLatLng).title("You are here"))
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true
                }
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

    }

}