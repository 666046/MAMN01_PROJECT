package com.example.sustaintrain;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class Garbage extends AppCompatActivity {

    private ImageButton button;
    private int trash;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage);

        String garbageText = "Garbage collected: ";
        String garbageEndText =" units";

        trash = getIntent().getExtras().getInt("trash");
        TextView textView2 = (TextView) findViewById(R.id.textView3);
        textView2.setText(garbageText + String.valueOf(trash) + garbageEndText);

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
}
