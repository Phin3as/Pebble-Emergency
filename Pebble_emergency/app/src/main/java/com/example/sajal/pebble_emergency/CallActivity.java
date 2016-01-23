package com.example.sajal.pebble_emergency;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Sajal on 1/22/2016.
 */
public class CallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("SAJAL");
        call();
    }

    public void call() throws SecurityException{
        Intent in = new Intent(Intent.ACTION_DIAL, Uri.parse("2676326112"));
        startActivity(in);
    }
}
