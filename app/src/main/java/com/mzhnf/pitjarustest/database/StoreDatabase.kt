package com.mzhnf.pitjarustest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StoreEntity::class], version = 1, exportSchema = false)
abstract class StoreDatabase : RoomDatabase(){
    abstract fun storeDao(): StoreDao

    companion object {
        @Volatile
        private var INSTANCE: StoreDatabase? = null

        fun getDatabase(context: Context): StoreDatabase {
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StoreDatabase::class.java,
                    "store_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}