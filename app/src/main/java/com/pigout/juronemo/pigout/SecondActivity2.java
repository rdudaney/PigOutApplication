package com.pigout.juronemo.pigout;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class SecondActivity2 extends AppCompatActivity {
    private ViewPager viewPager;
    private BusinessFragmentCollectionAdapter adapter;
    private HashMap<String, String> URLParam;
    private double[] origin;

    SearchRun currentRun;
    Business currentBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second2);

        if(savedInstanceState == null) {
            Log.d("State", "savedInstanceState is NULL");
            Intent intent = getIntent();
            URLParam = (HashMap<String, String>) intent.getSerializableExtra("URLParam");
            origin = intent.getDoubleArrayExtra("Origin");
        }

//        viewPager = findViewById(R.id.view_pager);
//        adapter = new BusinessPageAdapter(URLParam,origin);
//        viewPager.setAdapter(adapter);


        adapter = new BusinessFragmentCollectionAdapter(getSupportFragmentManager());
        adapter.setParameters(URLParam,origin);

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
    }



}
