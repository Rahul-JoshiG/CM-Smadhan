package com.rahuljoshi.rapidsolutionteam.data

data class SolutionTeam(
    val name: String
)

object SolutionTeamData {
    fun getSolutionTeamData(): List<SolutionTeam> {
        return listOf(
            SolutionTeam("अधिशासी अभियंता, निर्माण खंड पौड़ी गढ़वाल"),
            SolutionTeam("अधिशासी अभियंता, जल संस्थान पौड़ी गढ़वाल"),
            SolutionTeam("अधिशासी अभियंता, जल निगम पौड़ी गढ़वाल"),
            SolutionTeam("अधिशासी अभियंता, विद्युत विभाग पौड़ी गढ़वाल"),
            SolutionTeam("जिला पंचायतराज अधिकारी, पौड़ी गढ़वाल"),
            SolutionTeam("मुख्य कृषि अधिकारी, पौड़ी गढ़वाल"),
            SolutionTeam("मुख्य पशुचिकित्साधिकारी पौड़ी गढ़वाल"),
            SolutionTeam("जिला समाज कल्याण अधिकारी पौड़ी गढ़वाल"),
            SolutionTeam("जिला उद्यान अधिकारी पौड़ी गढ़वाल"),
            SolutionTeam("जिला कार्यक्रम अधिकारी पौड़ी गढ़वाल"),
            SolutionTeam("उप प्रभागीय वनाधिकारी गढ़वाल वन प्रभाग पौड़ी")
        )
    }
}
