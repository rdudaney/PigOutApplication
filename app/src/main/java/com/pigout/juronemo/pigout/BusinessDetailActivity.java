package com.pigout.juronemo.pigout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BusinessDetailActivity extends AppCompatActivity {

    private Business currentBus;
    ArrayList<String> googlePhotoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_detail);

        Intent intent = getIntent();

        this.currentBus = (Business) intent.getSerializableExtra("CurrentBus");

        if(this.currentBus.getGooglePlaceDetailsBool() == false) {
            runTask newTask = new runTask();
            newTask.execute();
        }else{
            dispImageView();
        }


    }

    private void dispImageView(){
        googlePhotoList = currentBus.getGooglePhotoList();
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        ImagePagerAdapter adapter = new ImagePagerAdapter(BusinessDetailActivity.this, googlePhotoList);
        viewPager.setAdapter(adapter);

    }





    private class runTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }


        @Override
        protected Void doInBackground(Void... voids) {

//            https://maps.googleapis.com/maps/api/place/photo?photoreference=PHOTO_REFERENCE&sensor=false&maxheight=MAX_HEIGHT&maxwidth=MAX_WIDTH&key=YOUR_API_KEY
            GooglePlaceDetails googlePlaceDetails = new GooglePlaceDetails(APIKeys.getGoogleKey());
            googlePlaceDetails.getData(currentBus);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dispImageView();

        }

        @Override
        protected void onProgressUpdate(Void... values) {

            super.onProgressUpdate(values);

        }
    }

    public class ImagePagerAdapter extends PagerAdapter {
        private Context context;
        private ArrayList<String> imageURL;

        ImagePagerAdapter(Context context, ArrayList<String> imageUrls) {
            this.context = context;
            this.imageURL = imageUrls;
        }

        @Override
        public int getCount() {
            return imageURL.size();
        }

        @Override
        public boolean isViewFromObject(View view,Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(context);

            Picasso.get()
                    .load(imageURL.get(position))
                    .fit()
                    .centerCrop()
                    .into(imageView);
            container.addView(imageView);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

//    private class ImagePagerAdapter extends PagerAdapter {
//        private int[] mImages = new int[] {
//                R.drawable.stars_regular_0,
//                R.drawable.stars_regular_10,
//                R.drawable.stars_regular_15,
//                R.drawable.stars_regular_25
//        };
//
//        @Override
//        public int getCount() {
//            return mImages.length;
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return view == ((ImageView) object);
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            Context context = BusinessDetailActivity.this;
//            ImageView imageView = new ImageView(context);
//            int padding = context.getResources().getDimensionPixelSize(
//                    R.dimen.padding_medium);
//            imageView.setPadding(padding, padding, padding, padding);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            imageView.setImageResource(mImages[position]);
//            ((ViewPager) container).addView(imageView, 0);
//            return imageView;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            ((ViewPager) container).removeView((ImageView) object);
//        }
//    }
}
