package com.pigout.juronemo.pigout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

public class FilterActivity extends AppCompatActivity {
    private EditText LocationText;
    private EditText DistanceText;
    private ToggleButton price1_Button;
    private ToggleButton price2_Button;
    private ToggleButton price3_Button;
    private ToggleButton price4_Button;

    private String locationVal;
    private String distanceVal;
    private boolean[] priceVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        LocationText = findViewById(R.id.location_edittext);
        DistanceText = findViewById(R.id.distance_edittext);

        price1_Button = findViewById(R.id.toggleButton1);
        price2_Button = findViewById(R.id.toggleButton2);
        price3_Button = findViewById(R.id.toggleButton3);
        price4_Button = findViewById(R.id.toggleButton4);




    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("STATE","FILTER ONRESUME CALLED");

        Intent intent = getIntent();
        locationVal = intent.getStringExtra("Location");
        distanceVal = intent.getStringExtra("Distance");
        priceVal = intent.getBooleanArrayExtra("Price");

        LocationText.setText(locationVal);
        DistanceText.setText(distanceVal);

        Log.d("STATE","FILTER ONRESUME LOCATION: " + locationVal);

        price1_Button.setChecked(priceVal[0]);
        price2_Button.setChecked(priceVal[1]);
        price3_Button.setChecked(priceVal[2]);
        price4_Button.setChecked(priceVal[3]);
    }


    public void reset_Click(View view){

        LocationText.setText("");
        DistanceText.setText("");

        price1_Button.setChecked(false);
        price2_Button.setChecked(false);
        price3_Button.setChecked(false);
        price4_Button.setChecked(false);
    }


    public void set_Click(View view){
        locationVal = LocationText.getText().toString();
        distanceVal = DistanceText.getText().toString();
        priceVal[0] = price1_Button.isChecked();
        priceVal[1] = price2_Button.isChecked();
        priceVal[2] = price3_Button.isChecked();
        priceVal[3] = price4_Button.isChecked();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("Location",locationVal);
        resultIntent.putExtra("Distance",distanceVal);
        resultIntent.putExtra("Price",priceVal);



        if( !distanceVal.equals("") && Integer.parseInt(distanceVal) > 24){
            AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please input distance 24 miles or less");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }else if( !distanceVal.equals("") && Integer.parseInt(distanceVal) < 1){
            AlertDialog alertDialog = new AlertDialog.Builder(FilterActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Please input distance greater than or equal to 1 mile");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;
        }


        Log.d("STATE","FILTER SETCLICK LOCATION: " + locationVal);

        setResult(RESULT_OK, resultIntent);
        finish();




    }
}
