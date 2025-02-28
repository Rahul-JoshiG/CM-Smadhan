package com.rahuljoshi.rapidsolutionteam.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.rahuljoshi.rapidsolutionteam.data.DistrictsData
import com.rahuljoshi.rapidsolutionteam.databinding.FragmentHomeBinding
import com.rahuljoshi.rapidsolutionteam.interfaces.TeamsInterface
import com.rahuljoshi.rapidsolutionteam.view.adapters.DistrictsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), TeamsInterface {
    private lateinit var mBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: home fragment created")
        mBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: home fragment view created")

        setUpDistrict()
    }

    private fun setUpDistrict() {
        Log.d(TAG, "setUpDistrict: ")
        val districtsList = DistrictsData.getDistrictName()
        val districtsAdapter = DistrictsAdapter(districtsList, this)
        mBinding.districtRecyclerView.apply {
            adapter = districtsAdapter
            layoutManager = GridLayoutManager(requireContext(), 3)
        }
    }

    companion object {
        private const val TAG = "HomeFragment"
    }

    override fun onTeamClicked(data: String) {
        if (data == "Pauri Garhwal") {
            val action = HomeFragmentDirections.actionHomeFragmentToDistrictSolutionFragment(data)
            findNavController().navigate(action)
        }
    }
}