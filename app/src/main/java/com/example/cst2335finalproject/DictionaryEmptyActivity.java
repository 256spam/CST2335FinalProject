package com.example.cst2335finalproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class DictionaryEmptyActivity extends AppCompatActivity {

    /**
     * all code created via the use of inclass examples, lab work and official documentation from developer.android.com
     *
     * load passed data
     * pass data into fragment
     * tell fragment it is on phone
     * @param savedInstanceState bundle of data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dict_empty);
        Bundle dataToPass = getIntent().getExtras();
        DictionaryFragment dFragment = new DictionaryFragment();
        dFragment.setArguments( dataToPass );
        dFragment.setTablet(false);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentLocation, dFragment)
                .addToBackStack("AnyName")
                .commit();
    }
}
