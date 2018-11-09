package com.pigout.juronemo.pigout;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//import org.json.JSONObject;
import com.pigout.juronemo.pigout.Application;
import com.pigout.juronemo.pigout.GoogleMaps;
import org.json.*;
import java.util.HashMap;
import java.util.Map;



public class Main {
    public static void main(String[] args) {
        double[] Origin = {34.17311,-118.60071};
        HashMap<String, String> URLParam = new HashMap<>();
        URLParam.put("term", "Food");
        URLParam.put("location", "Woodland-Hills-CA");
        URLParam.put("latitude", "");
        URLParam.put("longitude", "");
        URLParam.put("radius", "");
        URLParam.put("categories", "");
        URLParam.put("locale", "");
//        URLParam.put("limit", "50");
//        URLParam.put("offset", "50");
        URLParam.put("sort_by", "");
        URLParam.put("price", "");
        URLParam.put("open_now", "");
        URLParam.put("open_at", "");
        URLParam.put("attributes", "");

        String Yelp_key = "X65ClnpzUb1NxgVieFb2aYfgKhsT8ddyOaypzBKWjH7E6jaCS6mUINvbb2bMm1kp9YbzItxiKZj-SmM04X2PjrdggByvQa2H37L3fivtD-hH48tP5EqLh3pRf96iW3Yx";
        Yelp yelp = new Yelp(Yelp_key,URLParam);

        String Google_key = "AIzaSyDULakzPzA_91C-EV9eLtYphqzqa0oIm3I";
        GoogleMaps google = new GoogleMaps(Google_key,Origin);

        Application.run(yelp,google);




//        System.out.println(yelp.total());
//        Business[] bus_list = yelp.all();
//        System.out.println(bus_list.length);
//
//
//        google.time_all(bus_list);
//
//        for (int i = 0; i < bus_list.length;i++){
//            System.out.println(String.format("%-6s %s",(i+1)+": ",bus_list[i].toString()));
//
//        }
//
//        int offset = 999; // total -1
//        Business[] bus_list50 = yelp.get50(offset);
//        System.out.println("Length of 50: " + bus_list50.length);
//
//        google.time_all(bus_list50);
//
//
//        for (int i = 0; i < bus_list50.length;i++){
//            System.out.println(String.format("%-6s %s",(offset+i+1)+": ",bus_list50[i].toString()));
//
//        }
//
//
    }
}
