package com.algogence.ourtodo

import android.app.Activity
import android.content.Context

class MySharedPreferences(private val context: Context) {
    fun addBoolean(name: String, value: Boolean){
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(name, value)
            apply()
        }
    }

    fun getBoolean(name: String, defaultValue: Boolean): Boolean{
        val sharedPref = (context as Activity).getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(name, defaultValue)
    }
}