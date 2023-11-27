package uz.ilhomjon.kirakashgo.presentation.models

data class Cost(
    val id: Int,
    val sum_for_per_km: Int,
    val type: String,
    val waiting_cost: Int
)