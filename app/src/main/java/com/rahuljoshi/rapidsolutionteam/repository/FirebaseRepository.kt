package com.rahuljoshi.rapidsolutionteam.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rahuljoshi.rapidsolutionteam.utils.Constant
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage,
    private val auth: FirebaseAuth
) {
    fun uploadComplaint(
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

    fun uploadFiles(uri: Uri, data: String) {
        Log.d(TAG, "uploadFiles: ")
        storage.reference.child("complaint_attachments/$data${uri.lastPathSegment}").putFile(uri)
    }


    suspend fun registerUser(name: String, email: String, password: String, district: String, department: String): FirebaseUser? {
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
    suspend fun loginUser(email: String, password: String):FirebaseUser?{
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

    companion object {
        private const val TAG = "FirebaseRepository"
    }
}
