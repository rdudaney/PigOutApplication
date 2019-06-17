package com.pigout.juronemo.pigout;

import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class YelpDetails implements Serializable {
    private String API_Key;

    public YelpDetails(String startAPI_Key){

        this.API_Key = startAPI_Key;
    }


    public void addInfo(Business dest){
        JSONObject Response = new JSONObject();
        try {
            Response = call_me(setup(dest));
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private String setup(Business dest){
        String url = "https://api.yelp.com/v3/businesses/" + dest.getId();

        return url;
    }


    private JSONObject call_me(String url) throws Exception {

//        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&departure_time=now" + "&key=" + this.API_Key +"&" + URL_attach ;
        URL obj = new URL(url);

        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Authorization", "Bearer " + this.API_Key);
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "text/plain");
        con.setRequestProperty("charset", "UTF-8");

        int responseCode = con.getResponseCode();

//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print in String
//        System.out.println(response.toString());

        //Read JSON response and print
        JSONObject myResponse = new JSONObject(response.toString());
//        System.out.println("\nresult after Reading JSON Response");
//        System.out.println("total- " + myResponse.getNumber("total"));

        Log.d("STATE","YelpDetails URL: " + url);
        Log.d("STATE","YelpDetails Response Code: " + responseCode);
        Log.d("STATE","YelpDetails Response: " + response);

        return myResponse;

    }

}
