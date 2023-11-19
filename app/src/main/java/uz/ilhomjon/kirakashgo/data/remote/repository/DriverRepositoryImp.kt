package uz.ilhomjon.kirakashgo.data.remote.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.ilhomjon.kirakashgo.data.remote.ApiService
import uz.ilhomjon.kirakashgo.data.remote.dto.CheckSmsCodeResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.LoginDriverResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse.GetDriveTokenResponse
import uz.ilhomjon.kirakashgo.domain.repository.DriverRepository
import uz.ilhomjon.kirakashgo.utils.Resource

class DriverRepositoryImp(
    private val apiService: ApiService,
    private val ioDispatcher: CoroutineDispatcher
) : DriverRepository {
    override fun loginDriver(username: String): Flow<Resource<LoginDriverResponse>> {
        return flow<Resource<LoginDriverResponse>> {
            emit(Resource.Loading())
            when (val response = apiService.loginDriver(username)) {
                is Resource.Error -> {
                    emit(Resource.Error("Something error", null))
                }

                is Resource.Loading -> {
                    emit(Resource.Loading())
                }

                is Resource.Success -> {
                    emit(Resource.Success(response.data))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override fun smsCheckCode(
        username: String,
        smsCode: String
    ): Flow<Resource<CheckSmsCodeResponse>> {
        return flow {
            emit(Resource.Loading())

            when (val response = apiService.smsCheckCode(username, smsCode)) {
                is Resource.Error -> {
                    emit(Resource.Error("Something error"))
                }

                is Resource.Loading -> {
                    emit(Resource.Loading())
                }

                is Resource.Success -> {
                    emit(Resource.Success(response.data))
                }
            }
        }.flowOn(ioDispatcher)
    }

    override fun getDriveToken(username: String): Flow<Resource<GetDriveTokenResponse>> {
        return flow {
            emit(Resource.Loading())
            when (val response = apiService.getDriverToken(username)) {
                is Resource.Error -> {
                    emit(Resource.Error("Something wrong"))
                }

                is Resource.Loading -> {
                    emit(Resource.Loading())
                }

                is Resource.Success -> {
                    emit(Resource.Success(response.data))
                }
            }
        }.flowOn(ioDispatcher)
    }
}