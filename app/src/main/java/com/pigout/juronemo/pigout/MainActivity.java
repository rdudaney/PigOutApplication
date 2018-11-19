package com.pigout.juronemo.pigout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start_Click(View view){

        EditText LocationText = findViewById(R.id.location_edittext);
        EditText DistanceText = findViewById(R.id.distance_edittext);

        ToggleButton price1_Button = findViewById(R.id.toggleButton1);
        ToggleButton price2_Button = findViewById(R.id.toggleButton2);
        ToggleButton price3_Button = findViewById(R.id.toggleButton3);
        ToggleButton price4_Button = findViewById(R.id.toggleButton4);

        String locationVal = LocationText.getText().toString();
        String distanceVal = DistanceText.getText().toString();
        Boolean[] priceVal = {price1_Button.isChecked(),price2_Button.isChecked(),price3_Button.isChecked(),price4_Button.isChecked()};

        HashMap<String,String> URLParam = HelperFunctions.formatParams(locationVal,distanceVal,priceVal);


        Intent goSecondView = new Intent(this, SecondActivity.class);
        goSecondView.putExtra("URLParam",URLParam);




        startActivity(goSecondView);
    }
}
