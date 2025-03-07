package com.rahuljoshi.rapidsolutionteam.view.user.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.rahuljoshi.rapidsolutionteam.R
import com.rahuljoshi.rapidsolutionteam.databinding.ActivityDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityDashboardBinding
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setInsets()
        setBottomNavigation()
        setOnClickListeners()
    }

    private fun setBottomNavigation() {
        Log.d(TAG, "setBottomNavigation: ")
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment_view) as NavHostFragment
        mNavController = navHostFragment.navController
        NavigationUI.setupWithNavController(mBinding.bottomNav, mNavController)

        mBinding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    mNavController.navigate(R.id.homeFragment)
                    true
                }
                R.id.ai_chatbot -> {
                    mNavController.navigate(R.id.aiChatbotFragment)
                    true
                }
                else -> false
            }
        }
    }


    private fun setOnClickListeners() {
        Log.d(TAG, "setOnClickListeners: ")

    }


    private fun setInsets(){
        Log.d(TAG, "setInsets: setting insets in dashboard activity")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object{
        private const val TAG = "DashboardActivity"
    }
}