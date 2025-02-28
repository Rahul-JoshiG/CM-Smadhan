package com.rahuljoshi.rapidsolutionteam.view.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.rahuljoshi.rapidsolutionteam.databinding.FragmentSolutionTeamBinding
import com.rahuljoshi.rapidsolutionteam.viewmodel.FirebaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SolutionTeam : Fragment() {

    private var _binding: FragmentSolutionTeamBinding? = null
    private val binding get() = _binding!!
    private val mViewModel : FirebaseViewModel by viewModels()

    private val args: SolutionTeamArgs by navArgs()

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                mViewModel.uploadFiles(it, args.teamName)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSolutionTeamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar()
        setUpSpinners()
        handleComplaintSubmission()
    }

    private fun setUpToolbar() {
        Log.d(TAG, "Setting up Toolbar")
        binding.teamAppBar.title = args.teamName

        binding.teamAppBar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpSpinners() {
        Log.d(TAG, "Initializing Spinners")

        binding.complaintType.adapter = createSpinnerAdapter(
            listOf("Select Type", "Solved", "Unsolved")
        )

        binding.complaintLevel.adapter = createSpinnerAdapter(
            listOf("Select Level", "District Level", "State Level")
        )

        binding.complaintType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedType = binding.complaintType.selectedItem.toString()
                binding.complaintReason.visibility = if (selectedType == "Unsolved") View.VISIBLE else View.GONE
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun createSpinnerAdapter(items: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun handleComplaintSubmission() {
        Log.d(TAG, "Handling Complaint Submission")

        binding.submitComplaint.setOnClickListener {
            val title = binding.complaintSubject.text?.trim().toString()
            val location = binding.complaintLocation.text?.trim().toString()
            val description = binding.complaintDescriptionEt.text?.trim().toString()
            val level = binding.complaintLevel.selectedItem.toString()
            val type = binding.complaintType.selectedItem.toString()
            val reason = binding.complaintReason.text?.trim().toString()

            if (validateInputs(title, location, description, reason,  level, type)) {
                mViewModel.sendData(
                    title, location, description, reason, level, type, args.teamName,
                    onSuccess = {
                        Toast.makeText(requireContext(), "Complaint Submitted Successfully!", Toast.LENGTH_SHORT).show()
                        findNavController().navigateUp()
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                )
                findNavController().navigateUp()
            }
        }

        binding.attachFile.setOnClickListener{
            selectFileFromDevice()
        }
    }

    private fun selectFileFromDevice() {
        Log.d(TAG, "selectFileFromDevice: ")
        filePickerLauncher.launch("*/*")
    }

    private fun validateInputs(
        title: String, location: String, description: String, reason : String, level: String, type: String
    ): Boolean {
        return when {
            level == "Select Level" || type == "Select Type" -> {
                showToast("Please select a valid Complaint Level and Type")
                false
            }
            title.isEmpty() || location.isEmpty() || description.isEmpty() -> {
                showToast("Please fill in all details")
                false
            }
            type == "Unsolved" && reason.isEmpty() ->{
                showToast("Describe the reason")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "SolutionTeam"
    }
}