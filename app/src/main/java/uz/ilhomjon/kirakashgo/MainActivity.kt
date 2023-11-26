package uz.ilhomjon.kirakashgo

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.github.florent37.runtimepermission.kotlin.askPermission
import dagger.hilt.android.AndroidEntryPoint
import uz.ilhomjon.kirakashgo.taximetr.MyLocationService
import uz.ilhomjon.kirakashgo.taximetr.websocket.MyWebSocketClient
import java.net.URI

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationService()
        connectWebSocket()
    }

    fun locationService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            askPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION, Manifest.permission.FOREGROUND_SERVICE){
                //all permissions already granted or just granted
                ContextCompat.startForegroundService(this, Intent(this, MyLocationService::class.java))
            }.onDeclined { e ->
                if (e.hasDenied()) {

                    AlertDialog.Builder(this)
                        .setMessage("Ushbu ruxsatlarni bermasangiz, taximert ishlamaydi")
                        .setPositiveButton("Xo'p beraman") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("Ishlamasa ham mayli") { dialog, which ->
                            dialog.dismiss();
                        }
                        .show();
                }

                if(e.hasForeverDenied()) {
                    //the list of forever denied permissions, user has check 'never ask again'

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
        }else{
            askPermission(Manifest.permission.ACCESS_FINE_LOCATION){
                //all permissions already granted or just granted
                ContextCompat.startForegroundService(this, Intent(this, MyLocationService::class.java))
            }.onDeclined { e ->
                if (e.hasDenied()) {

                    AlertDialog.Builder(this)
                        .setMessage("Ushbu ruxsatlarni bermasangiz, taximert ishlamaydi")
                        .setPositiveButton("Xo'p beraman") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("Ishlamasa ham mayli") { dialog, which ->
                            dialog.dismiss();
                        }
                        .show();
                }

                if(e.hasForeverDenied()) {
                    //the list of forever denied permissions, user has check 'never ask again'

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
        }

    }

    fun connectWebSocket(){
        val serverUri = URI("ws://147.182.206.31/ws/orders/?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzMyMzU0ODI2LCJpYXQiOjE3MDA4MTg4MjYsImp0aSI6Ijg3ZDc5NDNjMDk5ODQ2ODNiNjA5OTQwMDhmYjExYmMxIiwidXNlcl9pZCI6MTd9.D-8ZUxZt7hyxV_wbGpeNHpbjWlHgZRyyk0I1bEPM4W0") // Websocket server manzili
        val client = MyWebSocketClient(serverUri)

        try {
            client.connect() // Websocketni ulash
            Toast.makeText(this, "Connected webSocket", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}