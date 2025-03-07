package com.rahuljoshi.rapidsolutionteam.view.user.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rahuljoshi.rapidsolutionteam.databinding.FragmentShowComplaintBinding
import com.rahuljoshi.rapidsolutionteam.viewmodel.FirebaseViewModel

class ShowComplaintFragment : Fragment() {
    private var _binding: FragmentShowComplaintBinding? = null
    private val binding get() = _binding!!
    private val mViewModel : FirebaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: view create for show complaint fragment...")
        _binding = FragmentShowComplaintBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        private const val TAG = "ShowComplaintFragment"
    }
}