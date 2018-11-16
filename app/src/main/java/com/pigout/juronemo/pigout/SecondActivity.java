package com.pigout.juronemo.pigout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class SecondActivity extends AppCompatActivity {

    private Running currentRun;
    private Business currentBus;
    private TextView BusinessName_Text;
    private TextView Address_Text;
    private TextView Rating_Text;
    private TextView CurrentNum_Text;
    private TextView RandomNum_Text;
    private Button nextBut;
    private Button prevBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        this.prevBut = findViewById(R.id.prev_button);
        this.nextBut = findViewById(R.id.next_button);
        this.BusinessName_Text = findViewById(R.id.businessName);
        this.Address_Text = findViewById(R.id.address);
        this.Rating_Text  = findViewById(R.id.rating);
        this.RandomNum_Text = findViewById(R.id.randomNum);
        this.CurrentNum_Text = findViewById(R.id.currentNum);

        HashMap<String, String> URLParam = new HashMap<>();
        URLParam.put("term", "Food");
        URLParam.put("location", "Woodland-Hills-CA");
        URLParam.put("latitude", "");
        URLParam.put("longitude", "");
        URLParam.put("radius", "");
        URLParam.put("categories", "");
        URLParam.put("locale", "");
//        URLParam.put("limit", "50");
//        URLParam.put("offset", "50");
        URLParam.put("sort_by", "");
        URLParam.put("price", "");
        URLParam.put("open_now", "");
        URLParam.put("open_at", "");
        URLParam.put("attributes", "");

        BusinessName_Text.setText("RUNNING");
        Log.d("STATE","test");
        this.currentRun = new Running(URLParam,null);
        this.currentBus = currentRun.nextBus();
        Log.d("STATE","total: " + currentRun.getTotal());
        displayCurrent();
    }


    public void displayCurrent(){
        if (currentRun.getCurrent() <= 0){
            prevBut.setVisibility(View.GONE);
        }else if (currentRun.getCurrent() >= currentRun.getTotal()){
            nextBut.setVisibility(View.GONE);
        }else {
            prevBut.setVisibility(View.VISIBLE);
            nextBut.setVisibility(View.VISIBLE);
        }

        BusinessName_Text.setText(currentBus.getName());
        Address_Text.setText(currentBus.getAddress());
        Rating_Text.setText(String.format("%.1f",currentBus.getRating()));
        CurrentNum_Text.setText(currentRun.getCurrent() + " / " + currentRun.getTotal());
        RandomNum_Text.setText(currentRun.getRandom() + " / " + currentRun.getTotal());


    }

}
