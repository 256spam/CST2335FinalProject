package com.example.cst2335finalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import java.util.ArrayList;


public class NewsfeedActivity extends AppCompatActivity {

    ArrayList<NewsHolder> newsItems;
    private EditText searchCriteria;
    private ProgressBar newsfeedProgBar;

    public NewsfeedActivity() {
        newsItems = new ArrayList<>();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        ListView newsfeedList = findViewById(R.id.newsfeedList);
        searchCriteria = findViewById(R.id.searchCriteria);
        Button searchButton = findViewById(R.id.searchButton);
        newsfeedProgBar = findViewById(R.id.newsfeedProgBar);

        NewsFeedAdapter adapter = new NewsFeedAdapter();
        newsfeedList.setAdapter(adapter);


        newsfeedProgBar.setVisibility(View.VISIBLE);

        searchButton.setOnClickListener(c -> {

            try{

            Toast.makeText(NewsfeedActivity.this, "Results for " + searchCriteria.getText().toString(), Toast.LENGTH_LONG).show();

            //temp filler list filler
            newsItems.clear();
            for(int i=0; i < 20; i++){

                newsItems.add(new NewsHolder("News item", searchCriteria.getText().toString(), i));
            }
                //simulate 1.5 second lag from searching
                Thread.sleep(1500);
                newsfeedProgBar.setVisibility(View.INVISIBLE);

                adapter.notifyDataSetChanged();
            }catch(Exception ex)
            {
                Log.e("Getting Bitmap failed", ex.getMessage() );
            }

        });

        newsfeedList.setOnItemClickListener((newsFeedList, view, position, id) -> {
            Log.i("Item click" , "Item "+ position + " clicked.");
            Snackbar.make(newsFeedList, "Good Choice!", Snackbar.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(NewsfeedActivity.this);
            @SuppressLint("InflateParams") View details = getLayoutInflater().inflate(R.layout.dialog_news_item_detail, null);


            TextView newsItem = details.findViewById(R.id.dialogNewsItem);
            newsItem.setText("You clicked on " + newsItems.get(position).getNewsItem() + " " + newsItems.get(position).getId());

            TextView newsSubItem = details.findViewById(R.id.dialogNewsSubItem);
            newsSubItem.setText("Here is " + newsItems.get(position).getNewsSubItem() + " " + newsItems.get(position).getId());

            builder.setView(details);

            AlertDialog dialog = builder.create();
            dialog.show();

        });
    }

    protected class NewsFeedAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsItems.size();
        }

        public NewsHolder getItem(int position) {
            return newsItems.get(position);
        }

        @SuppressLint({"SetTextI18n", "ViewHolder"})
        public View getView(int position, View old, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View view;

            NewsHolder thisRow = getItem(position);
            view = inflater.inflate(R.layout.news_feed_layout, parent, false);

            TextView message = view.findViewById(R.id.newsFeedItem);
            TextView messageType = view.findViewById(R.id.newsFeedSubItem);

            message.setText(thisRow.getNewsItem() + " " + position);
            messageType.setText(thisRow.getNewsSubItem());

            return view;

        }

        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }


    public class NewsHolder {
        String newsItem;
        String newsSubItem;
        protected long id;

        private NewsHolder(String newsItem, String newsSubItem, long id) {
            setNewsItem(newsItem);
            setNewsSubItem(newsSubItem);
            setId(id);
        }

        private String getNewsItem() {
            return newsItem;
        }

        private void setNewsItem(String newsItem) {
            this.newsItem = newsItem;
        }

        private String getNewsSubItem() {
            return newsSubItem;
        }

        private void setNewsSubItem(String newsSubItem) {
            this.newsSubItem = newsSubItem;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }
}