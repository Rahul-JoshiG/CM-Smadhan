package com.rahuljoshi.rapidsolutionteam.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahuljoshi.rapidsolutionteam.data.SolutionTeamData
import com.rahuljoshi.rapidsolutionteam.databinding.FragmentDistrictSolutionBinding
import com.rahuljoshi.rapidsolutionteam.interfaces.TeamsInterface
import com.rahuljoshi.rapidsolutionteam.view.adapters.SolutionTeamAdapter

class DistrictSolutionFragment : Fragment(), TeamsInterface {

    private var _binding : FragmentDistrictSolutionBinding? = null
    private val binding get() = _binding!!

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
        binding.appBar.title = args.districtName

        binding.appBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    companion object {
       private const val TAG = "DistrictSolutionFragment"
    }

    override fun onTeamClicked(data: String) {
        Log.d(TAG, "onTeamClicked: ")
        val action = DistrictSolutionFragmentDirections.actionDistrictSolutionFragmentToSolutionTeam(data)
        findNavController().navigate(action)
    }
}