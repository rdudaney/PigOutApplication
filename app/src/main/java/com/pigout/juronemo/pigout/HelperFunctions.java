package com.pigout.juronemo.pigout;

import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;

public class HelperFunctions {


    public static HashMap<String, String> formatParams(String loc, String dist, Boolean[] price){
        HashMap<String, String> URLParam = new HashMap<>();

        URLParam.put("term", "Food");
        URLParam.put("location", formatLoc(loc)); //"Woodland-Hills-CA"
        URLParam.put("latitude", "");
        URLParam.put("longitude", "");
        URLParam.put("radius", formatDist(dist));
        URLParam.put("categories", "");
        URLParam.put("locale", "");
//        URLParam.put("limit", "50");
//        URLParam.put("offset", "50");
        URLParam.put("sort_by", "");
        URLParam.put("price", formatPrices(price));
        URLParam.put("open_now", "");
        URLParam.put("open_at", "");
        URLParam.put("attributes", "");


        return URLParam;

    }

    private static String formatLoc(String loc){

        if(loc != null && !loc.equals("")){
            String[] splitted = loc.split("[\\s,]");
            Log.d("URLParam Func","Location String: " + Arrays.toString(splitted));
            loc = "";
            for(int i = 0; i < splitted.length;i++){
                if(!splitted[i].equals("")) {
                    loc += splitted[i].toLowerCase() + "-";
                }
            }

            loc = loc.substring(0,loc.length()-1);

        }



        Log.d("URLParam Func","Location String: " + loc);
        return loc;
    }

    private static String formatDist(String dist){
        double convert = 1609.34; // meters to mile

        if(dist != null && !dist.equals("")){
             int distInt = Integer.parseInt(dist);
             distInt = (int)(distInt * convert);
             dist = Integer.toString(distInt);
        }

        Log.d("URLParam Func","Distance String: " + dist);

        return dist;
    }

    private static String formatPrices(Boolean[] prices){
        String returnString ="";

        for(int i = 0; i < prices.length;i++){
            if (prices[i]){
                returnString += (i+1) + ",";
            }
        }

        if(returnString.length() > 0){
            returnString =returnString.substring(0,returnString.length()-1);
        }


        Log.d("URLParam Func","prices string: " + returnString);

        return returnString;
    }


}