package com.rahuljoshi.rapidsolutionteam.data

data class Districts(
    val name: String
)
object DistrictsData{
    fun getDistrictName(): List<Districts> {
        return listOf(
            Districts("Almora"),
            Districts("Bageshwar"),
            Districts("Chamoli"),
            Districts("Champawat"),
            Districts("Dehradun"),
            Districts("Haridwar"),
            Districts("Nainital"),
            Districts("Pauri Garhwal"),
            Districts("Pithoragarh"),
            Districts("Rudraprayag"),
            Districts("Tehri Garhwal"),
            Districts("Udham Singh Nagar"),
            Districts("Uttarkashi")
        )
    }

}