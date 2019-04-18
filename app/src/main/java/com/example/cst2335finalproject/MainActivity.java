package com.example.cst2335finalproject.NYTArticle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;

import com.example.cst2335finalproject.dictionaryActivity.DictionaryActivity;
import com.example.cst2335finalproject.flightstatusActivity.FlightActivity;
import com.example.cst2335finalproject.NYTArticle.MainActivityNewYorkTimes;
import com.example.cst2335finalproject.newsfeedActivity.NewsFeedActivity;

public class MainActivity extends AppCompatActivity {

    Toolbar tBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.cst2335finalproject.R.layout.activity_main);

        tBar = (Toolbar)findViewById(com.example.cst2335finalproject.R.id.toolbar);
        setSupportActionBar(tBar);

        Button dicBtn = findViewById(com.example.cst2335finalproject.R.id.enter_dic);
        Button fstBtn = findViewById(com.example.cst2335finalproject.R.id.enter_fst);
        Button nfBtn = findViewById(com.example.cst2335finalproject.R.id.enter_nf);
        Button nytBtn = findViewById(com.example.cst2335finalproject.R.id.enter_nyt);
       // ImageButton nytMenuBtn = findViewById(R.id.nyt_menu_KN);


        // when user click on enter dictionary button
        dicBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivity.this, DictionaryActivity.class);
            startActivity(nextPage);
        });
        // when user click on enter flight status tracker button
        fstBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivity.this, FlightActivity.class);
            startActivity(nextPage);
        });
        // when user click on enter news feed button
        nfBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivity.this, NewsFeedActivity.class);
            startActivity(nextPage);
        });
        // when user click on enter new york times article button
        nytBtn.setOnClickListener(c->{
            Intent nextPage = new Intent(MainActivity.this, MainActivityNewYorkTimes.class);
            startActivity(nextPage);
        });
       // nytMenuBtn.setOnClickListener(c->{
//            Intent nextPage = new Intent(MainActivity.this, MainActivityNewYorkTimes.class);
//            startActivity(nextPage);
//        });
    }

    /**
     * inflate the icons for toolbar
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(com.example.cst2335finalproject.R.menu.toolbar_menu, menu);
        return true;
    }

    /**
     * when click on the icons
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent nextPage = null;
        switch(item.getItemId())
        {
            //when click on "dictionary":
            case R.id.go_dic:
                nextPage = new Intent(MainActivity.this, DictionaryActivity.class);
                startActivity(nextPage);
                break;
            // when click on "flight tracker":
            case R.id.go_flight:
                nextPage = new Intent(MainActivity.this, FlightActivity.class);
                startActivity(nextPage);
                break;
            // when click on "news feed":
            case R.id.go_news_feed:
                nextPage = new Intent(MainActivity.this, NewsFeedActivity.class);
                startActivity(nextPage);
                break;
            // when click on "new york times":
            case R.id.go_new_york:
                nextPage = new Intent(MainActivity.this, MainActivityNewYorkTimes.class);
                startActivity(nextPage);
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
