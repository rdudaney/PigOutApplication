package com.pigout.juronemo.pigout;

import android.util.JsonReader;
import android.util.Log;
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
    private int total;

    public Yelp(String startApi_Key,HashMap<String, String> startURLParam) {

        this.API_Key = startApi_Key;
        setURLParam(startURLParam);
        total();

    }

    private void setURLParam(HashMap<String, String> startURLParam){

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

    private void total(){

        JSONObject call = new JSONObject();

        try {
            call = HelperFunctions.urlGetRequest(setupURL(1,0),this.API_Key);
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


        this.total = total_num;

    }
    public int getTotal(){return this.total;};

    // TODO: Change so instead of if the offset is close to the total, it reduces the size, change it so the offset accomoddates the maximum size
    public  Business[] get50(int offset){
        int size = MAX_LIMIT;

        if (offset + 1 + MAX_LIMIT > total){
            size = total - (offset+1) + 1;
        }

        Business[] business_list = new Business[size];
        JSONObject Response = new JSONObject();
        JSONArray bus_array = new JSONArray();


        try {
            Response = HelperFunctions.urlGetRequest(setupURL(size,offset),this.API_Key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            bus_array = Response.getJSONArray("businesses");
        }catch (JSONException e){
        }

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

    private String setupURL(int limit, int offset){
        String url = "https://api.yelp.com/v3/businesses/search?limit=" + limit + "&offset=" + offset + "&" + this.URLParam;

        return url;

    }

}
