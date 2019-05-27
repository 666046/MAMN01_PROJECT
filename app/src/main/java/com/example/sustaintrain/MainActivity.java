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

    }

    public void openSaveTheWorld() {
        Intent intent = new Intent(this, SaveTheWorld.class);
        startActivity(intent);
    }

    public void openStatistics(){
        Intent intent = new Intent(this, Statistics.class);
        intent.putExtra("trash", 1);
        intent.putExtra("steps", 2);
        intent.putExtra("distance", 3);
        startActivity(intent);
    }

    public void openMap(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }




}


