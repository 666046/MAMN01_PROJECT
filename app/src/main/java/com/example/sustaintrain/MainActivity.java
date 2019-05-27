package com.example.sustaintrain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private ImageButton button2;
    private Button mapButton;
    private int steps, trash;
    private double distance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*button= (Button) findViewById(R.id.button3);
     button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openSaveTheWorld();
            }
        });*/
        button2 = (ImageButton) findViewById(R.id.button4);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openStatistics();
            }
        });
        mapButton = (Button) findViewById(R.id.button);
        mapButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openMap();
            }
        });
        //steps = 0;
        //trash = 0;
        //distance = 0.0;
       //steps = getIntent().getExtras().getInt("steps");
       //double distance = getIntent().getExtras().getDouble("distance");
       // int trash = getIntent().getExtras().getInt("trash");

    }

    public void openSaveTheWorld() {
        Intent intent = new Intent(this, SaveTheWorld.class);
        startActivity(intent);
    }

    public void openStatistics(){
        Intent intent = new Intent(this, Statistics.class);
        intent.putExtra("trash", trash);
        intent.putExtra("steps", steps);
        intent.putExtra("distance", distance);
        startActivity(intent);
    }

    public void openMap(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                steps = data.getIntExtra("steps", 0);
                trash = data.getIntExtra("trash", 0);
                distance = data.getDoubleExtra("distance", 0.0 );
            }
            if (resultCode == RESULT_CANCELED) {
                steps = 0;
                trash = 0;
                distance = 0.0;
            }
        }
    }

}


