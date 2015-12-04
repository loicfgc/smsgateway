package com.simplesmsgw.smsgateway;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

public class MyService extends IntentService
{
    private BroadcastReceiver myBootReceiver;
    private static MyService myHTTPDService;

    private MyHTTPD server;

    public MyService()
    {
        super("MyService");
        myHTTPDService = this;
    }

    public static MyService getInstance()
    {
        return myHTTPDService;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show();
        if(myBootReceiver != null) unregisterReceiver(myBootReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Toast.makeText(this, "Service starting", Toast.LENGTH_SHORT).show();
        IntentFilter filter = new IntentFilter();

        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        myBootReceiver = new MyBootReceiver();
        registerReceiver(myBootReceiver, filter);

        try {
            server = new MyHTTPD();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {

    }
}