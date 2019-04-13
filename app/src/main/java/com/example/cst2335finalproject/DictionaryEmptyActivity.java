package com.example.cst2335finalproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DictionaryEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict_empty);

        Log.e("DEBUG","loaded empty activity");
        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample
        Log.e("DEBUG","loaded data bundle in activity");
        //This is copied directly from FragmentExample.java lines 47-54
        DictionaryFragment dFragment = new DictionaryFragment();
        dFragment.setArguments( dataToPass ); //pass data to the the fragment
        Log.e("DEBUG","loaded data bundle in fragment");
        dFragment.setTablet(false); //tell the Fragment that it's on a phone.
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
