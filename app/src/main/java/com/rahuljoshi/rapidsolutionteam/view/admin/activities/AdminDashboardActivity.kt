package com.rahuljoshi.rapidsolutionteam.view.admin.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.rahuljoshi.rapidsolutionteam.R
import com.rahuljoshi.rapidsolutionteam.data.DistrictsData
import com.rahuljoshi.rapidsolutionteam.databinding.ActivityAdminDashboardBinding
import com.rahuljoshi.rapidsolutionteam.interfaces.TeamsInterface
import com.rahuljoshi.rapidsolutionteam.view.user.adapters.DistrictsAdapter

class AdminDashboardActivity : AppCompatActivity(), TeamsInterface {
    private lateinit var mBinding : ActivityAdminDashboardBinding
    private var isToastShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        applyInsets()

        setUpDistrict()

    }

    private fun setUpDistrict() {
        Log.d(TAG, "setUpDistrict")
        val districtsList = DistrictsData.getDistrictName()
        val districtsAdapter = DistrictsAdapter(districtsList, this)
        mBinding.districtRecyclerView.apply {
            adapter = districtsAdapter
            layoutManager = GridLayoutManager(this@AdminDashboardActivity, 3)
        }
    }

    private fun applyInsets(){
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onTeamClicked(data: String) {
        if(!isToastShown){
            isToastShown = true
            Toast.makeText(this@AdminDashboardActivity, "$data clicked", Toast.LENGTH_SHORT).show()
            Handler(Looper.getMainLooper()).postDelayed({
                isToastShown = false
            }, 2000)
        }
    }

    companion object{
        private const val TAG = "AdminDashboardActivity"
    }
}