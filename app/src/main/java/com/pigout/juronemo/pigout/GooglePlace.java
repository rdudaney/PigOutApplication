package com.pigout.juronemo.pigout;

import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class GooglePlace implements Serializable {
    private String API_Key;

    public GooglePlace(String startAPI){
        this.API_Key = startAPI;
    }

    public void getData(Business dest){

        JSONObject Response = new JSONObject();
        try {
            Response = HelperFunctions.urlGetRequest(setupURL(dest),this.API_Key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dest.setGooglePlace(Response);




    }

    private String setupURL(Business dest){
        String loc = "point:" + dest.getLatitude() + "," + dest.getLongitude();
        String fields = "name,rating,formatted_address,geometry,photos";
        String input = dest.getName().toLowerCase().replaceAll("[^A-Za-z0-9 ]","");
        input = input.replaceAll(" ","%20");


        String url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?";
        url += "input=" + input + "&";
        url += "key=" + API_Key + "&";
        url += "fields=" + fields + "&";
        url += "inputtype=textquery&";
        url += "locationbias=" + loc;

        //url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=mongolian%20grill&inputtype=textquery&fields=photos,formatted_address,name,opening_hours,rating&locationbias=circle:2000@47.6918452,-122.2226413&key=" + this.API_Key;

        return url;
    }

}
