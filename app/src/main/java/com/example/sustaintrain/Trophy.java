package com.example.sustaintrain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import static com.example.sustaintrain.StepCounter.HIGH_SCORE;
import static com.example.sustaintrain.StepCounter.PREFS_NAME;

public class Trophy extends AppCompatActivity {

    private ImageButton button;
    private TextView score;
    private TextView steps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trophy);
        button = (ImageButton) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHome();
            }
        });

        score = (TextView) findViewById(R.id.HighScore);
        steps = (TextView) findViewById(R.id.highscoresteps);

        score.setText("Picked up: " + StepCounter.pickedUp);
        steps.setText("Steps: " + StepCounter.steps);


    }
    public void openHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed(){
        openHome();
    }
}