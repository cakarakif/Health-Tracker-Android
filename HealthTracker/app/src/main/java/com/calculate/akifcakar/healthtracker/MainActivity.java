package com.calculate.akifcakar.healthtracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.calculate.akifcakar.healthtracker.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button bmiButton;
    Button eerButton;
    Button phtButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bmiButton=(Button)findViewById(R.id.bmi);
        eerButton=(Button)findViewById(R.id.eer);
        phtButton=(Button)findViewById(R.id.pht);

        switchBMI();
        switchEER();
        switchPHT();
    }

    private void switchBMI(){
        bmiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),BMI.class);
                startActivity(intent);
            }
        });
    }

    private void switchEER(){
        eerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EER.class);
                startActivity(intent);
            }
        });
    }

    private void switchPHT(){
        phtButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PHT.class);
                startActivity(intent);
            }
        });
    }
}
