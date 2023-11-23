package uz.ilhomjon.kirakashgo.data.remote

import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import uz.ilhomjon.kirakashgo.data.remote.dto.CheckSmsCodeResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.DriverLocationResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.LoginDriverResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.driverprofileresponse.DriverProfileResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse.GetDriveTokenResponse
import uz.ilhomjon.kirakashgo.utils.Resource

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
        @Query("order_id") order_id: String
    )

    @PUT("drivers/start_order/")
    suspend fun startOrder(
        @Query("order_id") order_id: String
    )

    @PUT("drivers/finish_order/")
    suspend fun finishOrder(
        @Query("order_id") order_id: String,
        @Query("destination_lat") destination_lat: String,
        @Query("destination_long") destination_long: String,
        @Query("total_sum") total_sum: String,
    )


    //LOCATION
    @POST("drivers/location/")
    suspend fun driverLocation(
        @Query("longitude") longitude: String, @Query("latitude") latitude: String
    ): DriverLocationResponse

    //PROFILE
    @POST("drivers/profil/")
    suspend fun driverProfile(
        @Header("Authorization") token: String
    ):Response<DriverProfileResponse>
}

//eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzMxNjc1MDcwLCJpYXQiOjE3MDAxMzkwNzAsImp0aSI6IjhjNWU1OWNiNjUxMDQwZmFhNjE4ZWM4YTNlMjUwYjNlIiwidXNlcl9pZCI6NH0.OYIbT8TYdLN_30fmg7x7ms1q4_cMKKPXXxMBt0YHlG8