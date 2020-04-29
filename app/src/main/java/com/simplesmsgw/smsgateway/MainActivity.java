package com.simplesmsgw.smsgateway;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent myServiceIntent;
    private static final int PREF = 20;
    boolean stop = true;
    private static final int SMS_PERMISSION = 100;
    protected PowerManager.WakeLock mWakeLock;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);
        checkPermission(Manifest.permission.SEND_SMS, SMS_PERMISSION);
        /* This code together with the one in onDestroy()
         * will make the screen be always on until this Activity gets destroyed. */
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");
        this.mWakeLock.acquire();

        getPref();
    }
    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[] { permission },
                            requestCode);
        }
        else {
            Toast.makeText(MainActivity.this,
                            "Les SMS sont fonctionnels !",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }


    public void getPref() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int port = Integer.parseInt(pref.getString("port", 8080 + ""));
        String password = pref.getString("password", "EON");
        int limit = Integer.parseInt(pref.getString("limit", 100 + ""));
        String server = pref.getString("server", "127.0.0.1:8888");

        MyHTTPD.PORT = port;
        MyHTTPD.PASSWORD = password;
        MyHTTPD.LIMIT = limit;
        MyHTTPD.SERVER = server;

        //get Wifi IP
        String ipAddress = "127.0.0.1";
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        if(wifiMgr != null) {
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            int ip = wifiInfo.getIpAddress();
            ipAddress = String.format("%d.%d.%d.%d", (ip & 0xff),(ip >> 8 & 0xff),(ip >> 16 & 0xff),(ip >> 24 & 0xff));
        }

        TextView tvOutgoing = (TextView) findViewById(R.id.tvOutgoing);
        tvOutgoing.setText("Pour envoyer un SMS - URL Broker");

        TextView tvIncoming = (TextView) findViewById(R.id.tvIncoming);
        tvIncoming.setText("http://"+ipAddress+":"+port+"/send?password="+password+"&toNum=[num]&message=[message]");

        //TextView tvIncoming = (TextView) findViewById(R.id.tvIncoming);
       // tvIncoming.setText("INCOMING : GET http://"+MyHTTPD.SERVER+"/receive?fromNum=[num]&message=[message]");

    }

    public void startService() {
        getPref();

        myServiceIntent = new Intent(this, MyService.class);
        startService(myServiceIntent);
    }

    public void stopService() {
        myServiceIntent = new Intent(this, MyService.class);
        stopService(myServiceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
                Intent ip = new Intent(this, MyPreferenceActivity.class);
                startActivityForResult(ip, MainActivity.PREF);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MainActivity.PREF) {
            stopService();
            startService();
        }
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnStart) {
            if(stop) {
                startService();
                ((Button) findViewById(R.id.btnStart)).setText("Arreter le broker");
                stop = false;
            }
            else {
                stopService();
                ((Button) findViewById(R.id.btnStart)).setText("Démarrer le broker");
                stop = true;
            }
        }

    }

    @Override
    public void onDestroy() {
        this.mWakeLock.release();
        super.onDestroy();
    }
}
