package uz.ilhomjon.kirakashgo.taximetr

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyLocationService : Service() {
    lateinit var myFindLocation: MyFindLocation
    override fun onCreate() {
        super.onCreate()
        myFindLocation = MyFindLocation(applicationContext)
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        myFindLocation.checkSettingsAndStartUpdates()
        startForeground(1, MyFunctions.createNotification(applicationContext, "Kirakash go ishlamoqda","To'g'ri ishlashi uchun keshdan tozalamang!!!"))

        return START_STICKY
    }
}