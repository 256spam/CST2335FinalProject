package com.example.cst2335finalproject.NYTArticle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import com.example.cst2335finalproject.R;
import android.content.Intent;
import android.widget.Button;


public class ArticleActivity2 extends AppCompatActivity {

    String inputPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_new_york_times);

        Intent previousPage = getIntent();
        inputPosition = previousPage.getStringExtra("inputPosition");

        System.out.println("im cool boy");
        System.out.println(inputPosition);

        WebView webView = findViewById(R.id.wvArticle1);
        webView.loadUrl(inputPosition);


    }


    }









