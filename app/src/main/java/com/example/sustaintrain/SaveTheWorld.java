package com.example.sustaintrain;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SaveTheWorld  extends AppCompatActivity {

    private Button button;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_the_world);
        button = (Button) findViewById(R.id.stepCounter);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openStepCounter();
            }
        });
    }
    public void openStepCounter(){
        Intent intent = new Intent(this, StepCounter.class);
        startActivity(intent);
    }

}
