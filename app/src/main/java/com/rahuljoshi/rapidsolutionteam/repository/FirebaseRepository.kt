package com.rahuljoshi.rapidsolutionteam.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.rahuljoshi.rapidsolutionteam.data.Complaint
import com.rahuljoshi.rapidsolutionteam.data.User
import com.rahuljoshi.rapidsolutionteam.utils.Constant
import com.rahuljoshi.rapidsolutionteam.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) {
    fun uploadComplaint(
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
        val complaintData = hashMapOf(
            "uid" to uid,
            "title" to title,
            "location" to location,
            "description" to description,
            "reason" to reason,
            "level" to level,
            "type" to type,
            "timestamp" to Timestamp.now(),
            "district" to selectedDistrict,
            "department" to selectedDepartment
        )

        Log.d(TAG, "uploadComplaint: $selectedDepartment of $selectedDistrict")
        firestore.collection(Constant.COLLECTION_NAME)
            .document(selectedDistrict)  // Ensure it goes to the correct department
            .collection(selectedDepartment)
            .add(complaintData)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e.message ?: "Failed to submit complaint")
            }
    }

    fun getComplaints(
        currentUserId: String?,
        selectedDistrict: String,
        selectedDepartment: String,
        onSuccess: (List<Map<String, Any>>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val reference = firestore.collection(Constant.COLLECTION_NAME)
            .document(selectedDistrict)
            .collection(selectedDepartment)

        reference.whereEqualTo("uid", currentUserId).get()
            .addOnSuccessListener { complaints ->
                val complaintList = mutableListOf<Map<String, Any>>()

                for (complaint in complaints) {
                    complaintList.add(complaint.data)
                }

                onSuccess(complaintList)
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "getComplaints: ${e.message}")
                onFailure(e)
            }
    }


    fun uploadFiles(uri: Uri, data: String) {
        Log.d(TAG, "uploadFiles: ")
        storage.reference.child("complaint_attachments/$data${uri.lastPathSegment}").putFile(uri)
    }

    suspend fun registerUser(
        name: String,
        email: String,
        password: String,
        district: String,
        department: String
    ): FirebaseUser? {
        Log.d(TAG, "registerUser: register new user")
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user
            user?.let {
                val userData = hashMapOf(
                    "uid" to it.uid,
                    "name" to name,
                    "email" to email,
                    "district" to district,
                    "department" to department
                )
                firestore.collection(Constant.USERS).document(it.uid).set(userData).await()
            }
            user
        } catch (e: Exception) {
            Log.d(TAG, "registerUser: ${e.message}")
            null
        }
    }

    // Login user
    suspend fun loginUser(email: String, password: String): FirebaseUser? {
        Log.d(TAG, "loginUser: login user")
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user
        } catch (e: Exception) {
            Log.d(TAG, "loginUser: ${e.message}")
            null
        }
    }

    // Get user details from Firestore
    suspend fun getUserDetails(uid: String): Map<String, Any>? {
        Log.d(TAG, "getUserDetails: getting user data from firebase")
        return try {
            val document = firestore.collection(Constant.USERS).document(uid).get().await()
            document.data
        } catch (e: Exception) {
            null
        }
    }

    // Logout user
    fun logout() {
        Log.d(TAG, "logout: logging out")
        auth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    private val districtCollection = firestore.collection(Constant.COLLECTION_NAME)
    private val userCollection = firestore.collection(Constant.USERS)

    suspend fun uploadTheComplaint(
        districtId: String,
        departmentId: String,
        complaint: Complaint
    ): Resource<Boolean> = withContext(Dispatchers.IO) {
        Log.d(TAG, "uploadComplaint: uploading complaint in repository")
        return@withContext try {
            val complaintRef = districtCollection
                .document(districtId)
                .collection(Constant.SUB_COLLECTION_NAME)
                .document(departmentId)
                .collection(Constant.SUB_SUB_COLLECTION_NAME)
                .document()
            complaintRef.set(complaint).await()
            Resource.Success(true)
        } catch (e: Exception) {
            Log.d(TAG, "uploadComplaint: ${e.message}")
            Resource.Error(e.localizedMessage ?: "Unknown Error")
        }
    }

    suspend fun getComplaints(
        districtId: String,
        departmentId: String,
    ): Resource<List<Complaint>> = withContext(Dispatchers.IO){
        Log.d(TAG, "getComplaints: getting all complaints in repository")
        return@withContext try {
            val result = districtCollection
                .document(districtId)
                .collection(Constant.SUB_COLLECTION_NAME)
                .document(departmentId)
                .collection(Constant.SUB_SUB_COLLECTION_NAME)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .await()

            val complaints = result.toObjects(Complaint::class.java)
            Resource.Success(complaints)
        } catch (e: Exception) {
            Log.d(TAG, "getComplaints: ${e.localizedMessage}")
            Resource.Error(e.localizedMessage ?: "Failed to fetch complaints")
        }
    }

    suspend fun createUserUsingEmailAndPassword(email: String, password: String) = withContext(
        Dispatchers.IO
    ) {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(result)
            Log.d(TAG, "createUserUsingEmailAndPassword: ${result.user}")
        } catch (e: Exception) {
            Log.d(TAG, "createUserUsingEmailAndPassword: ${e.localizedMessage}")
        }
    }

    suspend fun uploadTheRegisteredUser(user: User): Resource<Boolean> =
        withContext(Dispatchers.IO) {
            Log.d(TAG, "uploadTheRegisteredUser: uploading the user in repository")
            return@withContext try {
                val userName = auth.currentUser?.displayName
                val userDocumentId = userName + auth.currentUser?.uid
                val collectionRef = userCollection.document(userDocumentId)
                collectionRef.set(user).await()

                Resource.Success(true)
            } catch (e: Exception) {
                Log.d(TAG, "uploadTheRegisteredUser: error to upload user")
                Resource.Error(e.localizedMessage ?: "Failed to upload user")
            }

        }

    suspend fun signInTheUser(email: String, password: String) = withContext(Dispatchers.IO) {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result)
        } catch (e: Exception) {
            Log.d(TAG, "signInTheUser: failed to sign the user in repository")
            Resource.Error(e.localizedMessage ?: "Failed to sign in", null)
        }
    }

    suspend fun singOutTheUser() = withContext(Dispatchers.IO) {
        Log.d(TAG, "singOutTheUser: sign out in repository")
        auth.signOut()
    }

    companion object {
        private const val TAG = "FirebaseRepository"
    }
}
