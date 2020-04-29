package com.simplesmsgw.smsgateway;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

public class MyService extends IntentService
{

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
        server.stop();
        Toast.makeText(this, "Broker OFF", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Toast.makeText(this, "Broker ON", Toast.LENGTH_SHORT).show();

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