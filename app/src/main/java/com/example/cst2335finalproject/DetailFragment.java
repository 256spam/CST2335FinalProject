package com.example.cst2335finalproject.NYTArticle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import com.example.cst2335finalproject.R;

public class DetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long db_id;
    private int id;
    private String webUrl;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        db_id = dataFromActivity.getLong("db_id" );
        id = dataFromActivity.getInt("id" );
        // Inflate the layout for this fragment
        View result =  inflater.inflate(com.example.cst2335finalproject.R.layout.activity_nyt_fragment, container, false);


        //show the article ID
        db_id = dataFromActivity.getLong(NytSavedArticle.ITEM_ID );
        TextView idView = (TextView)result.findViewById(com.example.cst2335finalproject.R.id.idText);
        idView.setText("Listview ID=" + id);


        //show saved article word
        TextView position = (TextView)result.findViewById(com.example.cst2335finalproject.R.id.message);
        idView.setText(dataFromActivity.getString(NytSavedArticle.ITEM_SELECTED));

//
        System.out.println("check here");
        System.out.println(dataFromActivity.getString(NytSavedArticle.ITEM_SELECTED));
        System.out.println("ends here");

        webUrl = dataFromActivity.getString(NytSavedArticle.ITEM_SELECTED);

        WebView webView = result.findViewById(com.example.cst2335finalproject.R.id.wvArticle1);
        webView.loadUrl(webUrl);


        Button goBack = result.findViewById(com.example.cst2335finalproject.R.id.open_activity2);
        goBack.setOnClickListener( clk -> {

            if(isTablet) {
            NytSavedArticle parent = (NytSavedArticle)getActivity();
            parent.finish(); //go back
            }
            else {
                EmptyActivity parent = (EmptyActivity) getActivity();
                parent.finish(); //go back
            }


        });

        // get the delete button, and add a click listener:
        Button deleteButton = (Button)result.findViewById(com.example.cst2335finalproject.R.id.deleteButton);
        deleteButton.setOnClickListener( clk -> {


            if(isTablet) { //both the list and details are on the screen:
                NytSavedArticle parent = (NytSavedArticle)getActivity();
                parent.deleteMessageId((int)db_id); //this deletes the item and updates the list
                //now remove the fragment since you deleted it from the database:
                // this is the object to be removed, so remove(this):
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
                parent.finish(); //go back
            }
            //for Phone:
            else {
                EmptyActivity parent = (EmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra("db_id", dataFromActivity.getLong("db_id"));
                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }

        });
        return result;


}


}