package com.rahuljoshi.rapidsolutionteam.view.admin.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rahuljoshi.rapidsolutionteam.R
import com.rahuljoshi.rapidsolutionteam.databinding.ActivityAdminLogInBinding

class AdminLogInActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityAdminLogInBinding
    private var isToastShown = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityAdminLogInBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)
        applyInsets()

        setOnClickListeners()
        
        
        

    }

    private fun setOnClickListeners() {
        mBinding.loginBtn.setOnClickListener {
            Log.d(TAG, "setOnClickListeners: ")
            checkCredentialsAndLogin()
        }
    }

    private fun checkCredentialsAndLogin() {
        Log.d(TAG, "checkCredentialsAndLogin: ")
        val adminId = mBinding.adminId.text.toString()
        val adminPassword = mBinding.adminPassword.text.toString()

        if(adminId.isEmpty() || adminPassword.isEmpty()){
            if(!isToastShown){
                isToastShown = true
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                Handler(mainLooper).postDelayed({
                    isToastShown = false
                },3000)
            }
        }else{
            navigateToAdminDashboard()
        }
    }

    private fun navigateToAdminDashboard() {
        Log.d(TAG, "navigateToAdminDashboard: ")
        val intent = Intent(this, AdminDashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun applyInsets(){
        Log.d(TAG, "applyInsets: ")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    
    
    companion object{
        private const val TAG = "AdminLogInActivity"
    }
}