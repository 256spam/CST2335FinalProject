package com.example.cst2335finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * Create view for fragment
 */
public class NewsfeedReadArticleDetailFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;

    public void setTablet(boolean tablet) { isTablet = tablet; }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(NewsfeedReadArticle.ITEM_ID );

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.newsfeed_read_article_detail_fragment, container, false);

        //show the message
        TextView message = result.findViewById(R.id.message);
        message.setText(dataFromActivity.getString(NewsfeedReadArticle.ITEM_SELECTED));

        //show the id:
        TextView idView = result.findViewById(R.id.idText);
        idView.setText("ID=" + id);

        return result;
    }
}
