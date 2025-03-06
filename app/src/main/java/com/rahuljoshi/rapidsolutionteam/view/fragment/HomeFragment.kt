package com.rahuljoshi.rapidsolutionteam.view.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.rahuljoshi.rapidsolutionteam.data.DistrictsData
import com.rahuljoshi.rapidsolutionteam.databinding.FragmentHomeBinding
import com.rahuljoshi.rapidsolutionteam.interfaces.TeamsInterface
import com.rahuljoshi.rapidsolutionteam.utils.Constant
import com.rahuljoshi.rapidsolutionteam.utils.ShardPref
import com.rahuljoshi.rapidsolutionteam.view.activity.LoginActivity
import com.rahuljoshi.rapidsolutionteam.view.adapters.DistrictsAdapter
import com.rahuljoshi.rapidsolutionteam.viewmodel.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), TeamsInterface {

    private var _binding: FragmentHomeBinding? = null
    private val mBinding get() = _binding!!
    private val mViewModel: FirebaseViewModel by viewModels()
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView: HomeFragment created")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: HomeFragment view created")

        if(ShardPref.getSkipButtonPressed(Constant.SKIP_ENTER)){
            currentUser == null
        }else{
            currentUser = mViewModel.currentUser()
        }

        setUpDistrict()
        setUpEmergencyNumbers()
        setUpSocialMedia()
        setOnClickListener()
        loginLogoutIcon()
    }

    private fun loginLogoutIcon() {
        Log.d(TAG, "loginLogoutIcon")
        mBinding.loginLogout.setOnClickListener {
            ShardPref.clearData()
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setOnClickListener() {
        Log.d(TAG, "setOnClickListener")
        mBinding.articlesLl.setOnClickListener { showToast("Coming soon...") }
        mBinding.galleryLl.setOnClickListener { showToast("Coming soon...") }
        mBinding.administrationLl.setOnClickListener { showToast("Coming soon...") }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpEmergencyNumbers() {
        Log.d(TAG, "setUpEmergencyNumbers")
        mBinding.policeLl.setOnClickListener { callService(Constant.POLICE) }
        mBinding.ambulanceLl.setOnClickListener { callService(Constant.AMBULANCE) }
        mBinding.emergencyLl.setOnClickListener { callService(Constant.EMERGENCY) }
    }

    private fun callService(type: String) {
        try {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(type)))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "Something went wrong. Try again later.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpSocialMedia() {
        Log.d(TAG, "setUpSocialMedia")
        mBinding.facebook.setOnClickListener { openSocialAccount(Constant.FACEBOOK_LINK) }
        mBinding.twitter.setOnClickListener { openSocialAccount(Constant.TWITTER_LINK) }
        mBinding.instagram.setOnClickListener { openSocialAccount(Constant.INSTAGRAM_LINK) }
    }

    private fun openSocialAccount(url: String) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } catch (e: Exception) {
            Log.e(TAG, "No app can handle this intent: $url", e)
            showToast("Something went wrong...")
        }
    }

    private fun setUpDistrict() {
        Log.d(TAG, "setUpDistrict")
        val districtsList = DistrictsData.getDistrictName()
        val districtsAdapter = DistrictsAdapter(districtsList, this)
        mBinding.districtRecyclerView.apply {
            adapter = districtsAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    override fun onTeamClicked(data: String) {
        if(data == "Pauri Garhwal"){
            val action = HomeFragmentDirections.actionHomeFragmentToDistrictSolutionFragment(data)
            findNavController().navigate(action)
        }else{
            showToast("Coming soon...")
        }
    }

    private fun checkUserDistrict(data: String) {
        Log.d(TAG, "Checking user district: $data")

        val userId = currentUser?.uid
        if (userId == null) {
            Log.e(TAG, "User not logged in!")
            showToast("Please log in first.")
            return
        }

        Log.d(TAG, "User is logged in with ID: $userId")
        mViewModel.getUserDetails(userId)

        viewLifecycleOwner.lifecycleScope.launch {
            mViewModel.userDataState.collectLatest { userData ->
                Log.d(TAG, "User Data Received: $userData")

                val userDistrict = userData?.get("district") as? String
                if (userDistrict == null) {
                    Log.e(TAG, "User district not found in Firestore!")
                    showToast("District information not available.")
                    return@collectLatest
                }

                Log.d(TAG, "User District: $userDistrict, Clicked District: $data")

                if (userDistrict == data) {
                    Log.d(TAG, "User's district matches, navigating...")
                    val action = HomeFragmentDirections.actionHomeFragmentToDistrictSolutionFragment(data)
                    findNavController().navigate(action)
                } else {
                    Log.d(TAG, "District does not match")
                    showToast("Oops! It is not your district.")
                    showToast("Coming soon...")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "HomeFragment"
    }
}
