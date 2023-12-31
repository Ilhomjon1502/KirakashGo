package uz.ilhomjon.kirakashgo.presentation.models

data class OrdersSocketResponse(
    val baggage: Boolean,
    val client: Any,
    val client_phone: String,
    val costs: List<Cost>,
    val date: String,
    val description: String,
    var destination_lat: String,
    var destination_long: String,
    val destination_name: String,
    val driver: Any,
    val for_women: Boolean,
    val grading_point: Any,
    val id: Int,
    val is_comfort: Boolean,
    val name_startin_place: String,
    var order_status: String,
    val starting_point_lat: String,
    val starting_point_long: String,
    var total_sum: Int,
    val waiting_seconds: Int
){
    companion object{
        lateinit var order:OrdersSocketResponse
    }
}