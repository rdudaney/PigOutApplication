package com.pigout.juronemo.pigout;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

public class FilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
    }

    public void set_Click(View view){
        EditText LocationText = findViewById(R.id.location_edittext);
        EditText DistanceText = findViewById(R.id.distance_edittext);

        ToggleButton price1_Button = findViewById(R.id.toggleButton1);
        ToggleButton price2_Button = findViewById(R.id.toggleButton2);
        ToggleButton price3_Button = findViewById(R.id.toggleButton3);
        ToggleButton price4_Button = findViewById(R.id.toggleButton4);

        String locationVal = LocationText.getText().toString();
        String distanceVal = DistanceText.getText().toString();
        boolean[] priceVal = {price1_Button.isChecked(),price2_Button.isChecked(),price3_Button.isChecked(),price4_Button.isChecked()};

        Intent resultIntent = new Intent();
        resultIntent.putExtra("Location",locationVal);
        resultIntent.putExtra("Distance",distanceVal);
        resultIntent.putExtra("Price",priceVal);



        if( Integer.parseInt(distanceVal) > 24){
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
        }else if( Integer.parseInt(distanceVal) < 1){
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



        setResult(RESULT_OK, resultIntent);
        finish();




    }
}
