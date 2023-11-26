package uz.ilhomjon.kirakashgo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.data.remote.dto.CheckSmsCodeResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.LoginDriverResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse.GetDriveTokenResponse
import uz.ilhomjon.kirakashgo.domain.repository.DriverRepository
import uz.ilhomjon.kirakashgo.presentation.viewmodel.utils.MyResource
import uz.ilhomjon.kirakashgo.utils.Resource
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class DriverViewModel @Inject constructor(
    private val driverRepository: DriverRepository
) : ViewModel() {


    //LOGIN
    private val loginStateFlow = MutableStateFlow<MyResource<LoginDriverResponse>?>(null)

    fun loginDriver(username: String): MutableStateFlow<MyResource<LoginDriverResponse>?> {
        viewModelScope.launch {
            try {
                coroutineScope {
                    loginStateFlow.value = MyResource.loading("Yuklanmoqda...")
                    val flow = driverRepository.loginDriver(username)

                    flow.collectLatest {
                        if (it.isSuccessful) {
                            loginStateFlow.value = MyResource.success(it.body()!!)
                        } else {
                            loginStateFlow.value =
                                MyResource.error(it.message() + "\nTelefon raqam ro'yhatdan o'tmagan")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.d("test", "loginDriver: internet error")
                loginStateFlow.value = MyResource.error("Internet bilan bog'liq muammo")
            }
        }
        return loginStateFlow
    }


    //CHECK SMS
    private val smsCheckCodeStateFlow = MutableStateFlow<MyResource<CheckSmsCodeResponse>?>(null)
    fun smsCheckCode(
        username: String,
        smsCode: String
    ): MutableStateFlow<MyResource<CheckSmsCodeResponse>?> {
        viewModelScope.launch {
            try {
                coroutineScope {
                    smsCheckCodeStateFlow.value = MyResource.loading("SMS jo'natish kutilmoqda")
                    val flow = driverRepository.smsCheckCode(username, smsCode)
                    flow.collectLatest {
                        if (it.isSuccessful) {
                            smsCheckCodeStateFlow.value = MyResource.success(it.body()!!)
                        }else{
                            smsCheckCodeStateFlow.value = MyResource.error(it.message())
                        }
                    }
                }
            } catch (e: Exception) {
                smsCheckCodeStateFlow.value = MyResource.error("Internet bilan bog'liq muammo")
            }
        }
        return smsCheckCodeStateFlow
    }

    //TOKEN
    private val driverTokenFlow = MutableStateFlow<MyResource<GetDriveTokenResponse>?>(null)
    fun getDriverToken(
        username: String
    ): MutableStateFlow<MyResource<GetDriveTokenResponse>?> {

        viewModelScope.launch {
            driverTokenFlow.value = MyResource.loading("Token olishga harakat qilinmoqda")
            val flow = driverRepository.getDriveToken(username)
            flow
                .catch {
                    Log.d("Test", "getDriverToken: ${it.message}")
                    driverTokenFlow.value = MyResource.error(it.message)
                }
                .collectLatest {
                    Log.d("Test", "getDriverToken: $it")
                    if (it.isSuccessful) {
                        driverTokenFlow.value = MyResource.success(it.body()!!)
                    }else{
                        driverTokenFlow.value = MyResource.error(it.message())
                    }
                }
        }
        return driverTokenFlow
    }
}