package com.pigout.juronemo.pigout;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessFragment extends Fragment {

    private static final int BUSINESS_DETAIL_INTEGER_VALUE = 614;

    Business currentBus;

    private TextView BusinessName_Text;
    private ImageView BusinessImage;
    private ProgressBar progressBar;
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



    public BusinessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_business, container, false);

        findViews(view);
        hideViews();


        return view;
    }

    public void select_Click(View view){

        Intent goBusinessDetail = new Intent(getContext(), BusinessDetailActivity.class);
        goBusinessDetail.putExtra("CurrentBus",currentBus);


        startActivityForResult(goBusinessDetail,BUSINESS_DETAIL_INTEGER_VALUE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case (BUSINESS_DETAIL_INTEGER_VALUE): {
                currentBus = (Business) data.getExtras().get("currentBus");
                break;
            }
        }
    }



    public void findViews(View view){

        progressBar = view.findViewById(R.id.progBar);

        BusinessName_Text = view.findViewById(R.id.businessName);
        Address_Text = view.findViewById(R.id.address);
        Rating_Text = view.findViewById(R.id.rating);
        NumRating_Text = view.findViewById(R.id.numRating);
        Time_Text = view.findViewById(R.id.time);
        Price_Text = view.findViewById(R.id.price);
        RandomNum_Text = view.findViewById(R.id.randomNum);
        CurrentNum_Text = view.findViewById(R.id.currentNum);
        Distance_Text = view.findViewById(R.id.distance);
        Type_Text = view.findViewById(R.id.type);
        GoogleRating_Text = view.findViewById(R.id.GoogleRatingText);

        BusinessImage = view.findViewById(R.id.businessImage);
        Rating_Image = view.findViewById(R.id.rating_image);
        GoogleRating_Image = view.findViewById(R.id.GoogleRatingImage);

        prevBut = view.findViewById(R.id.prev_button);
        nextBut = view.findViewById(R.id.next_button);
        selBut = view.findViewById(R.id.select_button);


    }

    public void displayViews() {
        progressBar.setVisibility(View.GONE);

        BusinessName_Text.setVisibility(View.VISIBLE);
        Address_Text.setVisibility(View.VISIBLE);
        NumRating_Text.setVisibility(View.VISIBLE);
        Price_Text.setVisibility(View.VISIBLE);
        Distance_Text.setVisibility(View.VISIBLE);
        Type_Text.setVisibility(View.VISIBLE);
        if (currentBus.getDuration() == 0 || currentBus.getDuration() == -1 || !currentBus.getGoogleMapsBool()) {
            Time_Text.setVisibility(View.GONE);
        }else{
            Time_Text.setVisibility(View.VISIBLE);
        }

        BusinessImage.setVisibility(View.VISIBLE);
        Rating_Image.setVisibility(View.VISIBLE);

        if(currentBus.getGoogleRating() == -1){
            GoogleRating_Text.setVisibility(View.GONE);
            GoogleRating_Image.setVisibility(View.GONE);
        }else{
            GoogleRating_Text.setVisibility(View.VISIBLE);
            GoogleRating_Image.setVisibility(View.VISIBLE);
        }


        prevBut.setVisibility(View.VISIBLE);
        nextBut.setVisibility(View.VISIBLE);
        selBut.setVisibility(View.VISIBLE);


    }

    public void hideViews(){

        progressBar.setVisibility(View.VISIBLE);

        BusinessName_Text.setVisibility(View.GONE);
        Address_Text.setVisibility(View.GONE);
        NumRating_Text.setVisibility(View.GONE);
        Time_Text.setVisibility(View.GONE);
        Price_Text.setVisibility(View.GONE);
        Distance_Text.setVisibility(View.GONE);
        Type_Text.setVisibility(View.GONE);
        GoogleRating_Text.setVisibility(View.GONE);

        BusinessImage.setVisibility(View.GONE);
        Rating_Image.setVisibility(View.GONE);
        GoogleRating_Image.setVisibility(View.GONE);

        prevBut.setVisibility(View.GONE);
        nextBut.setVisibility(View.GONE);
        selBut.setVisibility(View.GONE);


    }

    public void setViews(){
        BusinessName_Text.setText(currentBus.getName());
        Address_Text.setText(currentBus.getAddress());
        NumRating_Text.setText(currentBus.getRating() +" / " + Integer.toString(currentBus.getReview_Count()) + " Reviews");
        if(currentBus.getPrice().equals("N/A")){
            Price_Text.setText("Price Not Available");
        }else{
            Price_Text.setText(currentBus.getPrice());
        }
        Distance_Text.setText(String.format("%.1f", currentBus.getDistance()) + " mi");

        if (currentBus.getDuration() == 0 || currentBus.getDuration() == -1 || !currentBus.getGoogleMapsBool()){
            Time_Text.setVisibility(View.GONE);
        }else{
            Time_Text.setText(Long.toString(Math.round(currentBus.getDuration())) + " min");
        }

        int YelpStars = (int)(currentBus.getRating()*10);
        String YelpStarsFilename = "stars_regular_" + YelpStars;
        if(getActivity() != null){
            int YelpStarsID = getResources().getIdentifier(YelpStarsFilename, "drawable", getActivity().getPackageName());
            Rating_Image.setImageResource(YelpStarsID);
        }
        Type_Text.setText(currentBus.getType());

        if(currentBus.getGoogleRating() == -1){
            GoogleRating_Text.setVisibility(View.GONE);
            GoogleRating_Image.setVisibility(View.GONE);
        }else{
            GoogleRating_Text.setText(Double.toString(currentBus.getGoogleRating()));
            GoogleRating_Image.setRating((float)currentBus.getGoogleRating());
        }

    }


    public void update(Business business){
        currentBus = business;


        if(currentBus == null){
        }else if(currentBus.getImageURL() == null || currentBus.getImageURL().equals("")){

            Picasso.get()
                    .load(R.drawable.noimage)
                    .into(BusinessImage, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            setViews();
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
                            setViews();
                            displayViews();
                        }

                        @Override
                        public void onError(Exception ex) {

                        }
                    });
        }


    }

}
