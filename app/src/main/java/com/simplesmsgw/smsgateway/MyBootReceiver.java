package com.simplesmsgw.smsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyBootReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean onboot = pref.getBoolean("onboot", false);
        int port = Integer.parseInt(pref.getString("port", 8080 + ""));
        String password = pref.getString("password", "Password");
        int limit = Integer.parseInt(pref.getString("limit", 100 + ""));

        MyHTTPD.PORT = port;
        MyHTTPD.PASSWORD = password;
        MyHTTPD.LIMIT = limit;

        if(onboot) {
            Intent serviceIntent = new Intent(context, MyService.class);
            serviceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startService(serviceIntent);
        }
    }
}