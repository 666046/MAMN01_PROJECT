package com.example.sustaintrain;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


public class StepCounter extends AppCompatActivity implements SensorEventListener,StepListener {
    private int steps;
    private int valueOf;
    private int pickedUp;
    private TextView stepcounter;
    private TextView pickedUpTrash;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Steps: ";
    private static final String TEXT_PICKED_TRASH ="Picked up: ";
    private Button finishRoute;
    private ImageButton help;
    private PowerManager pm;
    private MediaPlayer mp;
    private boolean isScreenOn;
    private Vibrator vib;
    public static final String PREFS_NAME = "PickedUp";
    public static final String HIGH_SCORE = "HighScore";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);
        finishRoute = (Button) findViewById(R.id.finishroute);
        help = (ImageButton) findViewById(R.id.helpImage);
        stepcounter = (TextView) findViewById(R.id.stepCounterView);
        pickedUpTrash= (TextView) findViewById(R.id.pickedUpTrahsView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mp = MediaPlayer.create(this, R.raw.tada);
        vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        stepcounter.setText(TEXT_NUM_STEPS + steps);
        pickedUpTrash.setText(TEXT_PICKED_TRASH + pickedUp);
        pickedUp = 0;
        valueOf = 0;
        steps = 0;


        

        finishRoute.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(StepCounter.this);

                // Set a title for alert dialog
                builder.setTitle("Select your answer.");

                // Ask the final question
                builder.setMessage("Are you sure you want to finish your route?");

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when user clicked the Yes button
                        // Set the TextView visibility GONE
                        openPickRoute();
                    }
                });

                // Set the alert dialog no button click listener
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something when No button clicked
                        Toast.makeText(getApplicationContext(),
                                "You've selected No",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(StepCounter.this);
                dialog.setPositiveButton("OK, got it!", null);
                LayoutInflater factory = LayoutInflater.from(StepCounter.this);
                final View view = factory.inflate(R.layout.alert_help, null);
                dialog.setView(view);
                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });




    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);


        isScreenOn = pm.isScreenOn();
        if(isScreenOn) {

            float yVal = event.values[1];

            if(yVal < -5){
                valueOf++;
            }

            if (yVal > 5 && valueOf > 0){

                pickedUp++;
                pickedUpTrash.setText(TEXT_PICKED_TRASH + pickedUp);
                valueOf = 0;
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                LayoutInflater factory = LayoutInflater.from(this);
                final View view = factory.inflate(R.layout.alert_image, null);
                dialog.setView(view);
                final AlertDialog alert = dialog.create();
                alert.show();
                mp.setLooping(false);
                mp.start();
                vib.vibrate(1000);

                final Timer timer = new Timer();
                timer.schedule(new TimerTask(){
                    public void run() {
                        alert.dismiss();
                        timer.cancel();

                    }
                }, 2000);
            }


        }

        }


    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }


    @Override
    public void step(long timeNs) {
        steps++;
        stepcounter.setText(TEXT_NUM_STEPS + steps);
    }

    public void openPickRoute(){
            double distance = Math.round(steps*0.76);
            Intent intent = new Intent();
            //Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("steps", steps);
            intent.putExtra("trash", pickedUp);
            intent.putExtra("distance", distance);
            setResult(RESULT_OK, intent);
            finish();

    }


    }



