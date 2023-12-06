package uz.ilhomjon.kirakashgo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.data.remote.dto.driverpostlocation.DriverLocationRequest
import uz.ilhomjon.kirakashgo.data.remote.dto.driverpostlocation.DriverLocationResponse
import uz.ilhomjon.kirakashgo.data.remote.dto.driverprofileresponse.DriverProfileResponse
import uz.ilhomjon.kirakashgo.domain.repository.DriverRepository
import uz.ilhomjon.kirakashgo.presentation.models.OrderAcceptResponse
import uz.ilhomjon.kirakashgo.presentation.viewmodel.utils.MyResource
import uz.ilhomjon.kirakashgo.utils.Resource
import javax.inject.Inject

private const val TAG = "DriverProfileViewModel"

@HiltViewModel
class DriverProfileViewModel @Inject constructor(
    private val driverRepository: DriverRepository
) : ViewModel() {
    private val stateFlow = MutableStateFlow<MyResource<DriverProfileResponse>?>(null)

    fun getDriverProfile(apiKey: String): MutableStateFlow<MyResource<DriverProfileResponse>?> {
        viewModelScope.launch {
            try {
                stateFlow.value = MyResource.loading("Yuklanmoqda")
                val flow = driverRepository.getDriverPorfile(apiKey)
                flow.collectLatest {
                    Log.d("DriverProfileViewModel", "getDriverProfile: ${it}")
                    if (it.isSuccessful) {
                        stateFlow.value = MyResource.success(it.body()!!)
                    } else {
                        stateFlow.value = MyResource.error("${it.message()}")
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "getDriverProfile: ${e.message}")
                stateFlow.value = MyResource.error("Internet bilan bog'liq muammo")
            }
        }
        return stateFlow
    }



    private val _postFlow = MutableStateFlow<Resource<DriverLocationResponse?>>(Resource.Loading())
    val postFlow = _postFlow.asStateFlow()
    fun postLocationDriver(
        key: String,
        driverLocationRequest: DriverLocationRequest
    ) {

        viewModelScope.launch {
            driverRepository.postLocationDriver(key, driverLocationRequest)
                .collect {
                    Log.d("DriverProfileViewModel", "getDriverProfile: ${it}")
                    if (it.isSuccessful) {
                        _postFlow.emit(Resource.Success(it.body()))
                    }
                }
        }
    }


    //orders
    private val orderAcceptFlow = MutableStateFlow<MyResource<OrderAcceptResponse>?>(null)
    suspend fun acceptOrder(
        token: String,
        id: Int
    ): MutableStateFlow<MyResource<OrderAcceptResponse>?> {

        orderAcceptFlow.value = MyResource.loading("Buyurtmani olishga harakat qilinmoqda")
        val flow = driverRepository.acceptOrder(token, id)
        flow
            .catch {
                Log.d(TAG, "acceptOrder: ${it}")
                orderAcceptFlow.value = MyResource.error(it.message)
            }
            .collectLatest {
                Log.d(TAG, "acceptOrder: $it")
                if (it.isSuccessful) {
                    orderAcceptFlow.value = MyResource.success(it.body()!!)
                } else {
                    orderAcceptFlow.value = MyResource.error(it.message())
                }
                Log.d(TAG, "acceptOrder: $it")
            }

        return orderAcceptFlow
    }

    private val orderCancelFlow = MutableStateFlow<MyResource<OrderAcceptResponse>?>(null)
    suspend fun cancelOrder(
        token: String,
        id: Int
    ): MutableStateFlow<MyResource<OrderAcceptResponse>?> {

        orderCancelFlow.value = MyResource.loading("Buyurtmani bekor qilishga harakat qilinmoqda")
        val flow = driverRepository.cancelOrder(token, id)
        flow
            .catch {
                Log.d(TAG, "cancelOrder: $it")
                orderCancelFlow.value = MyResource.error(it.message)
            }
            .collectLatest {
                Log.d(TAG, "cancelOrder: $it")
                if (it.isSuccessful) {
                    orderCancelFlow.value = MyResource.success(it.body()!!)
                } else {
                    orderCancelFlow.value = MyResource.error(it.message())
                }
                Log.d(TAG, "cancelOrder: $it")
            }

        return orderCancelFlow
    }


    private val orderStartFlow = MutableStateFlow<MyResource<OrderAcceptResponse>?>(null)
    suspend fun startOrder(
        token: String,
        id: Int
    ): MutableStateFlow<MyResource<OrderAcceptResponse>?> {

        orderStartFlow.value = MyResource.loading("Buyurtmani Start qilishga harakat qilinmoqda")
        val flow = driverRepository.startOrder(token, id)
        flow
            .catch {
                Log.d(TAG, "startOrder: $it")
                orderStartFlow.value = MyResource.error(it.message)
            }
            .collectLatest {
                Log.d(TAG, "startOrder: $it")
                if (it.isSuccessful) {
                    orderStartFlow.value = MyResource.success(it.body()!!)
                } else {
                    orderStartFlow.value = MyResource.error(it.message())
                }
            }
        return orderStartFlow
    }

    private val orderFinishFlow = MutableStateFlow<MyResource<OrderAcceptResponse>?>(null)
    suspend fun finishOrder(
        token: String,
        id: Int,
        des_lat: String,
        des_long: String,
        total_sum: String
    ): MutableStateFlow<MyResource<OrderAcceptResponse>?> {

        orderFinishFlow.value = MyResource.loading("Buyurtmani tugatishga harakat qilinmoqda")
        val flow = driverRepository.finishOrder(token, id, des_lat, des_long, total_sum)
        flow
            .catch {
                Log.d(TAG, "finishOrder: $it")
                orderFinishFlow.value = MyResource.error(it.message)
            }
            .collectLatest {
                Log.d(TAG, "finishOrder: $it")
                if (it.isSuccessful) {
                    orderFinishFlow.value = MyResource.success(it.body()!!)
                } else {
                    orderFinishFlow.value = MyResource.error(it.message())
                }
            }
        return orderFinishFlow
    }
}