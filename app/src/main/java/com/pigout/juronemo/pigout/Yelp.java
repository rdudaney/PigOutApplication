package com.pigout.juronemo.pigout;

import android.util.JsonReader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class Yelp {

    private static final int MAX_LIMIT= 50;
    private static final int MAX_BUSINESS= 1000;
    private String API_Key;
    private String URLParam;

    public Yelp(String startApi_Key,HashMap<String, String> startURLParam){

        this.API_Key = startApi_Key;
        setURLParam(startURLParam);

    }

    public void setURLParam(HashMap<String, String> startURLParam){

        String URLParam_string = "";
        String key;
        String value;

        if ((startURLParam.get("location").length() == 0) && (startURLParam.get("latitude").length() == 0 || startURLParam.get("longitude").length() == 0)){
            throw new IllegalArgumentException("missing location information");
        }

        Iterator it = startURLParam.entrySet().iterator();
        while (it.hasNext()){
            HashMap.Entry pair = (HashMap.Entry)it.next();
            key = (String)pair.getKey();
            value =(String)pair.getValue();
            if (value.length() != 0)
                URLParam_string += key + "=" + value +"&";
        }

        URLParam_string = URLParam_string.substring(0,URLParam_string.length()-1);
        System.out.println(URLParam_string);

        this.URLParam = URLParam_string;

    }

    public int total(){
        JSONObject call = new JSONObject();

        try {
            call = call_me(1,0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int total_num = 0;
        try {
            total_num = call.getInt("total");
        }catch (JSONException e){
        }
        if (total_num > MAX_BUSINESS){
            total_num = MAX_BUSINESS;
        }


        return total_num;

    }

    public Business[] all(){
        int total = total();
        if (total > MAX_BUSINESS){
            total = MAX_BUSINESS;
        }

        Business[] business_list = new Business[total];
        JSONObject Response = new JSONObject();
        JSONArray bus_array = new JSONArray();



        for(int i = 0; i < total; i+=50){
            try {
                Response = call_me(MAX_LIMIT,i);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                bus_array = Response.getJSONArray("businesses");
            }catch (JSONException e){
            }

            System.out.println("\nYELP Api Run: " + (i/MAX_LIMIT+1) + " Out of: " + (total/MAX_LIMIT));


            for (int j = 0; j < bus_array.length(); j++) {
                JSONObject singleBus = null;
                try {
                    singleBus = bus_array.getJSONObject(j);
                }catch (JSONException e){
                }
                if ((i+j) < total){
                    business_list[i+j] = new Business(singleBus);
                }
            }

        }

        return business_list;
    }
    // Change so instead of if the offset is close to the total, it reduces the size, change it so the offset accomoddates the maximum size
    public  Business[] get50(int offset){
        int total = total();
        if (total > 1000){
            total = 1000;
        }

        int size = MAX_LIMIT;
        if (offset + 1 + MAX_LIMIT > total){
            size = total - (offset+1) + 1;
        }

        Business[] business_list = new Business[size];
        JSONObject Response = new JSONObject();
        JSONArray bus_array = new JSONArray();



        try {
            Response = call_me(size,offset);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            bus_array = Response.getJSONArray("businesses");
        }catch (JSONException e){
        }
        System.out.println("\nYELP Api 50 Run: Offset: " + offset);


        for (int j = 0; j < bus_array.length(); j++) {
            JSONObject singleBus = null;
            try {
                singleBus = bus_array.getJSONObject(j);
            }catch (JSONException e){
            }

            business_list[j] = new Business(singleBus);
        }


        return business_list;
    }

    private JSONObject call_me(int limit, int offset) throws Exception {

        String url = "https://api.yelp.com/v3/businesses/search?limit=" + limit + "&offset=" + offset + "&" + this.URLParam;
        System.out.println(url);
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

        return myResponse;

    }
}
