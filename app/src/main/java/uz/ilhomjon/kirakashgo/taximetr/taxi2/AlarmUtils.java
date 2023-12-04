package uz.ilhomjon.kirakashgo.taximetr.taxi2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import java.util.Objects;

import uz.ilhomjon.kirakashgo.MainActivity;

public class AlarmUtils {
    @Nullable
    static PendingIntent pendingIntent;

    public static void setAlarm(Context context, long intervalMillis) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, LockerRestarterBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + intervalMillis,
                intervalMillis,
                pendingIntent
        );
    }

    public static void cancelActivityStarter() {
        if (pendingIntent != null) {
            pendingIntent.cancel();
        }
    }

}