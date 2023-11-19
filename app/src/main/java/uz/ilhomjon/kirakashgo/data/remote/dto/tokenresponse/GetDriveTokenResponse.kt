package uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse

data class GetDriveTokenResponse(
    val access: String,
    val refresh: String,
    val user: User
)