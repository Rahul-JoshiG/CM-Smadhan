package com.rahuljoshi.rapidsolutionteam.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahuljoshi.rapidsolutionteam.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    fun sendData(
        title: String,
        location: String,
        description: String,
        reason: String,
        level: String,
        type: String,
        //hodDepartment: String,  // The department assigned to the HOD
        selectedDepartment: String,  // The department selected by the user
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.uploadComplaint(
                title, location, description, reason, level, type, selectedDepartment, onSuccess, onFailure
            )
        }
    }

    fun uploadFiles(uri: Uri, data: String) {
        Log.d(TAG, "uploadFiles: ")
        viewModelScope.launch {
            repository.uploadFiles(uri, data)
        }

    }

    companion object{
        private const val TAG = "FirebaseViewModel"
    }
}
