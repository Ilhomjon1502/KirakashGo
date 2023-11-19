package uz.ilhomjon.kirakashgo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.data.remote.dto.CheckSmsCodeResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.LoginDriverResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse.GetDriveTokenResponse
import uz.ilhomjon.kirakashgo.domain.repository.DriverRepository
import uz.ilhomjon.kirakashgo.utils.Resource
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class DriverViewModel @Inject constructor(
    private val driverRepository: DriverRepository
) : ViewModel() {

    private val loginStateFlow = MutableStateFlow<Resource<LoginDriverResponse>>(Resource.Loading())

    fun loginDriver(username: String): MutableStateFlow<Resource<LoginDriverResponse>> {
        viewModelScope.launch {
            driverRepository.loginDriver(username).collectLatest { response ->
                when (response) {
                    is Resource.Error -> loginStateFlow.value = Resource.Error("something wrong")
                    is Resource.Loading -> loginStateFlow.value = Resource.Loading()
                    is Resource.Success -> loginStateFlow.value = Resource.Success(response.data)
                }
            }
        }
        return loginStateFlow
    }

    private val checkSmsCodeStateFlow =
        MutableStateFlow<Resource<CheckSmsCodeResponse>>(Resource.Loading())

    fun smsCodeCheck(
        username: String, smsCode: String
    ): MutableStateFlow<Resource<CheckSmsCodeResponse>> {
        viewModelScope.launch {
            driverRepository.smsCheckCode(username, smsCode).collectLatest { response ->
                when (response) {
                    is Resource.Error -> {
                        checkSmsCodeStateFlow.value = Resource.Error("Something error")
                    }

                    is Resource.Loading -> {
                        checkSmsCodeStateFlow.value = Resource.Loading()
                    }

                    is Resource.Success -> {
                        checkSmsCodeStateFlow.value = Resource.Success(response.data)
                    }
                }
            }
        }
        return checkSmsCodeStateFlow
    }

    private val driverTokenStateFlow =
        MutableStateFlow<Resource<GetDriveTokenResponse>>(Resource.Loading())

    fun getDriveToken(username: String): MutableStateFlow<Resource<GetDriveTokenResponse>> {
        viewModelScope.launch {
            driverRepository.getDriveToken(username).collectLatest { response ->
                when (response) {
                    is Resource.Error -> {
                        driverTokenStateFlow.value = Resource.Error("Something went wrong")
                    }

                    is Resource.Loading -> {
                        driverTokenStateFlow.value = Resource.Loading()
                    }

                    is Resource.Success -> {
                        driverTokenStateFlow.value = Resource.Success(response.data)
                    }
                }
            }
        }
        return driverTokenStateFlow
    }
}