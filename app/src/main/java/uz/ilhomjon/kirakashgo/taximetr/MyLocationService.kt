package uz.ilhomjon.kirakashgo.taximetr

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import uz.ilhomjon.kirakashgo.presentation.viewmodel.DriverProfileViewModel
import uz.ilhomjon.kirakashgo.utils.Const.API_KEY
import javax.inject.Inject

@AndroidEntryPoint
class MyLocationService: Service() {
    lateinit var myFindLocation: MyFindLocation
    @Inject
    lateinit var viewModel: DriverProfileViewModel
    override fun onCreate() {
        super.onCreate()
        myFindLocation = MyFindLocation(applicationContext, viewModel)
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        myFindLocation.checkSettingsAndStartUpdates()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(1, MyFunctions.createNotification(applicationContext, "Kirakash go ishlamoqda","To'g'ri ishlashi uchun keshdan tozalamang!!!"))
        }

        return START_STICKY
    }
}