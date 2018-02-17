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
    public static String SERVER;
    private int number = 0;

    public MyHTTPD() throws IOException {
        super(PORT);
        start();
        Log.d("SERVER", "Start on port" + PORT);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Log.d("SERVER","Get a request: " + session.toString());

        try {

            if(session.getUri().compareTo("/send") == 0) {

                String toNum = session.getParms().get("toNum");
                String password = session.getParms().get("password");
                String message = session.getParms().get("message");
                Log.d("SERVER","toNum: " + toNum);
                Log.d("SERVER","password: " + password);
                Log.d("SERVER","message: " + message);

                if(password.equals(PASSWORD)) {

                    if(number <= LIMIT) {
                        //send SMS
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(toNum, null, message, null, null);

                        String msg = "OK : " + number + "/" + LIMIT;
                        number++;

                        return newFixedLengthResponse(msg);
                    }
                    else {
                        return newFixedLengthResponse("ERROR: limit reached " + LIMIT);
                    }
                }else {
                    return newFixedLengthResponse("ERROR: Bad password");
                }
            }
            else if (session.getUri().compareTo("/info") == 0) {
                return newFixedLengthResponse("SMS sent: " + number + "/" + LIMIT);
            }

            else {
                return newFixedLengthResponse("ERROR: Bad request");
            }
        }
        catch (Exception e) {
            return newFixedLengthResponse(e.toString());
        }
    }
}
