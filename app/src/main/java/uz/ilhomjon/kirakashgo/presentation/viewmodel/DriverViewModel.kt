package uz.ilhomjon.kirakashgo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
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


    //LOGIN
    private val loginStateFlow = MutableStateFlow<LoginDriverResponse?>(null)

    fun loginDriver(username: String): MutableStateFlow<LoginDriverResponse?> {
        viewModelScope.launch {
            val flow = driverRepository.loginDriver(username)

            flow.collectLatest {
                if (it.isSuccessful) {
                    loginStateFlow.value = it.body()
                }
            }
        }
        return loginStateFlow
    }


    //CHECK SMS
    private val smsCheckCodeStateFlow = MutableStateFlow<CheckSmsCodeResponse?>(null)
    fun smsCheckCode(username: String, smsCode: String): MutableStateFlow<CheckSmsCodeResponse?> {
        viewModelScope.launch {
            val flow = driverRepository.smsCheckCode(username, smsCode)
            flow.collectLatest {
                if (it.isSuccessful) {
                    smsCheckCodeStateFlow.value = it.body()
                }
            }
        }
        return smsCheckCodeStateFlow
    }

    //TOKEN
    private val driverTokenFlow = MutableStateFlow<GetDriveTokenResponse?>(null)
    fun getDriverToken(
        username: String
    ): MutableStateFlow<GetDriveTokenResponse?> {

        viewModelScope.launch {
            val flow = driverRepository.getDriveToken(username)
            flow
                .catch {
                    Log.d("Test", "getDriverToken: ${it.message}")
                }
                .collectLatest {
                    Log.d("Test", "getDriverToken: $it")
                    if (it.isSuccessful) {
                        driverTokenFlow.value = it.body()
                    }
                }
        }
        return driverTokenFlow
    }
}