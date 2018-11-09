package com.pigout.juronemo.pigout;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Application {

    public static void run(Yelp yelp, GoogleMaps google){
        int total = yelp.total();
        int slice = 20;
        Business[] bus_list = new Business[total];
        String toEnd = "";
        Scanner sc = new Scanner(System.in);
        int i = 0;
        int[] random_int_list = randomInts(total);

        while (!toEnd.equals("n")){
            System.out.println("Run i: " + i);
            if (i >= total){
                System.out.println("End of available Businesses");
                return;
            }
            if(bus_list[random_int_list[i]] == null){
                nextBusSlice(random_int_list,slice,bus_list,i,yelp, google);
            }


            System.out.println((random_int_list[i]+1)+ ": " + bus_list[random_int_list[i]]);

            System.out.println("Press 'n' to stop: ");
            toEnd = sc.next();
            i++;
        }

    }
    public static int[] randomInts(int max){
        int[] randomArray = new int[max];
        ArrayList<Integer> arrL = new ArrayList<>();
        Random rand = new Random();

        for(int i = 0; i < max; i++){
            arrL.add(i);
        }
        System.out.println(arrL.size());

        for(int i = 0; i < max; i++){
            int rand_int = rand.nextInt(arrL.size());
            randomArray[i] = arrL.get(rand_int);
            arrL.remove(rand_int);
//            System.out.println(randomArray[i]);
//            System.out.println(arrL);
//            System.out.println(Arrays.toString(randomArray));
        }
        System.out.println(Arrays.toString(randomArray));
        return randomArray;
    }

    public static void nextBusSlice(int[] random_int_list, int slice, Business[] bus_list, int i,Yelp yelp, GoogleMaps google) {

        for (int j = i; j < i + slice; j++) {
            if (j >= random_int_list.length){
                return;
            }
            if ((bus_list[random_int_list[j]] == null)) {
                System.out.println("nextBusSlice: j: "+ j + " randomnumber: " + random_int_list[j]);
                Business[] newBus = yelp.get50(random_int_list[j]);
                google.time_all(newBus);
                for (int k = 0; k<newBus.length;k++){
                    bus_list[random_int_list[j+k]] = newBus[k];
                }
            }


        }
    }


}
