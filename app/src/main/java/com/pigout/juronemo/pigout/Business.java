package com.pigout.juronemo.pigout;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Business {

//    private String name;
//    private double rating;
//    private String price;
//    private int price_int;
//    private JSONObject address_obj;
//    private String address_string;
//    private int numRating;

//    private String id;
    private String name;
    private double rating;
    private String price;
    private int review_count;
    private String total_address;
    private double latitude;
    private double longitude;
    private double[] coordinates;
    private double duration;
    private String imageURL;
    private double distance;
    private String type;
    private Boolean durationBool;
    private double googleRating;
    private Boolean googlePlaceBool;
    private String id;


    public Business(JSONObject startObj){
        try{
            this.name = startObj.getString("name");
            this.rating = startObj.getDouble("rating");
            this.review_count = startObj.getInt("review_count");
            this.total_address = displayAddress(startObj.getJSONObject("location"));
            this.coordinates = setCoor(startObj.getJSONObject("coordinates"));
            this.imageURL = startObj.getString("image_url");
            this.distance = startObj.getDouble("distance");
            this.type = startObj.getJSONArray("categories").getJSONObject(0).getString("title");
            this.id = startObj.getString("id");

        } catch(JSONException e){
        }


        try {
            this.price = startObj.getString("price");
        } catch (Exception e) {
            this.price = "N/A";
        }

        this.durationBool = false;
        this.googlePlaceBool = false;
        this.latitude = this.coordinates[0];
        this.longitude = this.coordinates[1];




    }

    private String displayAddress(JSONObject location) {
        JSONArray disp_address_array = null;
        try {
            disp_address_array = location.getJSONArray("display_address");
        }catch (JSONException e){
        }

        String address_string = "";

        for (int i = 0; i < disp_address_array.length(); i++) {
            try {
                address_string += disp_address_array.getString(i) + ", ";
            }catch (JSONException e){
            }
        }
        address_string = address_string.substring(0,address_string.length()-2);

        return address_string;
    }


    // Yelp functions
    private double[] setCoor(JSONObject coor){
        double[] coordinates = new double[2];

        try {
            coordinates[0] = coor.getDouble("latitude");
            coordinates[1] = coor.getDouble("longitude");
        }catch (JSONException e){
        }

        return coordinates;
    }

    public String getName(){return this.name;}
    public double getRating(){return this.rating;}
    public String getPrice(){return this.price;}
    public int getReview_Count(){return this.review_count;}
    public String getAddress(){return this.total_address;}
    public double[] getCoordinates(){return this.coordinates;}
    public double getDistance(){return this.distance/1609.34;}
    public double getLatitude(){return this.latitude;}
    public double getLongitude(){return this.longitude;}
    public String getType(){return this.type;}
    public String getImageURL() {
        return imageURL;
    }
    public String getId(){return this.id;}


    // Google Maps functions
    public void setGoogleMaps(JSONObject Response){

        JSONObject rows;
        try {
            rows = Response.getJSONArray("rows").getJSONObject(0);
            JSONObject element = rows.getJSONArray("elements").getJSONObject(0);
            JSONObject duration = element.getJSONObject("duration_in_traffic");
            this.duration = duration.getInt("value")/60.0;
        } catch (Exception e) {
            this.duration = -1;

        }
        this.durationBool = true;
    }
    public void setDuration(double startDur){
        this.duration = startDur;
        this.durationBool = true;
    }
    public double getDuration(){return this.duration;}
    public Boolean getDurationBool(){return this.durationBool;}

    // Google Place functions
    public void setGooglePlace(JSONObject Response){
        double rating = -1;
        String status = "";
        try {
            rating = Response.getJSONArray("candidates").getJSONObject(0).getDouble("rating");
            status = Response.getString("status");
        }catch (Exception e){
        }

        if(!status.equals("OK")){
            rating = -1;
        }

        this.googleRating = rating;
        this.googlePlaceBool = true;

    }
    public void setGoogleRating(double startGoogleRating){
        this.googleRating = startGoogleRating;
        this.googlePlaceBool = true;
    }
    public Boolean getGooglePlaceBool(){return this.googlePlaceBool;}
    public double getGoogleRating(){return this.googleRating;}



}
