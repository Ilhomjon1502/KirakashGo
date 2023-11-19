package uz.ilhomjon.kirakashgo.data.remote.dto

data class LoginDriverResponse(
    val detail: String,
    val first_name: String,
    val role: String,
    val success: Boolean,
    val username: String
)