package com.example.sustaintrain;

        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.os.CountDownTimer;
        import android.os.Handler;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.content.DialogInterface;
        import android.hardware.Sensor;
        import android.hardware.SensorEvent;
        import android.hardware.SensorEventListener;
        import android.hardware.SensorManager;
        import android.media.MediaPlayer;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageButton;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;

public class Statistics extends AppCompatActivity implements SensorEventListener {

    ImageView compass_img;
    TextView txt_compass;
    int mAzimuth;
    private SensorManager mSensorManager;
    private Sensor mRotationV, mAccelerometer, mMagnetometer;
    boolean haveSensor = false, haveSensor2 = false;
    float[] rMat = new float[9];
    float[] orientation = new float[3];
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private ImageButton button;
    private ProgressBar progressBar;
    private Handler handler;
    private CountDownTimer countDownGarbage, countDownWalking, countDownTrophy;
    private int counter;
    private boolean timerStartedGarbage, timerStartedWalking, timerStartedTrophy;
    private int checkState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        compass_img = (ImageView) findViewById(R.id.pie_menu);
        //txt_compass = (TextView) findViewById(R.id.txt_azimuth);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setScaleY(2f);

        //COUNTER/PROGRESSBAR
        timerStartedGarbage = false;
        timerStartedWalking = false;
        timerStartedTrophy = false;


        countDownGarbage = new CountDownTimer(5000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(counter*100/(5000/1000));
                counter++;

            }

            @Override
            public void onFinish() {
                progressBar.setProgress(100);
                Intent intent = new Intent(Statistics.this, Garbage.class);
                int trash = getIntent().getExtras().getInt("trash");
                intent.putExtra("trash",trash);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //startActivityForResult(new Intent(Statistics.this, Garbage.class))
                //timerStartedGarbage = false;
                //Statistics.this.finish();
                counter= counter +2;
                onPause();
                cancel();
            }
        };

        countDownWalking = new CountDownTimer(5000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(counter*100/(5000/1000));
                counter++;

            }

            @Override
            public void onFinish() {
                progressBar.setProgress(100);
                Intent intent = new Intent(Statistics.this, Walking.class);
                int steps = getIntent().getExtras().getInt("steps");
                double distance = getIntent().getExtras().getDouble("distance");
                intent.putExtra("steps",steps);
                intent.putExtra("distance", distance);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //timerStartedWalking = false;
                //Statistics.this.finish();
                onPause();
                counter= counter +2;
                cancel();
            }
        };

        countDownTrophy = new CountDownTimer(5000,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(counter*100/(5000/1000));
                counter++;

            }

            @Override
            public void onFinish() {

                progressBar.setProgress(100);
                Intent intent = new Intent(Statistics.this, Trophy.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                //timerStartedTrophy = false;
                //Statistics.this.finish();
                onPause();
                counter= counter +2;
                cancel();
            }
        };

        start();

        button = (ImageButton) findViewById(R.id.button4);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHome();
            }
        });
    }

    public void openHome(){
        if(timerStartedTrophy){
            countDownTrophy.cancel();
        }
        if(timerStartedGarbage){
            countDownGarbage.cancel();
        }
        if(timerStartedWalking){
            countDownWalking.cancel();
        }
        Intent returnBtn = new Intent(this, MainActivity.class);
        startActivity(returnBtn);
    }




    @Override
    public void onSensorChanged(SensorEvent event) {
        if(counter < 6) {


            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                SensorManager.getRotationMatrixFromVector(rMat, event.values);
                mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
            }

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
                mLastAccelerometerSet = true;
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
                mLastMagnetometerSet = true;
            }
            if (mLastAccelerometerSet && mLastMagnetometerSet) {
                SensorManager.getRotationMatrix(rMat, null, mLastAccelerometer, mLastMagnetometer);
                SensorManager.getOrientation(rMat, orientation);
                mAzimuth = (int) (Math.toDegrees(SensorManager.getOrientation(rMat, orientation)[0]) + 360) % 360;
            }

            mAzimuth = Math.round(mAzimuth);
            compass_img.setRotation(-mAzimuth);


            String where = "Rotate to pick a setting";


            if (mAzimuth >= 1 && mAzimuth <= 120) {

                if (!(timerStartedGarbage)) {

                    if (timerStartedTrophy) {
                        countDownTrophy.cancel();
                        timerStartedTrophy = false;
                        counter = 0;
                    } else if (timerStartedWalking) {
                        countDownWalking.cancel();
                        timerStartedWalking = false;
                        counter = 0;
                    }

                    countDownGarbage.start();
                    timerStartedGarbage = true;

                }

                where = "Trash stats" + counter;
            }

            if (mAzimuth >= 121 && mAzimuth <= 240) {

                if (!(timerStartedTrophy)) {

                    if (timerStartedGarbage) {
                        countDownGarbage.cancel();
                        timerStartedGarbage = false;
                        counter = 0;
                    } else if (timerStartedWalking) {
                        countDownWalking.cancel();
                        timerStartedWalking = false;
                        counter = 0;
                    }

                    countDownTrophy.start();
                    timerStartedTrophy = true;

                }
                where = "High score";


            }

            if (mAzimuth >= 241 && mAzimuth <= 360) {

                if (!(timerStartedWalking)) {

                    if (timerStartedTrophy) {
                        countDownTrophy.cancel();
                        timerStartedWalking = false;
                        counter = 0;
                    } else if (timerStartedGarbage) {
                        countDownGarbage.cancel();
                        timerStartedGarbage = false;
                        counter = 0;
                    }

                    countDownWalking.start();
                    timerStartedWalking = true;

                }
                where = "Health stats";
            }


           //txt_compass.setText(where);

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void start(){
        if(mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)== null){
            if((mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) == null) || (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) == null)){
                noSensorAlert();
            }
            else {
                mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
                haveSensor = mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
                haveSensor2 = mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_UI);
            }
        }
        else{
            mRotationV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
            haveSensor = mSensorManager.registerListener(this, mRotationV, SensorManager.SENSOR_DELAY_UI);
        }
    }

    public void noSensorAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Your device doesn't support the Compass.")
                .setCancelable(false)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void stop(){
        if(haveSensor && haveSensor2){
            mSensorManager.unregisterListener(this, mAccelerometer);
            mSensorManager.unregisterListener(this, mMagnetometer);
        }
        else{
            if(haveSensor)
                mSensorManager.unregisterListener(this, mRotationV);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }


    @Override
    protected void onResume() {
        super.onResume();
        start();
    }
    @Override
    public void onBackPressed(){
        openHome();

    }


}