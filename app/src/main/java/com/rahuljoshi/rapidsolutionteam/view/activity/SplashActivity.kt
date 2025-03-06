package com.rahuljoshi.rapidsolutionteam.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rahuljoshi.rapidsolutionteam.R
import com.rahuljoshi.rapidsolutionteam.databinding.ActivitySplashBinding
import com.rahuljoshi.rapidsolutionteam.utils.Constant
import com.rahuljoshi.rapidsolutionteam.utils.ShardPref

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {


    private lateinit var mBinding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        applyInsets()

        checkedUserIsLoggedInOrNot()
    }

    private fun checkedUserIsLoggedInOrNot() {
        Log.d(TAG, "checkedUserIsLoggedInOrNot: ")
        val isLoggedIn = ShardPref.isUserLoggedIn(Constant.IS_USER_LOGGED_IN)
        if (isLoggedIn) {
            Log.d(TAG, "checkedUserIsLoggedInOrNot: User is logged in")
            openActivity(DashboardActivity::class.java)
        } else {
            Log.d(TAG, "checkedUserIsLoggedInOrNot: User is not logged in")
            openActivity(LoginActivity::class.java)
        }
    }

    private fun openActivity(activity: Class<*>) {
        Log.d(TAG, "openActivity: opening $activity")
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashActivity, activity)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN_TIME)

    }

    private fun applyInsets() {
        Log.d(TAG, "applyInsets: ")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    companion object {
        private const val TAG = "SplashActivity"
        private const val SPLASH_SCREEN_TIME = 3000L
    }
}