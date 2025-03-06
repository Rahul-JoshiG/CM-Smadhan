package com.rahuljoshi.rapidsolutionteam.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object ShardPref {

    private lateinit var mSharedPreferences: SharedPreferences
    private const val TAG = "ShardPref"


    fun initPreference(context: Context){
        Log.d(TAG, "initPreference: ")
        mSharedPreferences = context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }

    fun clearData(){
        Log.d(TAG, "clearData: ")
        mSharedPreferences.edit().clear().apply()
    }

    fun isUserLoggedIn(key: String): Boolean{
        Log.d(TAG, "isUserLoggedIn: ${mSharedPreferences.getBoolean(key, false)}")
        return mSharedPreferences.getBoolean(key, false)
    }

    fun putIsUserLoggedIn(key: String, value: Boolean){
        Log.d(TAG, "putBoolean: user login $value")
        mSharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun putSkipButtonOn(key: String, value: Boolean){
        Log.d(TAG, "putSkipButtonOn: ")
        mSharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getSkipButtonPressed(key: String): Boolean{
        Log.d(TAG, "getSkipButtonPressed: ")
        return mSharedPreferences.getBoolean(key, false)
    }


}