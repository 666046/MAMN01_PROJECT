package com.example.sustaintrain;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class StepCounter extends AppCompatActivity implements SensorEventListener,StepListener {
    private int steps;
    private int pickedUp;
    private TextView stepcounter;
    private TextView pickedUpTrash;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Steps: ";
    private static final String TEXT_PICKED_TRASH ="Picked up: ";
    private Button finishRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);
        finishRoute = (Button) findViewById(R.id.finishroute);
        stepcounter = (TextView) findViewById(R.id.stepCounterView);
        pickedUpTrash= (TextView) findViewById(R.id.pickedUpTrahsView);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        sensorManager.registerListener(this, accel, SensorManager.SENSOR_DELAY_FASTEST);
        stepcounter.setText(TEXT_NUM_STEPS + steps);
        pickedUpTrash.setText(TEXT_PICKED_TRASH + pickedUp);

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
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

    }

}

