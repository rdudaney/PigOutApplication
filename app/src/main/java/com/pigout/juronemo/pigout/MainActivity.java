package com.pigout.juronemo.pigout;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiConfiguration;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.*;
//import com.google.android.gms.location.LocationListener;

import java.util.HashMap;
import java.util.Objects;
// TODO: GooglePlace Details or YelpDetails (Google may return up to 10 photos)
// TODO: Save filter instance state
// TODO: Change distance from Yelp to Google
// TODO: fix locationRequest returning null when first initialized
// TODO: Add constraints (distance, minimum rating) and search Keyword
// TODO: Display Category, phone number, website
// TODO: Get Google Place rating
// TODO: Add additional information when business is selected
// TODO: Sort by rating?
// TODO: Add location from wifi, automatically see if location updated
// TODO: Make Address clickable to Google maps, and Yelp link to yelp
// TODO: Wrapper class that only contains Business, GoogleMaps, YelpPLace, and GooglePLace
// TODO: If next Bus is not null, do not use ASyncTask

public class MainActivity extends AppCompatActivity {

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private String locationVal = "";
    private String distanceVal = "";
    private boolean[] priceVal = {false,false,false,false};
    private static final int FILTER_INTEGER_VALUE = 593;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);



    }

    public void filter_Click(View view){
        Intent i = new Intent(this,FilterActivity.class);

        i.putExtra("Location",locationVal);
        i.putExtra("Distance",distanceVal);
        i.putExtra("Price",priceVal);
        startActivityForResult(i, FILTER_INTEGER_VALUE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (FILTER_INTEGER_VALUE) : {

                Log.d("STATE","MAIN onActivityResult ResultCode: " + resultCode);

                if(resultCode == RESULT_OK) {
                    locationVal = data.getStringExtra("Location");
                    distanceVal = data.getStringExtra("Distance");
                    priceVal = data.getBooleanArrayExtra("Price");

                    Log.d("STATE","MAIN onActivityResult LOCATION: " + locationVal);
                }

                break;
            }
        }
    }

    public void start_Click(View view){

        double[] origin = null;
        if (locationVal.equals("")){
            origin = getLoc();

            if (origin == null){
                return;
            }
        }

        Intent goSecondView = new Intent(this, SecondActivity.class);
        HashMap<String,String> URLParam = HelperFunctions.formatParams(locationVal,distanceVal,priceVal, origin);
        goSecondView.putExtra("URLParam",URLParam);


        goSecondView.putExtra("Origin",origin);

        startActivity(goSecondView);
    }

    private void requestGPS(){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    private double[] getLoc(){
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };



        if (locationVal.equals("") && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestGPS();
            return null;
        }

        if ( locationVal.equals("") && !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            displayLocationSettingsRequest(getApplicationContext());
            return null;
        }

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return null;
        }

        //locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if(lastKnownLocation == null){
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("No Location Found");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return null;
        }

//        while (lastKnownLocation == null){
//            lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        }

        double latitude = lastKnownLocation.getLatitude();
        double longitude = lastKnownLocation.getLongitude();

        double[] returnVal = {latitude,longitude};

        return returnVal;

    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }




    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);


        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        //Log.d("MAINVIEW", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        //Log.d("MAINVIEW", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            //Log.d("MAINVIEW", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        //Log.d("MAINVIEW", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


}
