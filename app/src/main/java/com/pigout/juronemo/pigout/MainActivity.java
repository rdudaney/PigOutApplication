package com.pigout.juronemo.pigout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void start_Click(View view){
        Intent goSecondView = new Intent(this, SecondActivity.class);
        startActivity(goSecondView);
    }
}
