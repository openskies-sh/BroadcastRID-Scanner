package com.example.broadcastrid_wifiaware;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.aware.WifiAwareManager;
import android.provider.Settings;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Variables
    AttachCallbackExt mainAttachCallback = new AttachCallbackExt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // If location permission is not enabled location settings will open up
        // User then needs to enable location permissions for the app
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        // Check whether or not device supports WiFi Aware and display status as Toast message
        boolean hasWiFiAware = getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_AWARE);
        Toast hasAware = Toast.makeText(this, "WiFi Aware Supported", Toast.LENGTH_SHORT);
        Toast noAware = Toast.makeText(this, "WiFi Aware Unsupported. Please Exit the Program.", Toast.LENGTH_LONG);

        if (hasWiFiAware) {
            hasAware.show();
        } else {
            noAware.show();
        }
    }

    public void subscribe(View view) {

        // Create WiFiAwareManager object
        WifiAwareManager wifiAwareManager = (WifiAwareManager)getSystemService(Context.WIFI_AWARE_SERVICE);
        // Constant values of WifiAwareManager
        String state_change = WifiAwareManager.ACTION_WIFI_AWARE_STATE_CHANGED;
        int data_init = WifiAwareManager.WIFI_AWARE_DATA_PATH_ROLE_INITIATOR;
        int data_resp = WifiAwareManager.WIFI_AWARE_DATA_PATH_ROLE_RESPONDER;

        // Create TextView objects to display status
        TextView status1 = (TextView) findViewById(R.id.wifiAwareState);
        TextView status2 = (TextView) findViewById(R.id.wifiAwareReceivedMessage);




        // Check if WiFi Aware is available
        boolean awareAvailable = wifiAwareManager.isAvailable();
        if (awareAvailable) {
            status1.setText("Wifi Aware Available");
            status2.setText("Searching for Publishers");
        } else {
            status1.setText("Wifi Aware");
            status2.setText("Not Available, please Turn on WiFi");

        }

        // Start WiFi Aware
        AttachCallbackExt attachCallback = new AttachCallbackExt();
        wifiAwareManager.attach(attachCallback, null);
        mainAttachCallback = attachCallback;
    }

    public void showMessageReceived(View view) {

        // Create TextView objects to display status
        TextView status1 = (TextView) findViewById(R.id.wifiAwareState);
        TextView status2 = (TextView) findViewById(R.id.wifiAwareReceivedMessage);


        if (mainAttachCallback.getMessageReceived() == null) {
            //do nothing
            Log.d("lol", "Nothing happened");
        } else {
            Log.d("lol", new String(mainAttachCallback.getMessageReceived()));
            String messageReceived = new String(mainAttachCallback.getMessageReceived());
            status1.setText("Message Received");
            status2.setText(" ' " + messageReceived + " ' ");

        }
    }

    public void stopPublish(View view) {
        Toast endService = Toast.makeText(this, "Subscriber Service Ended", Toast.LENGTH_LONG);
        endService.show();
        finish();
        System.exit(1);
    }
}