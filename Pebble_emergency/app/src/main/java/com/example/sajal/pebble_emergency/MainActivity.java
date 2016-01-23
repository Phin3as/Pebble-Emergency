package com.example.sajal.pebble_emergency;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

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


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String msg = getLocation();

                if (msg.length()==0) {
                    msg = "YOU SUCK!";
                }

//                System.out.println(msg);

                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage("2676326112", null, msg, null, null);
                    layout.setBackgroundColor(Color.GREEN);
                } catch (Exception e) {
                    layout.setBackgroundColor(Color.RED);
                }

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = getLocation();

                if (msg.length()==0) {
                    msg = "YOU SUCK!";
                }

//                System.out.println(msg);
                try {
                    Intent in = new Intent(Intent.ACTION_DIAL);
                    in.setData(Uri.parse("tel:2676326112"));
                    startActivity(in);

                    layout.setBackgroundColor(Color.GREEN);
                } catch (Exception e) {
                    e.printStackTrace();
                    layout.setBackgroundColor(Color.RED);
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

    public String getLocation() {

        String msg=null;

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location l = null;

        Double latitude=0.0,longitude=0.0;
        String strAdd="";

        for (int i = 0; i < providers.size(); i++) {
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) {
                latitude = l.getLatitude();
                longitude = l.getLongitude();
                strAdd = getCompleteAddressString(latitude, longitude);
                break;
            }
        }
        if (strAdd.length()!=0) {
            msg = "Latitude:"+latitude+"\nLongitude:"+longitude+"\nAddress:"+strAdd;
        }
        return msg;
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

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder
                    .getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                android.location.Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                strAdd = strReturnedAddress.toString();
//                Log.v("My Current loction address","" + strReturnedAddress.toString());
            } else {
//                Log.v("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
//            Log.v("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }


}
