package com.simplesmsgw.smsgateway;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent myServiceIntent;

    private static final int PREF = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(this);

        Button btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(this);

        getPref();
    }

    public void getPref() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        int port = Integer.parseInt(pref.getString("port", 8080 + ""));
        String password = pref.getString("password", "Password");
        int limit = Integer.parseInt(pref.getString("limit", 100 + ""));

        MyHTTPD.PORT = port;
        MyHTTPD.PASSWORD = password;
        MyHTTPD.LIMIT = limit;

        String numero = ((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getLine1Number();
        TextView tvUrl = (TextView) findViewById(R.id.tvURL);
        tvUrl.setText("http://127.0.0.1:"+port+"/?numero="+numero+"&password="+password+"&message=HelloWorld");
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
            startService();
        }
        else if (v.getId() == R.id.btnStop) {
            stopService();
        }
    }
}
