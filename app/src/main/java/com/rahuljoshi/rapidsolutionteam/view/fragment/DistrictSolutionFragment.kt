package com.rahuljoshi.rapidsolutionteam.view.fragment

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahuljoshi.rapidsolutionteam.data.SolutionTeamData
import com.rahuljoshi.rapidsolutionteam.databinding.FragmentDistrictSolutionBinding
import com.rahuljoshi.rapidsolutionteam.interfaces.TeamsInterface
import com.rahuljoshi.rapidsolutionteam.view.adapters.SolutionTeamAdapter
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
        Log.d(TAG, "checkingTheUsersDistrict: ")
        val id = mViewModel.currentUser()?.uid
        if (id != null) {
            mViewModel.getUserDetails(id) // Triggers data fetch
            viewLifecycleOwner.lifecycleScope.launch {
                mViewModel.userDataState.collectLatest { userData ->
                    userData?.get("department")?.let { userDepartment ->
                        if (userDepartment == data) {
                            val action = DistrictSolutionFragmentDirections.actionDistrictSolutionFragmentToSolutionTeam(data, args.districtName)
                            findNavController().navigate(action)
                        } else {
                            Toast.makeText(requireContext(), "Oops!! It is not your department...", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }else{
            findNavController().navigateUp()
            Toast.makeText(requireContext(), "To access this feature please login...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpRecyclerView() {
        Log.d(TAG, "setUpRecyclerView: ")
        val departmentList = SolutionTeamData.getSolutionTeamData()
        val departmentAdapter = SolutionTeamAdapter(departmentList, this)
        binding.departmentRecyclerView.apply {
            adapter = departmentAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setUpToolbar() {
        Log.d(TAG, "setUpToolbar: ")
        binding.districtName.text = args.districtName

        binding.backTo.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "DistrictSolutionFragment"
    }

    override fun onTeamClicked(data: String) {
        Log.d(TAG, "onTeamClicked: ")
        val action = DistrictSolutionFragmentDirections.actionDistrictSolutionFragmentToSolutionTeam(data, args.districtName)
        findNavController().navigate(action)
        //checkingUserDepartment(data)
    }
}
