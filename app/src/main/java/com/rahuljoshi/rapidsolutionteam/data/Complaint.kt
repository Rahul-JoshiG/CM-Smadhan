package com.rahuljoshi.rapidsolutionteam.data

data class Complaint(
    val uid: String = "",
    val title: String = "",
    val description: String = "",
    val type: String = "",
    val level: String = "",
    val fileUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
