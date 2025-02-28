package com.rahuljoshi.rapidsolutionteam.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rahuljoshi.rapidsolutionteam.R
import com.rahuljoshi.rapidsolutionteam.databinding.ActivityRegisterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setInsets()

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        Log.d(TAG, "setOnClickListeners: ")
        mBinding.registerButton.setOnClickListener {
            Log.d(TAG, "setOnClickListeners: register button clicked")
            checkValidationAndNavigateToDashboard()
        }

        mBinding.skipButton.setOnClickListener{
            Log.d(TAG, "setOnClickListeners: skip button clicked")
            navigateToDashBoardActivity()
        }
    }

    private fun checkValidationAndNavigateToDashboard() {
        Log.d(TAG, "checkValidationAndNavigateToDashboard: ")

        val fullName = mBinding.fullNameEditText.text.toString()
        val emailId = mBinding.emailId.text.toString()
        val phoneNumber = mBinding.phoneNumber.text.toString()
        val password = mBinding.password.text.toString()
        val confirmPassword = mBinding.confirmPassword.text.toString()

        if(fullName.isEmpty() || emailId.isEmpty() || phoneNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            mBinding.showErrorMessage.text = "Please fill all the fields"
        }else if(password != confirmPassword){
            mBinding.showErrorMessage.text = "Passwords do not match"
        }else{
            navigateToDashBoardActivity()
        }
    }

    private fun navigateToDashBoardActivity() {
        Log.d(TAG, "navigateToDashBoardActivity: ")
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun setInsets(){
        Log.d(TAG, "setInsets: setting insets")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object{
        private const val TAG = "RegisterActivity"
    }
}