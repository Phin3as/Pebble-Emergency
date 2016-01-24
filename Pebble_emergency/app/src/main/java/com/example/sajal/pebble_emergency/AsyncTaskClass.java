package com.example.sajal.pebble_emergency;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Sajal on 1/23/2016.
 */
public class AsyncTaskClass extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] params){

        ArrayList array = (ArrayList)params[0];
        Double latitude = (Double) array.get(0);
        Double longitude = (Double) array.get(1);
        String rawData = "{\"latitude\":\"" + latitude.toString() + "\",\"longitude\":\"" + longitude.toString()+"\"}";
        System.out.println(rawData);
        String type = "application/json";
        String encodedData = rawData;
        try {
//            System.out.println("MID");
            URL u = new URL("http://52.10.202.217:5000/data ");
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type", type);
            conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
            OutputStream os = conn.getOutputStream();
            os.write(encodedData.getBytes());
            os.flush();
            os.close();
            conn.connect();
        }catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("END");
        return null;
    }
}
