package com.pigout.juronemo.pigout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Business implements Serializable {

    // Yelp Search
    private String id;
    private String name;
    private double rating;
    private String price;
    private int review_count;
    private String total_address;
    private double latitude;
    private double longitude;
    private double[] coordinates;
    private String imageURL;
    private double distance;
    private String type;

    // Google Maps
    private double duration;
    private Boolean googleMapsBool = false;

    // Google Place Search
    private double googleRating;
    private Boolean googlePlaceSearchBool = false;
    private String googlePlaceSearchID;

    // Google Place Details
    private Boolean googlePlaceDetailsBool = false;
    ArrayList<String> googlePhotoList;


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

        this.googleMapsBool = false;
        this.googlePlaceSearchBool = false;
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


    // Yelp Search functions
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


    // Google Maps
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
        this.googleMapsBool = true;
    }
    public void setDuration(double startDur){
        this.duration = startDur;
        this.googleMapsBool = true;
    }
    public double getDuration(){return this.duration;}
    public void setGoogleMapsBool(Boolean startBool){this.googleMapsBool = startBool;}
    public Boolean getGoogleMapsBool(){return this.googleMapsBool;}

    // Google Place Search
    public void setGooglePlaceSearch(JSONObject Response){
        double rating = -1;
        String status = "";
        try {
            JSONObject firstCandidate = Response.getJSONArray("candidates").getJSONObject(0);
            rating = firstCandidate.getDouble("rating");
            status = Response.getString("status");
            this.googlePlaceSearchID = firstCandidate.getString("place_id");
        }catch (Exception e){
        }

        if(!status.equals("OK")){
            rating = -1;
        }

        this.googleRating = rating;
        this.googlePlaceSearchBool = true;

    }
    public void setGoogleRating(double startGoogleRating){
        this.googleRating = startGoogleRating;
        this.googlePlaceSearchBool = true;
    }
    public double getGoogleRating(){return this.googleRating;}
    public String getGooglePlaceSearchID(){return this.googlePlaceSearchID;}
    public Boolean getGooglePlaceSearchBool(){return this.googlePlaceSearchBool;}


    // Google Place Details
    public void setGooglePlaceDetails(JSONObject Response){
        try {
            this.googlePhotoList = new ArrayList<String>();
            JSONObject results = Response.getJSONObject("result");
            JSONArray photos = results.getJSONArray("photos");

            for(int i = 0; i < photos.length();i++) {
                JSONObject photoInfo = photos.getJSONObject(i);
                googlePhotoList.add("https://maps.googleapis.com/maps/api/place/photo?maxwidth=1600&photoreference=" + photoInfo.getString("photo_reference") + "&key=" + APIKeys.getGoogleKey());
            }


        }catch (Exception e){
        }


    }
    public Boolean getGooglePlaceDetailsBool(){return this.googlePlaceDetailsBool;}
    public ArrayList<String> getGooglePhotoList(){return this.googlePhotoList;}




    // Google Place Photos

    // string[] getGooglePhotos()  gets a list of google photo urls (https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&fields=name,rating,formatted_phone_number&key=YOUR_API_KEY)
    // need to pass


}
