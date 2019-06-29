package com.pigout.juronemo.pigout;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessFragment extends Fragment {

    Business currentBus;
    private TextView BusinessName_Text;
    private ImageView BusinessImage;
    private ProgressBar progressBar;


    public BusinessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_business, container, false);

        progressBar = view.findViewById(R.id.progBar);
        BusinessName_Text = view.findViewById(R.id.businessName);
        BusinessImage = view.findViewById(R.id.businessImage);


        progressBar.setVisibility(View.VISIBLE);
        BusinessName_Text.setVisibility(View.GONE);

        return view;
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
                        }

                        @Override
                        public void onError(Exception ex) {

                        }
                    });
        }





        progressBar.setVisibility(View.GONE);

        BusinessName_Text.setText(business.getName());

        BusinessName_Text.setVisibility(View.VISIBLE);


    }

}
