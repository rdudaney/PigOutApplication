package com.pigout.juronemo.pigout;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.view.LayoutInflater;
import android.content.Context;
import android.widget.TextView;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class BusinessFragmentCollectionAdapter extends FragmentStatePagerAdapter {
    SearchRun currentRun;
    Business currentBus;
    private HashMap<String, String> URLParam;
    private double[] origin;
    int itemNum;


    public BusinessFragmentCollectionAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setParameters(HashMap<String, String> startURLParam,double[] startOrigin)  {
        URLParam = startURLParam;
        origin = startOrigin;
    }



    @Override
    public int getCount() {
        return 1000;
    }



    @Override
    public Fragment getItem(int i) {
        itemNum = i;
        BusinessFragment businessFragment = new BusinessFragment();

        run2Task newTask = new run2Task(i,businessFragment);
        newTask.execute();

        return businessFragment;
    }





    private class run2Task extends AsyncTask<Void, Void, Void> {
        int busNum;
        Business aSyncBus;
        BusinessFragment busFrag;

        public run2Task(int startNum, BusinessFragment startFrag) {
            busNum = startNum;
            busFrag = startFrag;
        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            busFrag.update(aSyncBus);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (currentRun == null) {
                currentRun = new SearchRun(URLParam, origin);
            }
            aSyncBus = currentRun.getBusInd(busNum);
            return null;
        }




        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);

        }
    }


}
