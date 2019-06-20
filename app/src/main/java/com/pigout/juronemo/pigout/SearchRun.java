package com.pigout.juronemo.pigout;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class SearchRun implements Serializable {

    private int total;
    private Business[] businessArray;
    private int[] randomIntArray;
    private HashMap<String, String> URLParam;
    private double[] origin;
    private int current;
    private YelpSearch yelpSearch;
    private YelpDetails yelpDetails;
    private GoogleMaps googleMaps;
    private GooglePlaceSearch googlePlaceSearch;
    public final static int SLICE = 1;


    public SearchRun(HashMap<String, String> startURLParam, double[] startOrigin){
        this.URLParam = startURLParam;
        this.origin = startOrigin;
        this.current = -1;


        String yelpKey = APIKeys.getYelpKey();
        String googleKey = APIKeys.getGoogleKey();

        this.yelpSearch = new YelpSearch(yelpKey,URLParam);
        this.yelpDetails = new YelpDetails(yelpKey);
        this.googlePlaceSearch = new GooglePlaceSearch(googleKey);
        this.googleMaps = null;
        if (startOrigin != null) {
            this.googleMaps = new GoogleMaps(googleKey, origin);
        }

        this.total = yelpSearch.getTotal();
        this.businessArray = new Business[this.total];
        randomInts(this.total);

        Log.d("STATE", "randomIntArray: " + Arrays.toString(randomIntArray));
        Log.d("STATE","this.total: " + this.total);

    }

    private void randomInts(int max){
        int[] randomArray = new int[max];
        ArrayList<Integer> arrL = new ArrayList<>();
        Random rand = new Random();

        for(int i = 0; i < max; i++){
            arrL.add(i);
        }

        for(int i = 0; i < max; i++){
            int rand_int = rand.nextInt(arrL.size());
            randomArray[i] = arrL.get(rand_int);
            arrL.remove(rand_int);
        }

        this.randomIntArray = randomArray;
    }

    public int getTotal(){return this.total;}
    public int getCurrent(){return this.current;}
    public int getRandom(){return this.randomIntArray[this.current];}
    public GoogleMaps getGoogleMaps(){return this.googleMaps;}

    public Business nextBus(){

        // End of list
        if(this.current + 1 >= this.total){
            return null;
        }
        // Next Business is not null
        else if(this.businessArray[(this.randomIntArray[this.current + 1])] != null) {
            this.current++;

            // Google Maps/ Duration information is Null then find it
            if(this.googleMaps != null && !this.businessArray[(this.randomIntArray[this.current])].getGoogleMapsBool())
            {
                this.googleMaps.getData(this.businessArray[(this.randomIntArray[this.current])]);
            }

            // Google Place information is Null then find it
            if(!this.businessArray[(this.randomIntArray[this.current])].getGooglePlaceSearchBool()){
                this.googlePlaceSearch.getData(this.businessArray[(this.randomIntArray[this.current])]);
            }

            return businessArray[this.randomIntArray[this.current]];
        }
        // Next Business is Null
        else{

            this.current++;

            for (int j = this.current; j < this.current + SLICE; j++) {
                if (j >= this.total){
                    break;
                }else if ((this.businessArray[this.randomIntArray[j]] == null)) {
                    Business[] newBus = yelpSearch.get50(this.randomIntArray[j]);

                    googlePlaceSearch.getData(newBus[0]);

                    if (this.googleMaps != null) {
                        this.googleMaps.getData(newBus[0]);
                    }

                    //this.yelpDetails.addInfo(newBus[0]); // TODO REMOVE THIS, ONLY FOR TEST

                    for (int k = 0; k < newBus.length;k++){
                        this.businessArray[this.randomIntArray[j]+k] = newBus[k];
                    }


                }
            }
            return businessArray[this.randomIntArray[this.current]];
        }
    }
    public Business prevBus(){
        if(this.current == 0){
            return this.businessArray[this.randomIntArray[this.current]];
        }
        else if(this.current > 0 || this.current < this.total) {
            this.current--;
            return this.businessArray[this.randomIntArray[this.current]];
        }else {
            return null;
        }
    }
    public Business peekBus(){
        return this.businessArray[this.randomIntArray[this.current+1]];
    }

}
