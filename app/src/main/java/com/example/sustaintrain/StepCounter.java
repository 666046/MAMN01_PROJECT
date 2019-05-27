package com.example.sustaintrain;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

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
    private PowerManager pm;
    private MediaPlayer mp;
    private boolean isScreenOn;
    private Vibrator vib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);
        finishRoute = (Button) findViewById(R.id.finishroute);
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
                openPickRoute();
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

