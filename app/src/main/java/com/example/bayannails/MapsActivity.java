package com.example.bayannails;


import android.os.Bundle;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.BatteryManager;


public class MapsActivity extends AppCompatActivity {

    private static final double DESTINATION_LATITUDE = 32.809946;
    private static final double DESTINATION_LONGITUDE = 34.984639;

    private BroadcastReceiver batteryLowReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        batteryLowReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int batteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                float batteryPercentage = batteryLevel * 100 / (float) batteryScale;

                if (batteryPercentage < 15) {
                    Toast.makeText(context, "Low battery, please charge!", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity
                } else {
                    startNavigation();
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLowReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryLowReceiver);
    }

    private void startNavigation() {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + DESTINATION_LATITUDE + "," + DESTINATION_LONGITUDE);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}







