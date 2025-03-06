package com.rahuljoshi.rapidsolutionteam

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.rahuljoshi.rapidsolutionteam.utils.ShardPref
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AppLevelApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        ShardPref.initPreference(this)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}