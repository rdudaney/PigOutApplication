package com.pigout.juronemo.pigout;

import android.util.Log;

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
    private GoogleMaps google;

    public final static int SLICE = 20;

    public Running(HashMap<String, String> startURLParam, double[] startOrigin){
        this.URLParam = startURLParam;
        this.Origin = startOrigin;
        this.current = -1;

        String Yelp_key = "X65ClnpzUb1NxgVieFb2aYfgKhsT8ddyOaypzBKWjH7E6jaCS6mUINvbb2bMm1kp9YbzItxiKZj-SmM04X2PjrdggByvQa2H37L3fivtD-hH48tP5EqLh3pRf96iW3Yx";
        String Google_key = "AIzaSyDULakzPzA_91C-EV9eLtYphqzqa0oIm3I";

        this.yelp = new Yelp(Yelp_key,URLParam);

        if (startOrigin != null) {
            this.google = new GoogleMaps(Google_key, Origin);
        }else{
            this.google = null;
        }

        this.total = yelp.getTotal();
        this.businessArray = new Business[this.total];
        randomInts(this.total);
        Log.d("STATE","Running created");
        Log.d("STATE", Arrays.toString(randomIntArray));

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
        else if(this.businessArray[this.randomIntArray[this.current + 1]] != null) {
            this.current++;
            return businessArray[this.randomIntArray[this.current]];
        }else{
            for (int j = this.current; j < this.current + SLICE; j++) {
                if (j >= this.total){
                    break;
                }else if ((this.businessArray[this.randomIntArray[j]] == null)) {
                    Business[] newBus = yelp.get50(this.randomIntArray[j]);

                    if (this.google != null) {
                        this.google.time_all(newBus);
                    }

                    for (int k = 0; k < newBus.length;k++){
                        this.businessArray[this.randomIntArray[j+k]] = newBus[k];
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

}
