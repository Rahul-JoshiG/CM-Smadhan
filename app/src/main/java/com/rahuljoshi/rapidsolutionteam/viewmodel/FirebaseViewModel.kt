package com.rahuljoshi.rapidsolutionteam.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.rahuljoshi.rapidsolutionteam.data.Complaint
import com.rahuljoshi.rapidsolutionteam.data.User
import com.rahuljoshi.rapidsolutionteam.repository.FirebaseRepository
import com.rahuljoshi.rapidsolutionteam.wrapper.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userState = MutableStateFlow<FirebaseUser?>(null)
    val userState: StateFlow<FirebaseUser?> = _userState

    private val _registerState = MutableLiveData<Boolean>()
    val registerState: LiveData<Boolean> = _registerState

    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> = _loginState

    private val _userDataState = MutableStateFlow<Map<String, Any>?>(null)
    val userDataState: StateFlow<Map<String, Any>?> = _userDataState

    fun sendData(
        uid: String?,
        title: String,
        location: String,
        description: String,
        reason: String,
        level: String,
        type: String,
        selectedDistrict: String,
        selectedDepartment: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            repository.uploadComplaint(
                uid,
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

    fun getComplaintsDetails(
        currentUserId: String?,
        selectedDistrict: String,
        selectedDepartment: String,
        onSuccess: (List<Map<String, Any>>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.getComplaints(
                    currentUserId, selectedDistrict, selectedDepartment, onSuccess, onFailure
                )
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching complaints: ${e.message}")
                onFailure(e)
            }
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
        name: String, email: String, password: String, district: String, department: String
    ) {
        Log.d(TAG, "register: in view model")
        viewModelScope.launch {
            val response = repository.registerUser(name, email, password, district, department)
            _registerState.value = response != null
        }

    }

    // Login user
    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val currentUser = repository.loginUser(email, password)
            if (currentUser != null) {
                _isLoading.value = false
                _loginState.value = true
            } else {
                _isLoading.value = false
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

    fun currentUser(): FirebaseUser? {
        return repository.getCurrentUser()
    }

    // Logout user
    fun logout() {
        repository.logout()
        _userState.value = null
        _userDataState.value = null
    }


    private val _getComplaints = MutableLiveData<Resource<List<Complaint>>>()
    val getComplaints: LiveData<Resource<List<Complaint>>> = _getComplaints

    private val _uploadComplaints = MutableLiveData<Resource<Boolean>>()
    val uploadComplaints: LiveData<Resource<Boolean>> = _uploadComplaints

    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean> = _status

    fun uploadComplaint(
        districtId: String,
        departmentId: String,
        complaint: Complaint
    ) {
        Log.d(TAG, "uploadComplaint: uploading complaints to repository in view model")
        viewModelScope.launch {
            val resultOfUploadComplaint =
                repository.uploadTheComplaint(districtId, departmentId, complaint)
            _uploadComplaints.value = resultOfUploadComplaint
        }
    }

    fun getComplaints(
        districtId: String,
        departmentId: String,
    ) {
        Log.d(TAG, "getComplaints: getting complaints from repository in view model")
        viewModelScope.launch {
            _getComplaints.value = Resource.Loading()
            val resultOfGetComplaints = repository.getComplaints(districtId, departmentId)
            _getComplaints.value = resultOfGetComplaints
        }
    }

    fun uploadTheUser(user: User){
        _status.value = true
        Log.d(TAG, "uploadTheUser: uploading the user from view model")
        viewModelScope.launch {
            repository.uploadTheRegisteredUser(user)
            _status.value = false
        }
    }

    fun createUser(email: String, password: String){
        Log.d(TAG, "createUser: create user from view model")
        _status.value = true
        viewModelScope.launch {
            repository.createUserUsingEmailAndPassword(email, password)
            _status.value = false
        }
    }

    fun signInUser(email: String, password: String){
        Log.d(TAG, "signInUser: sign in user from view model")
        _status.value = true
        viewModelScope.launch {
            repository.signInTheUser(email, password)
            _status.value = false
        }
    }

    fun signOutUser(){
        Log.d(TAG, "signOutUser: sign out user from view model")
        viewModelScope.launch {
            repository.singOutTheUser()
        }
    }

    companion object {
        private const val TAG = "FirebaseViewModel"
    }
}
