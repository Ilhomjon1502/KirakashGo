package uz.ilhomjon.kirakashgo.data.remote.dto.driverprofileresponse

data class Data(
    val balance: Int,
    val car_color: String,
    val car_number: String,
    val car_type: String,
    val category: Category,
    val first_name: String,
    val gender: String,
    val has_baggage: Boolean,
    val id: Int,
    val last_name: String,
    val phone: String,
    val role: String,
    val username: String
)