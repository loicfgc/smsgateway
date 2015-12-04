package com.simplesmsgw.smsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by erkan.valentin on 04/12/2015.
 */
public class MyBootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        //if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
        //{
        // Phone is booted, lets start the service
        Intent serviceIntent = new Intent(context, MyService.class);
        serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(serviceIntent);
        //}
    }


}