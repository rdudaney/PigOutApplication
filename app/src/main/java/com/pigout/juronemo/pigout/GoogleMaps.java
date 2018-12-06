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

    private int INCREMENT =  100;
    private String API_Key;
    private double[] origin;

    public GoogleMaps(String startAPIkKey, double[] startOrigin){
        this.API_Key = startAPIkKey; this.origin = startOrigin;
    }

    public void setOrigin(double[] startOrigin){
        this.origin = startOrigin;
    }

    public void singleTime(Business dest){
        JSONObject Response = new JSONObject();
        try {
            Response = call_me(setup_single(dest));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject rows = null;
        try {
            rows = Response.getJSONArray("rows").getJSONObject(0);
            JSONObject element = rows.getJSONArray("elements").getJSONObject(0);
            //                System.out.println(element);
            JSONObject duration = element.getJSONObject("duration_in_traffic");
            //                System.out.println(duration);
            dest.setDuration(duration.getInt("value")/60.0);
        } catch (Exception e) {
            dest.setDuration(-1);

        }


    }

    public void time_all(Business[] dest){
        double[] dur = new double[dest.length];
        JSONObject Response = new JSONObject();
        int[] incr = new int[dest.length/INCREMENT+2];


        for (int i = 1; i < incr.length-1;i++){
            incr[i] = i*INCREMENT;
        }
        incr[incr.length-1] = dest.length;

        System.out.println("Increment Array");
        for(int i = 0; i < incr.length; i++){
            System.out.println(incr[i]);
        }

        for (int j = 1; j < incr.length;j++) {
            if (incr[j] != incr[j-1]) {
                System.out.println("j: " + j + " incr[j-1]: " + incr[j - 1] + " incr[j]-1: " + (incr[j] - 1));
                System.out.println("Subarray length: " + Arrays.copyOfRange(dest, incr[j - 1], incr[j] ).length);
                try {
                    Response = call_me(setup(Arrays.copyOfRange(dest, incr[j - 1], incr[j])));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                System.out.println(Response.toString());
                JSONObject rows = null;
                try {
                    rows = Response.getJSONArray("rows").getJSONObject(0);
                } catch (JSONException e){
                }
//                System.out.println(rows);
                //System.out.println("Element Length: " + rows.getJSONArray("elements").length());

                for (int i = 0; i < (incr[j] - incr[j - 1]); i++) {
                    //            System.out.println("i: " + i + " Address:" + Response.getJSONArray("destination_addresses").getString(i));
                    System.out.println(incr[j - 1] + i);
                    try {
                        JSONObject element = rows.getJSONArray("elements").getJSONObject(i);
                        //                System.out.println(element);
                        JSONObject duration = element.getJSONObject("duration_in_traffic");
                        //                System.out.println(duration);
                        dest[incr[j - 1] + i].setDuration(duration.getInt("value")/60.0);
                    } catch (Exception e) {
                        dest[incr[j - 1] + i].setDuration(-1);

                    }
                }
            }
        }

    }

    private String setup(Business[] dest){
//        String URL_attach = "origins="+origin[0]+","+origin[1]+"&destinations="+dest[0]+"%2C"+dest[1];
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&departure_time=now" + "&key=" + this.API_Key +"&";
        url += "origins="+this.origin[0]+","+this.origin[1]+"&destinations=";

        for(int i = 0;i < dest.length;i++){
            String addon = dest[i].getCoordinates()[0] + "%2C" + dest[i].getCoordinates()[1] + "%7C";
            url += addon;

        }
        url = url.substring(0,url.length()-3);
        System.out.println(url);
        return url;
    }

    private String setup_single(Business dest){
//        String URL_attach = "origins="+origin[0]+","+origin[1]+"&destinations="+dest[0]+"%2C"+dest[1];
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&departure_time=now" + "&key=" + this.API_Key +"&";
        url += "origins="+this.origin[0]+","+this.origin[1]+"&destinations=" + dest.getCoordinates()[0] + "%2C" + dest.getCoordinates()[1];

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

        Log.d("STATE","Google URL: " + url);
        Log.d("STATE","Google Response Code: " + responseCode);
        Log.d("STATE","Google Response: " + response);

        return myResponse;

    }

}
