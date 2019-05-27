package com.example.sustaintrain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Walking extends AppCompatActivity {

    private ImageButton button;
    private int steps;
    private double distance;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walking);

        String stepText = "Step counter: ";
        String distanceText = "Distance walked: ";
        String distanceEndText =" meter";

        steps = getIntent().getExtras().getInt("steps");
        TextView textView1 = (TextView) findViewById(R.id.textView3);
        textView1.setText(stepText + String.valueOf(steps));

        distance = getIntent().getExtras().getDouble("distance");
        TextView textView2 = (TextView) findViewById(R.id.textView4);
        textView2.setText(distanceText + String.valueOf(distance) + distanceEndText);



        button = (ImageButton) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openHome();
            }
        });
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