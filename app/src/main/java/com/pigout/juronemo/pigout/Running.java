package com.pigout.juronemo.pigout;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Running {

    private int total;
    private Business[] businessArray;
    private int[] randomIntArray;
    private HashMap<String, String> URLParam;
    private double[] Origin;
    private int current;
    private Yelp yelp;
    private YelpDetails yelpDetails;
    private GoogleMaps googleMaps;
    private GooglePlace googlePlace;

    public final static int SLICE = 1;

    public Running(HashMap<String, String> startURLParam, double[] startOrigin){
        this.URLParam = startURLParam;
        this.Origin = startOrigin;
        this.current = -1;

//        InputStream inputStream = context.getResources().openRawResource(R.raw.APIKEYS);
//        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
//        String eachline = bufferedReader.readLine();
//        while (eachline != null) {
//            // `the words in the file are separated by space`, so to get each words
//            String[] words = eachline.split(" ");
//            eachline = bufferedReader.readLine();
//        }

        String Yelp_key = "";
        String Google_key = "";
        String GooglePlace_key = Google_key;

        this.yelp = new Yelp(Yelp_key,URLParam);
        this.googlePlace = new GooglePlace(GooglePlace_key);
        this.yelpDetails = new YelpDetails(Yelp_key);

        if (startOrigin != null) {
            this.googleMaps = new GoogleMaps(Google_key, Origin);
        }else{
            this.googleMaps = null;
        }

        this.total = yelp.getTotal();
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


    public Business nextBus(){

        if(this.current + 1 >= this.total){
            return null;
        }
        else if(this.businessArray[(this.randomIntArray[this.current + 1])] != null) {
            this.current++;

            if(this.googleMaps != null && !this.businessArray[(this.randomIntArray[this.current])].getDurationBool())
            {
                this.googleMaps.getData(this.businessArray[(this.randomIntArray[this.current])]);
            }

            if(!this.businessArray[(this.randomIntArray[this.current])].getGooglePlaceBool()){
                this.googlePlace.getData(this.businessArray[(this.randomIntArray[this.current])]);
            }



            return businessArray[this.randomIntArray[this.current]];
        }else{
            // Business not already found

            this.current++;

            for (int j = this.current; j < this.current + SLICE; j++) {
                if (j >= this.total){
                    break;
                }else if ((this.businessArray[this.randomIntArray[j]] == null)) {
                    Business[] newBus = yelp.get50(this.randomIntArray[j]);

                    googlePlace.getData(newBus[0]);

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
