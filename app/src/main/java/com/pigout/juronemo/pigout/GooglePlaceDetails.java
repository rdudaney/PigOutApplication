package com.pigout.juronemo.pigout;

import org.json.JSONObject;

public class GooglePlaceDetails {

    private String API_Key;

    public GooglePlaceDetails(String startAPI){
        this.API_Key = startAPI;
    }

    public void getData(Business dest){
        String placeID = dest.getGooglePlaceSearchID();


        JSONObject Response = new JSONObject();
        try {
            Response = HelperFunctions.urlGetRequest(setupURL(placeID),this.API_Key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dest.setGooglePlaceDetails(Response);




    }

    private String setupURL(String placeID){
        String fields = "photo";

        String url = "https://maps.googleapis.com/maps/api/place/details/json?" ;
        url += "placeid=" + placeID+ "&";
        url += "fields=" + fields + "&";
        url += "key=" + API_Key ;


        //https://maps.googleapis.com/maps/api/place/details/json?placeid=ChIJN1t_tDeuEmsRUsoyG83frY4&fields=name,rating,formatted_phone_number&key=YOUR_API_KEY

        return url;
    }
}
