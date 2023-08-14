package com.mzhnf.pitjarustest.repository

import android.content.Context
import com.mzhnf.pitjarustest.database.StoreDao
import com.mzhnf.pitjarustest.database.StoreDatabase
import com.mzhnf.pitjarustest.database.StoreEntity
import com.mzhnf.pitjarustest.model.response.StoreResponse

class StoreRepository (private val context: Context){
    private val storeDao : StoreDao by lazy {
        StoreDatabase.getDatabase(context).storeDao()
    }
    suspend fun saveStoreToDatabase(stores: List<StoreResponse>){
        val storeEntities = stores.map { store ->
            StoreEntity(
                storeId = store.storeId,
                storeCode = store.storeCode,
                storeName = store.storeName,
                address = store.address,
                dcId = store.dcId,
                dcName = store.dcName,
                accountId = store.accountId,
                accountName = store.accountName,
                subchannelId = store.subchannelId,
                channelId = store.channelId,
                channelName = store.channelName,
                areaId = store.areaId,
                areaName = store.areaName,
                regionId = store.regionId,
                regionName = store.regionName,
                latitude = store.latitude,
                longitude = store.longitude,
                subchannelName = store.subchannelName,
                distance = store.distance
            )
        }
        storeDao.insertStores(storeEntities)
    }
    suspend fun getStoresFromDatabase(): List<StoreEntity>{
        return storeDao.getAllStores()
    }
}