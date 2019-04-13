package com.example.cst2335finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class NewsfeedEmptyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_empty_activity);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample

        NewsfeedReadArticleDetailFragment dFragment = new  NewsfeedReadArticleDetailFragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}