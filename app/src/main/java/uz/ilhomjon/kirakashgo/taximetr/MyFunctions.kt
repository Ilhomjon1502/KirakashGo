package uz.ilhomjon.kirakashgo.taximetr

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_NO_CREATE
import android.content.Context
import android.content.Intent
import android.os.Build
import uz.ilhomjon.kirakashgo.MainActivity
import uz.ilhomjon.kirakashgo.R

object MyFunctions {
    fun createNotification(context: Context, title: String, contentText: String): Notification {
        val channelId = "my_channel_id" // Unikal kanal identifikatori
        createNotificationChannel(context, channelId, "My Channel")

        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(context, 0, notificationIntent, FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, notificationIntent, FLAG_NO_CREATE)
        }
        return Notification.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // Xabarnomaning ikonasi
            .setContentTitle(title) // Xabarnomaning sarlavhasi
            .setContentText(contentText) // Xabarnomaning matni
            .setContentIntent(pendingIntent)
            .build()
    }

    fun createNotificationChannel(context: Context, channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    //oradagi masofani aniqlash
    fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = (Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + (Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2)))
        val c = 2 * Math.asin(Math.sqrt(a))
        val distanceInMeters = Math.round(6371000 * c)
        return distanceInMeters.toDouble()
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}