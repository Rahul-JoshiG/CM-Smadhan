package com.rahuljoshi.rapidsolutionteam.view.user.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.rahuljoshi.rapidsolutionteam.R
import com.rahuljoshi.rapidsolutionteam.databinding.ActivityLoginBinding
import com.rahuljoshi.rapidsolutionteam.utils.Constant
import com.rahuljoshi.rapidsolutionteam.utils.ShardPref
import com.rahuljoshi.rapidsolutionteam.viewmodel.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint

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

    private fun setOnClickListener() {
        mBinding.rememberMeCheckbox.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG, "Remember Me Checked: $isChecked")
            isUserWantToRememberMe = isChecked
            ShardPref.putIsUserLoggedIn(Constant.IS_USER_LOGGED_IN, isChecked)
        }

        mBinding.skipButton.setOnClickListener {
            Log.d(TAG, "Skip button clicked on user login page")
            ShardPref.putIsUserLoggedIn(Constant.IS_USER_LOGGED_IN, false)
            ShardPref.putIsUserLoggedIn(Constant.SKIP_ENTER, true)
            navigateToActivity(DashboardActivity::class.java)
        }

        mBinding.loginButton.setOnClickListener {
            Log.d(TAG, "Login button clicked on user login page")
            checkValidationAndNavigateToDashboard()
        }

        mBinding.registerText.setOnClickListener {
            Log.d(TAG, "Navigate to RegisterActivity for user registration")
            navigateToActivity(RegisterActivity::class.java)
        }
    }

    private fun checkValidationAndNavigateToDashboard() {
        Log.d(TAG, "Validating login credentials of user in login screen")
        val email = mBinding.emailId.text.toString().trim()
        val password = mBinding.password.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        mViewModel.login(email, password)
        mViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                mBinding.loginButton.text = ""
                mBinding.progressBar.visibility = View.VISIBLE
            } else {
                mBinding.loginButton.text = getString(R.string.login)
                mBinding.progressBar.visibility = View.GONE
            }
        }
        mViewModel.loginState.observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                Log.d(TAG, "checkValidationAndNavigateToDashboard: ")
                ShardPref.putIsUserLoggedIn(Constant.IS_USER_LOGGED_IN, isUserWantToRememberMe)
                navigateToActivity(DashboardActivity::class.java)
                finish()
            } else {
                Toast.makeText(
                    this@LoginActivity,
                    "Login failed. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateToActivity(activity: Class<*>) {
        Log.d(TAG, "Navigating to ${activity.simpleName}")
        val intent = Intent(this, activity)
        startActivity(intent)
    }

    private fun setInsets() {
        Log.d(TAG, "Applying window insets")
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
