package project.hackmol.hackmolinstafix.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val isRepairTechnician: Boolean = false
)