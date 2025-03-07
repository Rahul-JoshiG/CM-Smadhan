package com.rahuljoshi.rapidsolutionteam.view.user.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rahuljoshi.rapidsolutionteam.R
import com.rahuljoshi.rapidsolutionteam.data.ListOfData
import com.rahuljoshi.rapidsolutionteam.databinding.ActivityRegisterBinding
import com.rahuljoshi.rapidsolutionteam.viewmodel.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityRegisterBinding
    private val mViewModel: FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setInsets()
        setUpSpinner()

        setOnClickListeners()
    }

    private fun setUpSpinner() {
        Log.d(TAG, "setUpSpinner: ")
        val districtsList = ListOfData.returnDistrictList()
        val departmentsList = ListOfData.returnDepartmentList()
        mBinding.districtName.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, districtsList)
        mBinding.departmentName.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, departmentsList)
    }

    private fun setOnClickListeners() {
        Log.d(TAG, "setOnClickListeners: ")

        mBinding.registerButton.setOnClickListener {
            Log.d(TAG, "setOnClickListeners: register button clicked")
            checkValidationAndNavigateToDashboard()
        }

        mBinding.skipButton.setOnClickListener {
            Log.d(TAG, "setOnClickListeners: skip button clicked")
            navigateToDashBoardActivity()
        }
    }

    private fun checkValidationAndNavigateToDashboard() {
        Log.d(TAG, "checkValidationAndNavigateToDashboard: ")

        val fullName = mBinding.fullNameEditText.text.toString().trim()
        val emailId = mBinding.emailId.text.toString().trim()
        val password = mBinding.password.text.toString().trim()
        val confirmPassword = mBinding.confirmPassword.text.toString().trim()
        val districtName = mBinding.districtName.selectedItem.toString()
        val departmentName = mBinding.departmentName.selectedItem.toString()

        if (fullName.isEmpty() || emailId.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
            districtName == "Select District" || departmentName == "Select Department") {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
        } else if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
        } else {
            observerRegistrationState(fullName, emailId, password, districtName, departmentName)
        }
    }

    private fun observerRegistrationState(
        fullName: String,
        emailId: String,
        password: String,
        districtName: String,
        departmentName: String
    ) {
        Log.d(TAG, "observerRegistrationState: ")
        mBinding.progressBar.visibility = VISIBLE
        mViewModel.register(fullName, emailId, password, districtName, departmentName)
        mViewModel.registerState.observe(this) { isRegistered ->
            if (isRegistered) {
                navigateToDashBoardActivity()
                mBinding.progressBar.visibility = GONE
            } else {
                Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                mBinding.progressBar.visibility = GONE
            }
        }
    }

    private fun navigateToDashBoardActivity() {
        Log.d(TAG, "navigateToDashBoardActivity: ")
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setInsets() {
        Log.d(TAG, "setInsets: setting insets")
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.register_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        private const val TAG = "RegisterActivity"
    }
}