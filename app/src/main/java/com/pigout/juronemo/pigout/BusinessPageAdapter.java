package com.pigout.juronemo.pigout;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class BusinessPageAdapter extends PagerAdapter {

    private SearchRun currentRun;
    private Business currentBus;
    private HashMap<String, String> URLParam;
    private double[] origin;

    ProgressBar progBar;
    TextView txtDisplay;


    //TODO: Change itemNum in ASyncTask to a local variable that is passed?

    public BusinessPageAdapter(HashMap<String, String> startURLParam,double[] startOrigin) {
        URLParam = startURLParam;
        origin = startOrigin;
    }

    @Override
    public int getCount() {
        return 1000;//currentRun.getTotal();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View page = inflater.inflate(R.layout.fragment_business, null);

        progBar = page.findViewById(R.id.progBar);
        //txtDisplay = page.findViewById(R.id.textTest);

        progBar.setVisibility(View.VISIBLE);

        run3Task newTask = new run3Task(position);
        newTask.execute();

        container.addView(page);
        return page;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((View) o);
    }


    private class run3Task extends AsyncTask<Void, Void, Void> {
        int busNum;
        Business aSyncBus;

        public run3Task(int startNum) {
            busNum = startNum;
        }

        @Override
        protected void onPreExecute() {


        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progBar.setVisibility(View.GONE);
            txtDisplay.setText(aSyncBus.getName());
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

