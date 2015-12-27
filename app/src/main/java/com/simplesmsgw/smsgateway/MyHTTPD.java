package com.simplesmsgw.smsgateway;

import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;

import java.io.IOException;

public class MyHTTPD extends NanoHTTPD {
    private Handler handler;

    public static int PORT;
    public static String PASSWORD;
    public static int LIMIT;
    private int number = 1;

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

                if(number <= LIMIT) {
                    //send SMS
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(numero, null, message, null, null);

                    String msg = "Send OK : " + number + "/" + LIMIT;
                    number++;

                    return newFixedLengthResponse(msg);
                }
                else {
                    return newFixedLengthResponse("Bloqued by limit " + LIMIT);
                }
            }else {
                return newFixedLengthResponse("Bad password");
            }
        }
        catch (Exception e) {
            return newFixedLengthResponse(e.toString());
        }
    }
}
