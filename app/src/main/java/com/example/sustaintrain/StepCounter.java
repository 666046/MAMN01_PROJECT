package com.example.sustaintrain;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.widget.TextView;


public class StepCounter extends Activity implements SensorEventListener {
    private int step;
    private TextView  counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter);
        counter= (TextView) findViewById(R.id.stepCounterView);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;

        if (values.length > 0) {
            value = (int) values[0];
        }


        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            step++;
            counter.setText(step);
        }
        }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

