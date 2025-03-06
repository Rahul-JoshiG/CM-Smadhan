package com.rahuljoshi.rapidsolutionteam.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.rahuljoshi.rapidsolutionteam.R
import com.rahuljoshi.rapidsolutionteam.databinding.ActivityLoginBinding
import com.rahuljoshi.rapidsolutionteam.utils.Constant
import com.rahuljoshi.rapidsolutionteam.utils.ShardPref
import com.rahuljoshi.rapidsolutionteam.viewmodel.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    private var isUserWantToRememberMe = false

    private val mViewModel: FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setInsets()
        setOnClickListener()
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
        if(mViewModel.currentUser()==null){
            Log.d(TAG, "onStart: user is null")
        }else{
            Log.d(TAG, "onStart: user is not null")
        }
    }

    private fun setOnClickListener() {
        mBinding.rememberMeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Remember Me Checked: $isChecked")
            isUserWantToRememberMe = isChecked
            ShardPref.putIsUserLoggedIn(Constant.IS_USER_LOGGED_IN, isChecked)
        }

        mBinding.skipButton.setOnClickListener {
            Log.d(TAG, "setOnClickListener: ")
            ShardPref.putIsUserLoggedIn(Constant.IS_USER_LOGGED_IN, false)
            ShardPref.putIsUserLoggedIn(Constant.SKIP_ENTER, true)
            navigateToActivity(DashboardActivity::class.java)
        }

        mBinding.loginButton.setOnClickListener {
            Log.d(TAG, "setOnClickListener: ")
            checkValidationAndNavigateToDashboard()
        }

        mBinding.registerText.setOnClickListener {
            Log.d(TAG, "setOnClickListener: ")
            navigateToActivity(RegisterActivity::class.java)
        }
    }

    private fun checkValidationAndNavigateToDashboard() {
        Log.d(TAG, "checkValidationAndNavigateToDashboard: ")
        val email = mBinding.emailId.text.toString().trim()
        val password = mBinding.password.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }else{
            mViewModel.login(email, password)
            mViewModel.loginState.observe(this, {isLoggedIn->
                if(isLoggedIn){
                    Log.d(TAG, "checkValidationAndNavigateToDashboard: ")
                    navigateToActivity(DashboardActivity::class.java)
                    finish() // Prevent going back to LoginActivity on pressing back
                }else{
                    Toast.makeText(this@LoginActivity, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun observeViewModel() {
        Log.d(TAG, "observeViewModel: ")
        lifecycleScope.launch {
            mViewModel.userState.collect { user ->
                if (user != null) {
                    ShardPref.putIsUserLoggedIn(Constant.IS_USER_LOGGED_IN, isUserWantToRememberMe)
                    navigateToActivity(DashboardActivity::class.java)
                    finish() // Prevent going back to LoginActivity on pressing back
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun navigateToActivity(activity: Class<*>) {
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