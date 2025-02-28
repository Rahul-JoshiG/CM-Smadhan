package com.rahuljoshi.rapidsolutionteam.data

import com.rahuljoshi.rapidsolutionteam.R

data class EmergencyNumbers(
    val title: String,
    val image: Int
)

object EmergencyNumbersData {
    fun listOfEmergencyNumbers(): List<EmergencyNumbers> {
        return listOf(
            EmergencyNumbers("Police", R.drawable.ic_police),
            EmergencyNumbers("Ambulance", R.drawable.ic_ambulance),
            EmergencyNumbers("Emergency", R.drawable.ic_emergency)
        )
    }
}
