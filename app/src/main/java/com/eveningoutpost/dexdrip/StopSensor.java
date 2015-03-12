package com.eveningoutpost.dexdrip;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.activeandroid.ActiveAndroid.beginTransaction;
import static com.activeandroid.ActiveAndroid.endTransaction;
import static com.activeandroid.ActiveAndroid.setTransactionSuccessful;


public class StopSensor extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public  Button                   button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Sensor.isActive()) {
            Intent intent = new Intent(this, StartNewSensor.class);
            startActivity(intent);
            finish();
        } else {
            setContentView(R.layout.activity_stop_sensor);
            button = (Button) findViewById(R.id.stop_sensor);
            addListenerOnButton();

            beginTransaction();
            try {
                Sensor sensor = Sensor.currentSensor();
                final long startedAt = (long) sensor.started_at;
                ((TextView) findViewById(R.id.sensor_age)).setText("Started at: " + new Date(startedAt).toString());
                ((TextView) findViewById(R.id.sensor_time_active)).setText("   " + DateUtils.getRelativeTimeSpanString(startedAt, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS, 0));

                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), "Stop Sensor", this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        mNavigationDrawerFragment.swapContext(position);
    }

    public void addListenerOnButton() {
        button = (Button) findViewById(R.id.stop_sensor);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                beginTransaction();
                try {
                    Sensor sensor = Sensor.currentSensor();
                    sensor.stopped_at = new Date().getTime();
                    Log.w("NEW SENSOR", "Sensor stopped at " + sensor.stopped_at);
                    sensor.save();

                    setTransactionSuccessful();
                } finally {
                    endTransaction();
                }

                Toast.makeText(getApplicationContext(), "Sensor stopped", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Home.class);
                startActivity(intent);
                finish();
            }

        });
    }
}
