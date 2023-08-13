package com.mzhnf.pitjarustest

import android.content.SharedPreferences
import androidx.multidex.MultiDexApplication
import androidx.preference.PreferenceManager

class PitjarusTest : MultiDexApplication() {
    companion object{
        lateinit var instance : PitjarusTest

        fun getApp() : PitjarusTest{
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getPreferences() : SharedPreferences{
        return PreferenceManager.getDefaultSharedPreferences(this)
    }

    fun setUser(user: String){
        getPreferences().edit().putString("PREFERENCES_USER",user).apply()
    }

    fun getUser(): String?{
        return getPreferences().getString("PREFERENCES_USER",null)
    }
}