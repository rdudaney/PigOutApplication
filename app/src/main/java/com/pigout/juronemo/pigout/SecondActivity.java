package com.pigout.juronemo.pigout;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import org.json.JSONObject;

import java.util.HashMap;

public class SecondActivity extends AppCompatActivity {

    public Running currentRun;
    private Business currentBus;
    private TextView BusinessName_Text;
    private TextView Address_Text;
    private TextView Rating_Text;
    private ImageView Rating_Image;
    private TextView NumRating_Text;
    private TextView Time_Text;
    private TextView CurrentNum_Text;
    private TextView RandomNum_Text;
    private TextView Price_Text;
    private Button nextBut;
    private Button prevBut;
    private ImageView BusinessImage;
    private HashMap<String, String> URLParam;
    private ProgressBar progBar;
    private double[] Origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        this.prevBut = findViewById(R.id.prev_button);
        this.nextBut = findViewById(R.id.next_button);
        this.BusinessName_Text = findViewById(R.id.businessName);
        this.Address_Text = findViewById(R.id.address);
        this.Rating_Text = findViewById(R.id.rating);
        this.Rating_Image = findViewById(R.id.rating_image);
        this.NumRating_Text = findViewById(R.id.numRating);
        this.Time_Text = findViewById(R.id.time);
        this.Price_Text = findViewById(R.id.price);
        this.RandomNum_Text = findViewById(R.id.randomNum);
        this.CurrentNum_Text = findViewById(R.id.currentNum);
        this.progBar = findViewById(R.id.progBar);
        this.BusinessImage = findViewById(R.id.businessImage);

        Intent intent = getIntent();
        URLParam = (HashMap<String, String>)intent.getSerializableExtra("URLParam");
        Origin = intent.getDoubleArrayExtra("Origin");

        if(Origin != null) {
            Log.d("MAINVIEW", "Second: " + Double.toString(Origin[0]) + " , " + Double.toString(Origin[1]));
        }else{
            Log.d("MAINVIEW", "Second: NULL");
        }



        hideViews();

        runTask newTask = new runTask();
        newTask.execute();

        nextBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
            }
        });
        prevBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevButton();
            }
        });


    }

    public void nextButton(){
        runTask newTask = new runTask();
        newTask.execute();
    }

    public void prevButton(){
        currentBus = currentRun.prevBus();
        loadPic();
    }

    public void displayCurrent() {

        if (currentRun.getCurrent() <= 0) {
            prevBut.setVisibility(View.GONE);
        } else if (currentRun.getCurrent() >= currentRun.getTotal()) {
            nextBut.setVisibility(View.GONE);
        } else {
            prevBut.setVisibility(View.VISIBLE);
            nextBut.setVisibility(View.VISIBLE);
        }

//        BusinessName_Text.setText("NAME: " + currentBus.getName());
//        Address_Text.setText("ADDRESS: " + currentBus.getAddress());
//        Rating_Text.setText(String.format("RATING: %.1f", currentBus.getRating()));
//        NumRating_Text.setText("# OF RATINGS: " + Integer.toString(currentBus.getReview_Count()));
//        Price_Text.setText("PRICE: " + currentBus.getPrice());
//        CurrentNum_Text.setText("NUM IN CURRENT: " +currentRun.getCurrent() + " / " + currentRun.getTotal());
//        RandomNum_Text.setText("NUM IN RANDOM: " + currentRun.getRandom() + " / " + currentRun.getTotal());
//
//        if (currentBus.getDuration() == 0){
//            Time_Text.setText("TIME: N/A");
//        }else{
//            Time_Text.setText("TIME: " + Long.toString(Math.round(currentBus.getDuration())) + " Minutes");
//        }

        BusinessName_Text.setText(currentBus.getName());
        Address_Text.setText(currentBus.getAddress());
        Rating_Text.setText(String.format("RATING: %.1f", currentBus.getRating()));
        NumRating_Text.setText(Integer.toString(currentBus.getReview_Count()) + " Reviews");
        Price_Text.setText(currentBus.getPrice());
        CurrentNum_Text.setText("NUM IN CURRENT: " +currentRun.getCurrent() + " / " + currentRun.getTotal());
        RandomNum_Text.setText("NUM IN RANDOM: " + currentRun.getRandom() + " / " + currentRun.getTotal());

        if (currentBus.getDuration() == 0){
            Time_Text.setText("N/A Minutes");
        }else{
            Time_Text.setText(Long.toString(Math.round(currentBus.getDuration())) + " Minutes");
        }

        int YelpStars = (int)(currentBus.getRating()*10);
        Log.d("YELPSTARS",Integer.toString(YelpStars));
        String YelpStarsFilename = "stars_regular_" + YelpStars;
        int YelpStarsID = getResources().getIdentifier(YelpStarsFilename, "drawable", getPackageName());
        Rating_Image.setImageResource(YelpStarsID);



    }

    public void hideViews(){
        nextBut.setVisibility(View.GONE);
        prevBut.setVisibility(View.GONE);
        BusinessName_Text.setVisibility(View.GONE);
        Address_Text.setVisibility(View.GONE);
        Rating_Text.setVisibility(View.GONE);
        Rating_Image.setVisibility(View.GONE);
        NumRating_Text.setVisibility(View.GONE);
        Time_Text.setVisibility(View.GONE);
        Price_Text.setVisibility(View.GONE);
        CurrentNum_Text.setVisibility(View.GONE);
        RandomNum_Text.setVisibility(View.GONE);
        BusinessImage.setVisibility(View.GONE);
    }

    public void displayViews(){
        nextBut.setVisibility(View.VISIBLE);
        prevBut.setVisibility(View.VISIBLE);
        BusinessName_Text.setVisibility(View.VISIBLE);
        Address_Text.setVisibility(View.VISIBLE);
        Rating_Text.setVisibility(View.VISIBLE);
        Rating_Image.setVisibility(View.VISIBLE);
        NumRating_Text.setVisibility(View.VISIBLE);
        Time_Text.setVisibility(View.VISIBLE);
        Price_Text.setVisibility(View.VISIBLE);
        //CurrentNum_Text.setVisibility(View.VISIBLE);
        //RandomNum_Text.setVisibility(View.VISIBLE);
        BusinessImage.setVisibility(View.VISIBLE);

    }

    public void loadPic(){

        //TODO Look for errors when there is no pictures or business to be shown
        Picasso.get()
                .load(currentBus.getImageURL())
                .into(BusinessImage, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        displayCurrent();
                        displayViews();
                    }

                    @Override
                    public void onError(Exception ex) {

                    }
                });

    }



    private class runTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            hideViews();
            progBar.setVisibility(View.VISIBLE);

        }


        @Override
        protected Void doInBackground(Void... voids) {
            if (currentRun == null) {
                currentRun = new Running(URLParam, Origin);


            }
            currentBus = currentRun.nextBus();

//            Picasso.get().load(currentBus.getImageURL()).fetch();
//


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadPic();
            progBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);

        }
    }
}
