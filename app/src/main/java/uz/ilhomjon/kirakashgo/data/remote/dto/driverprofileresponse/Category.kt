package uz.ilhomjon.kirakashgo.data.remote.dto.driverprofileresponse

data class Category(
    val baggage_cost: Int,
    val bonus_percent: Int,
    val id: Int,
    val minimum: Int,
    val sum_for_per_km: Int,
    val type: String,
    val waiting_cost: Int
)