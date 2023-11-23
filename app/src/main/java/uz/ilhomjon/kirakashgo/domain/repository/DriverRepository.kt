package uz.ilhomjon.kirakashgo.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import uz.ilhomjon.kirakashgo.data.remote.ApiService
import uz.ilhomjon.kirakashgo.data.remote.GetTokenData
import uz.ilhomjon.kirakashgo.data.remote.dto.CheckSmsCodeResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.LoginDriverResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.driverprofileresponse.DriverProfileResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse.GetDriveTokenResponse
import uz.ilhomjon.kirakashgo.utils.Resource
import javax.inject.Inject

class DriverRepository @Inject constructor(
    private val apiService: ApiService
) {
    //Register
    suspend fun loginDriver(username: String): Flow<Response<LoginDriverResponse>> =
        flow { emit(apiService.loginDriver(username)) }

    suspend fun smsCheckCode(
        username: String,
        smsCode: String
    ): Flow<Response<CheckSmsCodeResponse>> =
        flow { emit(apiService.smsCheckCode(username, smsCode)) }

    suspend fun getDriveToken(username: String): Flow<Response<GetDriveTokenResponse>> =
        flow { emit(apiService.getDriverToken(GetTokenData(username))) }


    //Profile
    suspend fun getDriverPorfile(apiKey:String):Flow<Response<DriverProfileResponse>> =
        flow { emit(apiService.driverProfile(apiKey)) }

}