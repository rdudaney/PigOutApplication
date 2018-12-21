package com.pigout.juronemo.pigout;

import android.util.Log;
import com.pigout.juronemo.pigout.Business;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.net.URLConnection;


public class GoogleMaps {

    private String API_Key;
    private double[] origin;

    public GoogleMaps(String startAPIkKey, double[] startOrigin){
        this.API_Key = startAPIkKey; this.origin = startOrigin;
    }

    public void getData(Business dest){
        JSONObject Response = new JSONObject();
        try {
            Response = HelperFunctions.urlGetRequest(setupURL(dest),this.API_Key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dest.setGoogleMaps(Response);

    }

    private String setupURL(Business dest){
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&departure_time=now" + "&key=" + this.API_Key +"&";
        url += "origins="+this.origin[0]+","+this.origin[1]+"&destinations=" + dest.getCoordinates()[0] + "%2C" + dest.getCoordinates()[1];

        return url;
    }

}
