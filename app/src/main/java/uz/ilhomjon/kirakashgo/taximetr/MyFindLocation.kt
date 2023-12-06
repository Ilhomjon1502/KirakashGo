package uz.ilhomjon.kirakashgo.taximetr

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.data.local.sharedpref.MySharedPreference
import uz.ilhomjon.kirakashgo.data.remote.dto.driverpostlocation.DriverLocationRequest
import uz.ilhomjon.kirakashgo.presentation.models.OrdersSocketResponse
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverProfileViewModel
import uz.ilhomjon.kirakashgo.taximetr.models.MyOrderService
import uz.ilhomjon.kirakashgo.utils.Resource

const val TAG = "MyFindLocation"

//@AndroidEntryPoint
class MyFindLocation(var context: Context, var viewModel: DriverProfileViewModel) {
    val REQUEST_CODE_PERMISSION = 1000
    lateinit var locationRequest: LocationRequest
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    lateinit var myOrderService: MyOrderService
    var locationCallback = object : LocationCallback() {
        override fun onLocationResult(location: LocationResult) {
            if (location == null) {
                return
            }
            for (location: Location in location.locations) {
//                Log.d(TAG, "GetCurrentLocation: ${location.toString()}")
                taximetr(location)
                scope.launch() {
//                    try {
                    MySharedPreference.init(context)
                    viewModel.postLocationDriver(
                        "${MySharedPreference.token.access}",
                        DriverLocationRequest(
                            location.bearing.toString(),
                            location.latitude.toString(),
                            location.longitude.toString()
                        )
                    )
//                    } catch (e: Exception) {
//                        Log.d(TAG, "onLocationResult: ${e.message}")
//                        Toast.makeText(
//                            context,
//                            "Location bazaga jo'natilmayapti",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
                }
            }
        }
    }

    lateinit var handler: Handler

    init {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        locationRequest = LocationRequest.create()
        locationRequest.setInterval(5000)
        locationRequest.setFastestInterval(5000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        myOrderService = MyOrderService()
        checkSettingsAndStartUpdates()
        handler = Handler(Looper.getMainLooper())
    }

    fun checkSettingsAndStartUpdates() {
        val request = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .build()
        val client = LocationServices.getSettingsClient(context)
        val locationSettingsResponseTask: Task<LocationSettingsResponse> =
            client.checkLocationSettings(request)
        locationSettingsResponseTask.addOnSuccessListener {
            //Settings of device are satisfied and we can start location updates
            startLocationUpdates()
        }
        locationSettingsResponseTask.addOnFailureListener {
            Log.d("TestService", "checkSettingsAndStartUpdates: ${it.message}")
            Toast.makeText(
                context,
                "Xatolik \ncheckSettingsAndStartUpdates",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        var ordersSocketResponse: OrdersSocketResponse? = null
        var orderStatus = 0
        var umumiyNarx = 0
        var kutishmi = false
        var kutishVaqti = 0
        var km = 0.0
        val orderServiceLiveData = MutableLiveData<MyOrderService>()
    }

    fun taximetr(location: Location) {
        if (orderStatus == 1) {
            if (ordersSocketResponse != null) {
                if (kutishmi) {
                    kutishHolatdaHisoblash()
                } else {
                    yoldaYurishniHisoblash(location)
                }

                if (isHandler) {
                    handler.postDelayed(runnable, 1000)
                    isHandler = false
                }

            }
        } else if (orderStatus == 0) {
            km = 0.0
            umumiyNarx = 0
            kutishVaqti = 0
            kutishmi = false
        }
    }

    var beforeLocation: Location? = null
    fun yoldaYurishniHisoblash(location: Location) {
        if (beforeLocation == null) {
            beforeLocation = location
        } else {
            km += MyFunctions.distance(
                beforeLocation!!.latitude,
                beforeLocation!!.longitude,
                location.latitude,
                location.longitude
            ) / 1000.0
            beforeLocation = location
        }
    }


    var isHandler = true
    fun kutishHolatdaHisoblash() {

    }

    private val runnable = object : Runnable {
        override fun run() {

            //kutish rejimida
            if (kutishmi) {
                kutishVaqti += 1
            }

            var km2 = km
            if (km >= 1) {
                km2 -= 1
            }
            if (ordersSocketResponse!!.is_comfort) {
                umumiyNarx = ((km2) * ordersSocketResponse!!.costs[1].sum_for_per_km
                        + ordersSocketResponse!!.costs[1].waiting_cost * kutishVaqti / 60
                        + ordersSocketResponse!!.total_sum)
                    .toInt()
            } else {
                umumiyNarx = ((km2) * ordersSocketResponse!!.costs[0].sum_for_per_km
                        + ordersSocketResponse!!.costs[0].waiting_cost * kutishVaqti / 60
                        + ordersSocketResponse!!.total_sum)
                    .toInt()
            }

            orderServiceLiveData.postValue(
                MyOrderService(
                    orderStatus,
                    umumiyNarx,
                    kutishVaqti,
                    ordersSocketResponse!!.id,
                    beforeLocation,
                    km
                )
            )
            handler.postDelayed(this, 1000)
        }
    }
}