package com.example.sajal.pebble_emergency;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import com.twilio.client.Connection;
import com.twilio.client.Device;
import com.twilio.client.Twilio;

public class MainActivity extends AppCompatActivity implements Twilio.InitListener{

    Button b1;
    Button b2;
    Button b3;
    LinearLayout layout;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        layout = (LinearLayout) findViewById(R.id.sajal);


        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("2676326112", null, "YOU SUCK!", null, null);
                    layout.setBackgroundColor(Color.GREEN);
                }
                catch (Exception e) {
                    layout.setBackgroundColor(Color.RED);
                }

            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    callTwilio();
                    Intent in = new Intent(Intent.ACTION_CALL);
                    in.setData(Uri.parse("tel:2676326112"));
                    startActivity(in);

//                    layout.setBackgroundColor(Color.GREEN);
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                    layout.setBackgroundColor(Color.RED);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    layout.setBackgroundColor(Color.BLUE);
                }
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setBackgroundColor(Color.BLACK);
            }
        });
    }

    public void callTwilio() {
        Device device;
        Connection connection;

        Context context = getApplicationContext();

        Twilio.initialize(context,this);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onInitialized() {
    }
}
