package com.rahuljoshi.rapidsolutionteam.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.rahuljoshi.rapidsolutionteam.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
    private val _userState = MutableStateFlow<FirebaseUser?>(null)
    val userState: StateFlow<FirebaseUser?> = _userState

    private val _registerState = MutableLiveData<Boolean>()
    val registerState: LiveData<Boolean> = _registerState

    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> = _loginState

    private val _userDataState = MutableStateFlow<Map<String, Any>?>(null)
    val userDataState: StateFlow<Map<String, Any>?> = _userDataState

    fun sendData(
        title: String,
        location: String,
        description: String,
        reason: String,
        level: String,
        type: String,
        selectedDistrict: String,  // The department assigned to the HOD
        selectedDepartment: String,  // The department selected by the user
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.uploadComplaint(
                title,
                location,
                description,
                reason,
                level,
                type,
                selectedDistrict,
                selectedDepartment,
                onSuccess,
                onFailure
            )
        }
    }

    fun uploadFiles(uri: Uri, data: String) {
        Log.d(TAG, "uploadFiles: ")
        viewModelScope.launch {
            repository.uploadFiles(uri, data)
        }

    }


    // Register user
    fun register(
        name: String,
        email: String,
        password: String,
        district: String,
        department: String
    ) {
        Log.d(TAG, "register: in view model")
        viewModelScope.launch {
            val response = repository.registerUser(name, email, password, district, department)
            if(response != null){
                _registerState.value = true
            }else{
                _registerState.value = false
            }
        }

    }

    // Login user
    fun login(email: String, password: String) {
        viewModelScope.launch {
            val currentUser = repository.loginUser(email, password)
            if(currentUser != null){
                _loginState.value = true
            }else{
                _loginState.value = false
            }
        }
    }

    // Get user details
    fun getUserDetails(uid: String) {
        viewModelScope.launch {
            val userData = repository.getUserDetails(uid)
            _userDataState.value = userData
        }
    }

    fun currentUser():FirebaseUser?{
        return repository.getCurrentUser()
    }

    // Logout user
    fun logout() {
        repository.logout()
        _userState.value = null
        _userDataState.value = null
    }

    companion object {
        private const val TAG = "FirebaseViewModel"
    }
}
