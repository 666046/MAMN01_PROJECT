package com.example.sustaintrain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Button button2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openSaveTheWorld();
            }
        });
        button2 = (Button) findViewById(R.id.button4);
        button2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openStatistics();
            }
        });

    }

    public void openSaveTheWorld() {
        Intent intent = new Intent(this, SaveTheWorld.class);
        startActivity(intent);
    }

    public void openStatistics(){
        Intent intent = new Intent(this, Statistics.class);
        startActivity(intent);
    }

}


