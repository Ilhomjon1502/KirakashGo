package uz.ilhomjon.kirakashgo.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import uz.ilhomjon.kirakashgo.data.remote.dto.CheckSmsCodeResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.driverpostlocation.DriverLocationRequest
import uz.ilhomjon.kirakashgo.data.remote.dto.driverpostlocation.DriverLocationResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.LoginDriverResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.driverprofileresponse.DriverProfileResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse.GetDriveTokenResponse
import uz.ilhomjon.kirakashgo.presentation.models.OrderAcceptResponse

interface ApiService {

    //LOGIN
    @POST("drivers/login/")
    suspend fun loginDriver(
        @Query("username") username: String
    ): Response<LoginDriverResponse>

    @POST("drivers/sms_chackcode/")
    suspend fun smsCheckCode(
        @Query("username") username: String, @Query("sms_code") sms_code: String
    ): Response<CheckSmsCodeResponse>
    @POST("user/driver_token/")
    suspend fun getDriverToken(
        @Body() data: GetTokenData
    ): Response<GetDriveTokenResponse>


    //ORDER
    @PUT("drivers/accept_order/")
    suspend fun acceptOrder(
        @Header("Authorization") token: String,
        @Query("order_id") order_id: Int
    ):Response<OrderAcceptResponse>

    @PUT("drivers/cancel_order/")
    suspend fun cancelOrder(
        @Header("Authorization") token: String,
        @Query("order_id") order_id: Int
    ):Response<OrderAcceptResponse>

    @PUT("drivers/start_order/")
    suspend fun startOrder(
        @Header("Authorization") token: String,
        @Query("order_id") order_id: Int
    ):Response<OrderAcceptResponse>

    @PUT("drivers/finish_order")
    suspend fun finishOrder(
        @Header("Authorization") token: String,
        @Query("order_id") order_id: Int,
        @Query("destination_lat") destination_lat: String,
        @Query("destination_long") destination_long: String,
        @Query("total_sum") total_sum: String,
    ):Response<OrderAcceptResponse>


    //LOCATION
    @POST("drivers/location/")
    suspend fun driverLocation(
        @Header("Authorization") token: String,
        @Body driverLocationRequest: DriverLocationRequest
    ): Response<DriverLocationResponse>

    //PROFILE
    @GET("drivers/profil/")
    suspend fun driverProfile(
        @Header("Authorization") token: String
    ):Response<DriverProfileResponse>
}

//eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzMyNjgyNDM5LCJpYXQiOjE3MDExNDY0MzksImp0aSI6IjRiMWYyYTc1ZWVlMDRiOGQ4MDE4YzE3YTExOWMxMjc0IiwidXNlcl9pZCI6MTd9.1guYAWmM9J0laAb9CIVuyOVaKVe9mqslRXRD76BVAE0

//operator abbobek:
// eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzMyMTkxMTg5LCJpYXQiOjE3MDA2NTUxODksImp0aSI6IjFiZDM5NGRmNzQwZTRjMGU5ZjQ1NzA5YWI0NzUzZTE3IiwidXNlcl9pZCI6Mn0.PBhsxhZWsnmSzVwMKXmD7UdXXo0DLwLEAQxvKnTRlec

//eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzMyODkyMzkzLCJpYXQiOjE3MDEzNTYzOTMsImp0aSI6IjRlZjNmYzIxMDEyNTRhYWNhYzM4MGVhY2RlMmNmNTM2IiwidXNlcl9pZCI6MTV9.zysFzTbyAHP4vy8tlz7yzACrJhGP9KA7R_i_otablJ0