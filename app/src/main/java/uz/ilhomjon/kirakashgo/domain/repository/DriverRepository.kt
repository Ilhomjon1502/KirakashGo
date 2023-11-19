package uz.ilhomjon.kirakashgo.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.ilhomjon.kirakashgo.data.remote.dto.CheckSmsCodeResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.LoginDriverResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse.GetDriveTokenResponse
import uz.ilhomjon.kirakashgo.utils.Resource

interface DriverRepository {
    fun loginDriver(username: String): Flow<Resource<LoginDriverResponse>>
    fun smsCheckCode(username: String, smsCode: String): Flow<Resource<CheckSmsCodeResponse>>
    fun getDriveToken(username: String): Flow<Resource<GetDriveTokenResponse>>
}