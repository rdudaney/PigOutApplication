package com.pigout.juronemo.pigout;

import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GooglePlace {
    private String API_Key;

    public GooglePlace(String startAPI){
        this.API_Key = startAPI;
    }


    public void searchPlace(Business dest){
        JSONObject Response = new JSONObject();
        try {
            Response = call_me(setup(dest));
        } catch (Exception e) {
            e.printStackTrace();
        }


        double rating = -1;
        String status = "";

        try {
            rating = Response.getJSONArray("candidates").getJSONObject(0).getDouble("rating");
            status = Response.getString("status");
            Log.d("STATE", "Rating: " + rating);
            Log.d("STATE", "Status: " + status);
        }catch (Exception e){
            Log.d("STATE","Didn't Work");
        }

        if(!status.equals("OK")){
            rating = -1;
        }

        dest.setGoogleRating(rating);




    }

    private String setup(Business dest){
        String loc = "point:" + dest.getLatitude() + "," + dest.getLongitude();
        String fields = "name,rating,formatted_address,geometry";
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

        Log.d("STATE","GooglePlace URL: " + url);
        Log.d("STATE","GooglePlace Response Code: " + responseCode);
        Log.d("STATE","GooglePlace Response: " + response);

        return myResponse;

    }

}
