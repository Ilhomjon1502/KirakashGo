package uz.ilhomjon.kirakashgo.data.remote.dto

data class DriverLocationResponse(
    var id: Int,
    var longitude: String,
    var latitude: String,
    var date: String,
    var driver: String
)