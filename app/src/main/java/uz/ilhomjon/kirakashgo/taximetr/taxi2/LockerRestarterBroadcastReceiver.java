package uz.ilhomjon.kirakashgo.taximetr.taxi2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import uz.ilhomjon.kirakashgo.taximetr.MyLocationService;

public class LockerRestarterBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(LockerRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
        context.startService(new Intent(context, MyLocationService.class));
    }
}