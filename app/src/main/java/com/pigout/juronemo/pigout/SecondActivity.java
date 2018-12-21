package com.pigout.juronemo.pigout;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import com.squareup.picasso.Picasso;

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
    private TextView Distance_Text;
    private TextView Type_Text;
    private TextView GoogleRating_Text;
    private RatingBar GoogleRating_Image;
    private Button nextBut;
    private Button prevBut;
    private Button selBut;
    private ImageView BusinessImage;
    private HashMap<String, String> URLParam;
    private ProgressBar progBar;
    private double[] Origin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar2);
        setSupportActionBar(myToolbar);



        this.prevBut = findViewById(R.id.prev_button);
        this.nextBut = findViewById(R.id.next_button);
        this.selBut = findViewById(R.id.select_button);
        this.BusinessName_Text = findViewById(R.id.businessName);
        this.Address_Text = findViewById(R.id.address);
        this.Rating_Text = findViewById(R.id.rating);
        this.Rating_Image = findViewById(R.id.rating_image);
        this.NumRating_Text = findViewById(R.id.numRating);
        this.Time_Text = findViewById(R.id.time);
        this.Price_Text = findViewById(R.id.price);
        this.RandomNum_Text = findViewById(R.id.randomNum);
        this.CurrentNum_Text = findViewById(R.id.currentNum);
        this.Distance_Text = findViewById(R.id.distance);
        this.progBar = findViewById(R.id.progBar);
        this.BusinessImage = findViewById(R.id.businessImage);
        this.Type_Text = findViewById(R.id.type);
        this.GoogleRating_Text =findViewById(R.id.GoogleRatingText);
        this.GoogleRating_Image =findViewById(R.id.GoogleRatingImage);

        Intent intent = getIntent();
        URLParam = (HashMap<String, String>)intent.getSerializableExtra("URLParam");
        Origin = intent.getDoubleArrayExtra("Origin");




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

        if (currentRun.getCurrent() + 1 >= currentRun.getTotal()) {
            Toast toast = Toast.makeText(this, "No more Restaurants", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();

         // haven't yet got the next Business, run ASyncTask
        }else if (currentRun.peekBus() == null){
            runTask newTask = new runTask();
            newTask.execute();

         //have got next Business, don't run ASyncTask
        }else{
            currentBus = currentRun.nextBus();
            loadAll();
        }

    }

    public void prevButton(){
        if (currentRun.getCurrent() == 0){
            Toast toast = Toast.makeText(this, "No previous Restaurants", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }else{
            currentBus = currentRun.prevBus();
            loadAll();
        }


    }

    public void loadText() {
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
        NumRating_Text.setText(currentBus.getRating() +" / " + Integer.toString(currentBus.getReview_Count()) + " Reviews");
        if(currentBus.getPrice().equals("N/A")){
            Price_Text.setText("Price Not Available");
        }else{
            Price_Text.setText(currentBus.getPrice());
        }
        CurrentNum_Text.setText("NUM IN CURRENT: " +currentRun.getCurrent() + " / " + currentRun.getTotal());
        RandomNum_Text.setText("NUM IN RANDOM: " + currentRun.getRandom() + " / " + currentRun.getTotal());
        Distance_Text.setText(String.format("%.1f", currentBus.getDistance()) + " mi");

        if (currentBus.getDuration() == 0 || currentBus.getDuration() == -1 || !currentBus.getDurationBool()){
            Time_Text.setVisibility(View.GONE);
        }else{
            Time_Text.setText(Long.toString(Math.round(currentBus.getDuration())) + " min");
        }

        int YelpStars = (int)(currentBus.getRating()*10);
        String YelpStarsFilename = "stars_regular_" + YelpStars;
        int YelpStarsID = getResources().getIdentifier(YelpStarsFilename, "drawable", getPackageName());
        Rating_Image.setImageResource(YelpStarsID);
        Type_Text.setText(currentBus.getType());

        if(currentBus.getGoogleRating() == -1){
            GoogleRating_Text.setVisibility(View.GONE);
            GoogleRating_Image.setVisibility(View.GONE);
        }else{
            GoogleRating_Text.setText(Double.toString(currentBus.getGoogleRating()));
            GoogleRating_Image.setRating((float)currentBus.getGoogleRating());
        }



    }

    public void hideViews(){
        nextBut.setVisibility(View.GONE);
        prevBut.setVisibility(View.GONE);
        selBut.setVisibility(View.GONE);
        BusinessName_Text.setVisibility(View.GONE);
        Address_Text.setVisibility(View.GONE);
        Rating_Text.setVisibility(View.GONE);
        Rating_Image.setVisibility(View.GONE);
        NumRating_Text.setVisibility(View.GONE);
        Time_Text.setVisibility(View.GONE);
        Price_Text.setVisibility(View.GONE);
        CurrentNum_Text.setVisibility(View.GONE);
        RandomNum_Text.setVisibility(View.GONE);
        Distance_Text.setVisibility(View.GONE);
        Type_Text.setVisibility(View.GONE);
        BusinessImage.setVisibility(View.GONE);
        GoogleRating_Text.setVisibility(View.GONE);
        GoogleRating_Image.setVisibility(View.GONE);

    }

    public void displayViews(){
        nextBut.setVisibility(View.VISIBLE);
        prevBut.setVisibility(View.VISIBLE);
        selBut.setVisibility(View.VISIBLE);
        BusinessName_Text.setVisibility(View.VISIBLE);
        Address_Text.setVisibility(View.VISIBLE);
        //Rating_Text.setVisibility(View.VISIBLE);
        Rating_Image.setVisibility(View.VISIBLE);
        NumRating_Text.setVisibility(View.VISIBLE);
        if (currentBus.getDuration() == 0 || currentBus.getDuration() == -1 || !currentBus.getDurationBool()) {
            Time_Text.setVisibility(View.GONE); //TODO: Eventually combine this and the one before
        }else{
            Time_Text.setVisibility(View.VISIBLE);
        }
        Price_Text.setVisibility(View.VISIBLE);
        //CurrentNum_Text.setVisibility(View.VISIBLE);
        //RandomNum_Text.setVisibility(View.VISIBLE);
        Type_Text.setVisibility(View.VISIBLE);
        Distance_Text.setVisibility(View.VISIBLE);
        BusinessImage.setVisibility(View.VISIBLE);

        if(currentBus.getGoogleRating() == -1){
            GoogleRating_Text.setVisibility(View.GONE);
            GoogleRating_Image.setVisibility(View.GONE);
        }else{
            GoogleRating_Text.setVisibility(View.VISIBLE);
            GoogleRating_Image.setVisibility(View.VISIBLE);
        }


    }

    public void loadAll(){
        Log.d("State","Current: " + currentRun.getCurrent());
        Log.d("State","Random: " + currentRun.getRandom());
        Log.d("State","Total: " + currentRun.getTotal());
        if(currentBus == null){
            Toast.makeText(this, "ERROR!", Toast.LENGTH_LONG).show();
        }else if(currentBus.getImageURL() == null || currentBus.getImageURL().equals("")){

            Picasso.get()
                    .load(R.drawable.noimage)
                    .into(BusinessImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            loadText();
                            displayViews();
                        }

                        @Override
                        public void onError(Exception ex) {

                        }
                    });
        }else{
            Picasso.get()
                    .load(currentBus.getImageURL())
                    .into(BusinessImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            loadText();
                            displayViews();
                        }

                        @Override
                        public void onError(Exception ex) {

                        }
                    });
        }


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
            loadAll();
            progBar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);

        }
    }
}
