package uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse

data class GetDriveTokenResponse(
    var access: String,
    val refresh: String,
    val user: User
)