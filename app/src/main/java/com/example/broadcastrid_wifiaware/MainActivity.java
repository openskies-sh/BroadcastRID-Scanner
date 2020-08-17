package com.example.broadcastrid_wifiaware;

import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.aware.WifiAwareManager;
import android.provider.Settings;
import androidx.core.content.ContextCompat;
import android.content.BroadcastReceiver;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Variables
    AttachCallbackExt mainAttachCallback = new AttachCallbackExt(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button start_logging_button = (Button) findViewById(R.id.start_logging_button);
        final Button stop_logging_button  = (Button) findViewById(R.id.stop_logging_button);


        IntentFilter filter =  new IntentFilter(WifiAwareManager.ACTION_WIFI_AWARE_STATE_CHANGED);

        // Check whether or not device supports WiFi Aware and display status as Toast message
        boolean hasWiFiAware = getPackageManager().hasSystemFeature(PackageManager.FEATURE_WIFI_AWARE);
        Toast hasAware = Toast.makeText(this, "WiFi Aware Supported", Toast.LENGTH_SHORT);
        Toast noAware = Toast.makeText(this, "WiFi Aware Unsupported. Please Exit the Program.", Toast.LENGTH_LONG);

        if (hasWiFiAware) {
            hasAware.show();
            registerReceiver(myReceiver, filter);

        } else {
        noAware.show();
        start_logging_button.setVisibility(View.GONE);
        stop_logging_button.setVisibility(View.GONE);
        }
    }


    BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }

    };
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
        AttachCallbackExt attachCallback = new AttachCallbackExt(this);
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