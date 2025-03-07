package com.rahuljoshi.rapidsolutionteam.repository

import android.util.Log
import com.google.firebase.Timestamp
import com.rahuljoshi.rapidsolutionteam.repository.FirebaseRepository.Companion.TAG

class ComplaintRepository {

    suspend fun uploadComplaint(
        complaintTitle: String,
        complaintDescription: String,
        complaintLocation: String,
        complaintReason: String,
        complaintLevel: String,
        complaintType: String,
        fileUrl: String
    ) {
        Log.d(TAG, "uploadComplaint: uploading complaint in repository")
        val currentUserId = auth.currentUser?.uid
        val userDistrict = getUserDetails(currentUserId ?: "")?.get("district")
        val userDepartment = getUserDetails(currentUserId ?: "")?.get("department")
        val complaintData = hashMapOf(
            "uid" to currentUserId,
            "title" to complaintTitle,
            "description" to complaintDescription,
            "location" to complaintLocation,
            "reason" to complaintReason,
            "level" to complaintLevel,
            "type" to complaintType,
            "district" to userDistrict,
            "department" to userDepartment,
            "timestamp" to Timestamp.now(),
            "fileUrl" to fileUrl
        )

    }
}