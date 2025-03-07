package com.rahuljoshi.rapidsolutionteam.view.user.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahuljoshi.rapidsolutionteam.databinding.FragmentComplaintBinding
import com.rahuljoshi.rapidsolutionteam.interfaces.OnShowComplaint
import com.rahuljoshi.rapidsolutionteam.view.user.adapters.ComplaintAdapter
import com.rahuljoshi.rapidsolutionteam.viewmodel.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ComplaintFragment : Fragment(), OnShowComplaint {
    private var _binding: FragmentComplaintBinding? = null
    private val mBinding get() = _binding!!

    private val args: ComplaintFragmentArgs by navArgs()

    private lateinit var complaintAdapter: ComplaintAdapter
    private val complaintList = mutableListOf<Map<String, Any>>()

    private val mViewModel: FirebaseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComplaintBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpRecyclerView()
        fetchUserDataAndComplaints()
        setOnClickListener()
    }

    private fun setUpToolbar() {
        Log.d(TAG, "setUpToolbar: set upping toolbar in complaint fragment")
        mBinding.appBar.title = args.departmentName
        mBinding.appBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setOnClickListener() {
        Log.d(TAG, "setOnClickListener: in complain fragment")
        mBinding.addNewComplaint.setOnClickListener {
            findNavController().navigate(ComplaintFragmentDirections.actionComplaintFragmentToSolutionTeam(args.departmentName, args.districtName))
        }
    }

    private fun setUpRecyclerView() {
        Log.d(TAG, "setUpRecyclerView: ")
        complaintAdapter = ComplaintAdapter(complaintList, this)
        mBinding.myComplaintsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = complaintAdapter
        }
    }

    private fun fetchUserDataAndComplaints() {
        val uid = mViewModel.currentUser()?.uid ?: return

        lifecycleScope.launch {
            mViewModel.userDataState.collectLatest { userData ->
                val department = userData?.get("department")?.toString() ?: ""
                val district = userData?.get("district")?.toString() ?: ""

                mViewModel.getComplaintsDetails(
                    currentUserId = uid,
                    selectedDistrict = district,
                    selectedDepartment = department,
                    onSuccess = { complaints ->
                        complaintList.clear()
                        complaintList.addAll(complaints)
                        complaintAdapter.notifyDataSetChanged()
                    },
                    onFailure = { exception ->
                        Log.e("FirestoreError", "Error fetching complaints", exception)
                    }
                )
            }
        }
    }

    override fun showComplaint(complaint: Map<String, Any>) {
        Log.d(TAG, "showComplaint: $complaint clicked on complaint fragment")
        val action = ComplaintFragmentDirections.actionComplaintFragmentToShowComplaintFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "ComplaintFragment"
    }
}
