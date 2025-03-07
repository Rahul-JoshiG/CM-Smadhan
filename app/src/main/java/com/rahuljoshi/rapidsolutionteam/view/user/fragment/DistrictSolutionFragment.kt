package com.rahuljoshi.rapidsolutionteam.view.user.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahuljoshi.rapidsolutionteam.data.SolutionTeamData
import com.rahuljoshi.rapidsolutionteam.databinding.FragmentDistrictSolutionBinding
import com.rahuljoshi.rapidsolutionteam.interfaces.TeamsInterface
import com.rahuljoshi.rapidsolutionteam.view.user.adapters.SolutionTeamAdapter
import com.rahuljoshi.rapidsolutionteam.viewmodel.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DistrictSolutionFragment : Fragment(), TeamsInterface {

    private var _binding: FragmentDistrictSolutionBinding? = null
    private val binding get() = _binding!!
    private val mViewModel: FirebaseViewModel by viewModels()
    private val args: DistrictSolutionFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDistrictSolutionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpRecyclerView()
    }

    private fun checkingUserDepartment(data: String) {
        val userId = mViewModel.currentUser()?.uid
        if (userId != null) {
            viewLifecycleOwner.lifecycleScope.launch {
                mViewModel.getUserDetails(userId)
                mViewModel.userDataState.collectLatest { userData ->
                    val userDepartment = userData?.get("department")
                    if (userDepartment == data) {
                        findNavController().navigate(DistrictSolutionFragmentDirections.actionDistrictSolutionFragmentToComplaintFragment(userDepartment.toString(),args.districtName))
                    } else {
                        showToast("Oops!! It is not your department...")
                    }
                }
            }
        } else {
            findNavController().navigateUp()
            showToast("To access this feature, please log in...")
        }
    }

    private fun setUpRecyclerView() {
        binding.departmentRecyclerView.apply {
            adapter = SolutionTeamAdapter(SolutionTeamData.getSolutionTeamData(), this@DistrictSolutionFragment)
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUpToolbar() {
        binding.districtName.text = args.districtName
        binding.backTo.setOnClickListener { findNavController().navigateUp() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTeamClicked(data: String) {
        checkingUserDepartment(data)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
