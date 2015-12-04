package com.simplesmsgw.smsgateway;

import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by erkan.valentin on 04/12/2015.
 */
public class MyHTTPD extends NanoHTTPD {
    private Handler handler;

    private final static int PORT = 8080;
    private final static String PASSWORD = "Password";

    public MyHTTPD() throws IOException {
        super(PORT);
        start();
        Log.d("SERVER", "Start on port" + PORT);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Log.d("SERVER","Get a request: " + session.toString());

        try {
            String numero = session.getParms().get("numero");
            String password = session.getParms().get("password");
            String message = session.getParms().get("message");
            Log.d("SERVER","Numero: " + numero);
            Log.d("SERVER","Password: " + password);
            Log.d("SERVER","Message: " + message);

            if(password.equals(PASSWORD)) {

                //send SMS
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(numero, null, message, null, null);

                String msg = "<html><body><h1>SMSGateway</h1>\n";
                msg += "<p>Send SMS to " + numero + "</p>";
                return newFixedLengthResponse( msg + "</body></html>\n" );
            }else {
                String msg = "<html><body><h1>SMSGateway</h1>\n";
                msg += "<p>Bad password</p>";
                return newFixedLengthResponse( msg + "</body></html>\n" );
            }
        }
        catch (Exception e) {
            String msg = "<html><body><h1>SMSGateway</h1>\n";
            msg += "<p>Error: " + e.toString() + "</p>";
            return newFixedLengthResponse( msg + "</body></html>\n" );
        }
    }
}
