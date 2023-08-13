package com.mzhnf.pitjarustest.ui.toko

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.mzhnf.pitjarustest.R
import com.mzhnf.pitjarustest.database.StoreEntity

class ListTokoAdapter(
    private val listData: List<StoreEntity>,
    private val currentLocation : LatLng? = LatLng(0.00,0.00),
    private val itemAdapterCallback: ItemAdapterCallback,
) : RecyclerView.Adapter<ListTokoAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTokoAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_row_toko,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListTokoAdapter.ViewHolder, position: Int) {
        holder.bind(listData[position], itemAdapterCallback, currentLocation)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        private val tvNamaToko:TextView = itemView.findViewById(R.id.tv_namatoko)
        private val tvCluster:TextView = itemView.findViewById(R.id.tv_cluster)
        private val tvJarak:TextView = itemView.findViewById(R.id.tv_jarak)
        fun bind(
            data: StoreEntity,
            itemAdapterCallback: ItemAdapterCallback,
            currentLocation: LatLng?
        ){
        itemView.apply {
            tvNamaToko.text = data.storeName
            tvCluster.text = data.storeCode

            val latitude = data.latitude ?: 0.0
            val longitude = data.longitude ?: 0.0
            val storeLatLng = LatLng(latitude, longitude)

            val distance = calculateDistance(currentLocation!!, storeLatLng)
            val distanceText = "%.2f km".format(distance)
            tvJarak.text = distanceText

            itemView.setOnClickListener { itemAdapterCallback.onClick(it, data) }
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
    }


    interface ItemAdapterCallback{
        fun onClick(v: View, data: StoreEntity)
    }

}