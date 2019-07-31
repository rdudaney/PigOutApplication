package com.pigout.juronemo.pigout;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class BusinessFragmentCollectionAdapter extends FragmentStatePagerAdapter {
    SearchRun currentRun;
    private HashMap<String, String> URLParam;
    private double[] origin;
    private int total;


    public BusinessFragmentCollectionAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setParameters(HashMap<String, String> startURLParam,double[] startOrigin){
        URLParam = startURLParam;
        origin = startOrigin;

    }



    @Override
    public int getCount() {
        return 1000;
    } //TODO: Have this return CurrentRun.total()



    @Override
    public Fragment getItem(int i) {
        BusinessFragment businessFragment = new BusinessFragment();

        runTask newTask = new runTask(i,businessFragment);
        newTask.execute();

        return businessFragment;
    }





    private class runTask extends AsyncTask<Void, Void, Void> {
        int busNum;
        Business aSyncBus;
        BusinessFragment busFrag;

        public runTask(int startNum, BusinessFragment startFrag) {
            busNum = startNum;
            busFrag = startFrag;
        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (busFrag != null) {
                busFrag.update(aSyncBus);
            }
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
