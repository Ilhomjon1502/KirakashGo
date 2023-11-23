package uz.ilhomjon.kirakashgo.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.data.remote.dto.driverprofileresponse.DriverProfileResponse
import uz.ilhomjon.kirakashgo.domain.repository.DriverRepository
import javax.inject.Inject

@HiltViewModel
class DriverProfileViewModel @Inject constructor(
    private val driverRepository: DriverRepository
):ViewModel() {
    private val stateFlow=MutableStateFlow<DriverProfileResponse?>(null)

    fun getDriverProfile(apiKey:String):MutableStateFlow<DriverProfileResponse?>{
        try {
            viewModelScope.launch {
                val flow=driverRepository.getDriverPorfile(apiKey)
                flow.collectLatest {
                    Log.d("DriverProfileViewModel", "getDriverProfile: ${it}")
                    if (it.isSuccessful){
                        stateFlow.value=it.body()
                    }
                }
            }
        }catch (e:Exception){
            Log.d("DriverProfileViewModel", "getDriverProfile: Internet error")
        }
        return stateFlow
    }
}