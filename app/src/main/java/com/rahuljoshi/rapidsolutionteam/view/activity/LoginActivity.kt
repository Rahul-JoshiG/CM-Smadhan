package com.rahuljoshi.rapidsolutionteam.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rahuljoshi.rapidsolutionteam.R
import com.rahuljoshi.rapidsolutionteam.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setInsets()

        setOnClickListener()

    }

    private fun setOnClickListener() {
        Log.d(TAG, "setOnClickListener: ")
        mBinding.skipButton.setOnClickListener {
            Log.d(TAG, "setOnClickListener: skip button clicked")
            navigateToActivity(DashboardActivity::class.java)
        }

        mBinding.loginButton.setOnClickListener {
            Log.d(TAG, "setOnClickListener: login button clicked")
            checkValidationAndNavigateToDashboard()
        }

        mBinding.registerText.setOnClickListener {
            Log.d(TAG, "setOnClickListener: register button clicked")
            navigateToActivity(RegisterActivity::class.java)
        }

    }

    private fun checkValidationAndNavigateToDashboard() {
        Log.d(TAG, "checkValidationAndNavigateToDashboard: ")
        val email = mBinding.emailId.text.toString().trim()
        val password = mBinding.password.text.toString().trim()
        if(email.isEmpty() || password.isEmpty()){
            mBinding.showErrorMessage.text = "Please fill in all fields"
        }else{
            navigateToActivity(DashboardActivity::class.java)
        }
    }

    private fun navigateToActivity(activity: Class<*>){
        Log.d(TAG, "navigateToDashboardActivity: ")
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    private fun setInsets() {
        Log.d(TAG, "setInsets: setting insets")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}