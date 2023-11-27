package uz.ilhomjon.kirakashgo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.data.remote.dto.DriverLocationRequest
import uz.ilhomjon.kirakashgo.data.remote.dto.DriverLocationResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.driverprofileresponse.DriverProfileResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.tokenresponse.GetDriveTokenResponse
import uz.ilhomjon.kirakashgo.domain.repository.DriverRepository
import uz.ilhomjon.kirakashgo.presentation.models.OrderAcceptResponse
import uz.ilhomjon.kirakashgo.presentation.viewmodel.utils.MyResource
import javax.inject.Inject

private const val TAG = "DriverProfileViewModel"
@HiltViewModel
class DriverProfileViewModel @Inject constructor(
    private val driverRepository: DriverRepository
):ViewModel() {
    private val stateFlow=MutableStateFlow<MyResource<DriverProfileResponse>?>(null)

    fun getDriverProfile(apiKey:String):MutableStateFlow<MyResource<DriverProfileResponse>?>{
            viewModelScope.launch {
                try {
                    stateFlow.value = MyResource.loading("Yuklanmoqda")
                    val flow = driverRepository.getDriverPorfile(apiKey)
                    flow.collectLatest {
                        Log.d("DriverProfileViewModel", "getDriverProfile: ${it}")
                        if (it.isSuccessful){
                            stateFlow.value = MyResource.success(it.body()!!)
                        }else{
                            stateFlow.value = MyResource.error("${it.message()}")
                        }
                    }
                }catch (e:Exception){
                    Log.d(TAG, "getDriverProfile: ${e.message}")
                    stateFlow.value = MyResource.error("Internet bilan bog'liq muammo")
                }
            }
        return stateFlow
    }

    private val postFlow = MutableStateFlow<DriverLocationResponse?>(null)
    fun postLocationDriver(key:String, driverLocationRequest:DriverLocationRequest):MutableStateFlow<DriverLocationResponse?>{

        viewModelScope.launch {
            try {
                val flow=driverRepository.postLocationDriver(key, driverLocationRequest)
                flow.collectLatest {
                    Log.d("DriverProfileViewModel", "getDriverProfile: ${it}")
                        postFlow.value=it
                }
            } catch (e: Exception) {
                Log.d(TAG, "postLocationDriver: ${e.message}")
            }
        }

        return postFlow
    }


    //orders
    private val orderAcceptFlow = MutableStateFlow<MyResource<OrderAcceptResponse>?>(null)
    fun acceptOrder(
        token:String,
        id:Int
    ): MutableStateFlow<MyResource<OrderAcceptResponse>?> {

        viewModelScope.launch {
            orderAcceptFlow.value = MyResource.loading("Buyurtmani olishga harakat qilinmoqda")
            val flow = driverRepository.acceptOrder(token, id)
            flow
                .catch {
                    Log.d("Test", "getDriverToken: ${it.message}")
                    orderAcceptFlow.value = MyResource.error(it.message)
                }
                .collectLatest {
                    Log.d("Test", "getDriverToken: $it")
                    if (it.isSuccessful) {
                        orderAcceptFlow.value = MyResource.success(it.body()!!)
                    }else{
                        orderAcceptFlow.value = MyResource.error(it.message())
                    }
                }
        }
        return orderAcceptFlow
    }

    private val orderStartFlow = MutableStateFlow<MyResource<OrderAcceptResponse>?>(null)
    fun startOrder(
        token:String,
        id:Int
    ): MutableStateFlow<MyResource<OrderAcceptResponse>?> {

        viewModelScope.launch {
            orderStartFlow.value = MyResource.loading("Buyurtmani Start qilishga harakat qilinmoqda")
            val flow = driverRepository.acceptOrder(token, id)
            flow
                .catch {
                    Log.d("Test", "getDriverToken: ${it.message}")
                    orderStartFlow.value = MyResource.error(it.message)
                }
                .collectLatest {
                    Log.d("Test", "getDriverToken: $it")
                    if (it.isSuccessful) {
                        orderStartFlow.value = MyResource.success(it.body()!!)
                    }else{
                        orderStartFlow.value = MyResource.error(it.message())
                    }
                }
        }
        return orderStartFlow
    }

    private val orderFinishFlow = MutableStateFlow<MyResource<OrderAcceptResponse>?>(null)
    fun finishOrder(
        token:String,
        id:Int
    ): MutableStateFlow<MyResource<OrderAcceptResponse>?> {

        viewModelScope.launch {
            orderFinishFlow.value = MyResource.loading("Buyurtmani tugatishga harakat qilinmoqda")
            val flow = driverRepository.acceptOrder(token, id)
            flow
                .catch {
                    Log.d("Test", "getDriverToken: ${it.message}")
                    orderFinishFlow.value = MyResource.error(it.message)
                }
                .collectLatest {
                    Log.d("Test", "getDriverToken: $it")
                    if (it.isSuccessful) {
                        orderFinishFlow.value = MyResource.success(it.body()!!)
                    }else{
                        orderFinishFlow.value = MyResource.error(it.message())
                    }
                }
        }
        return orderFinishFlow
    }
}