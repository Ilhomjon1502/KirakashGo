package uz.ilhomjon.kirakashgo.taximetr

import android.app.ActivityManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverProfileViewModel
import uz.ilhomjon.kirakashgo.taximetr.taxi2.AlarmUtils
import uz.ilhomjon.kirakashgo.utils.Const.API_KEY
import uz.ilhomjon.kirakashgo.utils.Resource
import javax.inject.Inject

@AndroidEntryPoint
class MyLocationService : Service() {
    var myFindLocation: MyFindLocation?=null

    @Inject
    lateinit var viewModel: DriverProfileViewModel
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if (isMyServiceRunning(MyLocationService::class.java)) {
            startService(Intent(this, MyLocationService::class.java))
        }
        myFindLocation=null
        myFindLocation = MyFindLocation(applicationContext, viewModel)
        AlarmUtils.cancelActivityStarter()
        AlarmUtils.setAlarm(this, 10000)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        myFindLocation?.checkSettingsAndStartUpdates()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            startForeground(
                1,
                MyFunctions.createNotification(
                    applicationContext,
                    "Kirakash go ishlamoqda",
                    "To'g'ri ishlashi uchun keshdan tozalamang!!!"
                )
            )
            getCurrentLocation()
        }

        return START_STICKY
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        Log.d("@@@", "onTaskRemoved: ")
        super.onTaskRemoved(rootIntent)
    }

    override fun onDestroy() {
        Log.d("@@@", "onDestroy: ")
        super.onDestroy()
    }

    fun getCurrentLocation() {
        scope.launch {
            viewModel.postFlow.collect {
                when (it) {
                    is Resource.Error -> {
                        Log.d("ServiceCheck", "getCurrentLocation: ${it.message}")
                    }

                    is Resource.Loading -> {
                        Log.d("ServiceCheck", "getCurrentLocation: Loading")
                    }

                    is Resource.Success -> {
                        Log.d("ServiceCheck", "getCurrentLocation: $it")
                    }
                }
            }
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("isMyServiceRunning?", true.toString() + "")
                return true
            }
        }
        Log.i("isMyServiceRunning?", false.toString() + "")
        return false
    }
}