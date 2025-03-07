package com.rahuljoshi.rapidsolutionteam.view.user.fragment

import android.R
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
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
    private var mCurrentUserId: String? = null

    private val args: SolutionTeamArgs by navArgs()

    private var selectedFileUri: Uri? = null

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedFileUri = it
                binding.fileName.text = getFileName(it)
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
        setOnClickListener()

        mCurrentUserId = mViewModel.currentUser()?.uid
    }

    private fun setOnClickListener() {
        binding.removeUploadFile.setOnClickListener {
            binding.fileName.text = ""
            selectedFileUri = null
        }
    }

    private fun setUpToolbar() {
        binding.teamName.text = args.teamName
        binding.backTo.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpSpinners() {
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
        return ArrayAdapter(requireContext(), R.layout.simple_spinner_item, items).apply {
            setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        }
    }

    private fun handleComplaintSubmission() {
        binding.submitComplaint.setOnClickListener {
            val title = binding.complaintSubject.text?.trim().toString()
            val location = binding.complaintLocation.text?.trim().toString()
            val description = binding.complaintDescriptionEt.text?.trim().toString()
            val level = binding.complaintLevel.selectedItem.toString()
            val type = binding.complaintType.selectedItem.toString()
            val reason = binding.complaintReason.text?.trim().toString()

            if (validateInputs(title, location, description, reason, level, type)) {
                mViewModel.sendData(
                    mCurrentUserId, title, location, description, reason, level, type, args.districtName, args.teamName,
                    onSuccess = {
                        Toast.makeText(requireContext(), "Complaint Submitted Successfully!", Toast.LENGTH_SHORT).show()
                        //sendEmail(title, location, description, reason, level, type)
                    },
                    onFailure = { errorMessage ->
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                )
            }
        }

        binding.attachFile.setOnClickListener {
            selectFileFromDevice()
        }
    }

    private fun selectFileFromDevice() {
        filePickerLauncher.launch("*/*")
    }

    private fun getFileName(uri: Uri): String {
        var fileName = "Unknown"
        val cursor: Cursor? = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && it.moveToFirst()) {
                fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }

    private fun validateInputs(
        title: String, location: String, description: String, reason: String, level: String, type: String
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
            type == "Unsolved" && reason.isEmpty() -> {
                showToast("Describe the reason")
                false
            }
            else -> true
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun sendEmail(
        title: String, location: String, description: String, reason: String, level: String, type: String
    ) {
        val recipient = "rapidsolutionteamuk@gmail.com" // Your official email
        val subject = "New Complaint Submission: $title"

        val message = """
        Complaint Details:
        -------------------------------
        Location: $location
        Type: $type
        Level: $level
        Description: $description
        Reason (if unsolved): $reason
        -------------------------------
    """.trimIndent()

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            this.type= "*/*"  // Set the correct MIME type
            putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)

            // Attach the file if selected
            selectedFileUri?.let { uri ->
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_STREAM, uri)
            }
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Send Email..."))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "No email client found.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "SolutionTeam"
    }
}
