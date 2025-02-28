package com.rahuljoshi.rapidsolutionteam.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.rahuljoshi.rapidsolutionteam.utils.Constant
import javax.inject.Inject

class FirebaseRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {
    fun uploadComplaint(
        title: String,
        location: String,
        description: String,
        reason: String,
        level: String,
        type: String,
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
            "timestamp" to Timestamp.now()
        )

        Log.d(TAG, "uploadComplaint: $selectedDepartment")
        firestore.collection(Constant.COLLECTION_NAME)
            .document(selectedDepartment)  // Ensure it goes to the correct department
            .collection("Complaints")
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

    companion object {
        private const val TAG = "FirebaseRepository"
    }
}
