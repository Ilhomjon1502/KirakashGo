package uz.ilhomjon.kirakashgo.presentation.models

data class OrderAcceptResponse(
    val first_name: String,
    val order_status: String,
    val phone: String,
    val success: Boolean
)