package com.example.cst2335finalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;


/**
 * Class that takes extra from main newsfeed activity or saved articles and uses it to create a new page that show all of the articles text.
 */
public class NewsfeedReadArticle extends AppCompatActivity {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;
    ListView newsText;
    CustomAdapter adapter;
    String text;
    Toolbar toolbar;
    String newsTitle, newsUrl;

    SQLiteDatabase dbs;
    NewsfeedSavedDatabase savedDB;

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;


    /**
     * Create page layout
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_read_article);

        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        TextView title = findViewById(R.id.newsfeedReadArticleTitle);
        TextView url = findViewById(R.id.newsfeedReadArticleUrl);
        newsText = findViewById(R.id.newsfeedReadArticleText);
        Button more = findViewById(R.id.newsfeedReadArticleMoreButton);
        toolbar = findViewById(R.id.newsfeedMenuBar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Menu");

        Intent fromPreviousPage = getIntent();
        newsTitle = fromPreviousPage.getStringExtra("Title");
        newsUrl = fromPreviousPage.getStringExtra("Url");
        text = fromPreviousPage.getStringExtra("Text");
        title.setText(newsTitle);
        url.setText("");

        savedDB = new NewsfeedSavedDatabase(this);
        dbs = savedDB.getWritableDatabase();

        adapter = new CustomAdapter();
        newsText.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        more.setOnClickListener(c->{

            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, newsTitle );
            dataToPass.putLong(ITEM_ID, id);

            if(isTablet)
            {
               NewsfeedReadArticleDetailFragment dFragment = new  NewsfeedReadArticleDetailFragment();//add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(NewsfeedReadArticle.this, NewsfeedEmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }

        });


    }

    /**
     * Custom adapter for listview. It populates it only with one String which is the text from the article.
     */
    protected class CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return 1;
        }

        public Object getItem(int position){
            return "Show this in row "+ position;
        }

        public View getView(int position, View old, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            View newView = inflater.inflate(R.layout.newsfeed_layout, parent, false );


            TextView rowText = newView.findViewById(R.id.newsFeedItem);
            TextView subRowText = newView.findViewById(R.id.newsFeedSubItem);
            rowText.setText( text );
            subRowText.setText(newsUrl);

            return newView;
        }

        public long getItemId(int position)
        {
            return position;
        }
    }


    /**
     * Inflate the menu items for use in the action bar
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.newsfeed_menu, menu);

        return true;
    }

    /**
     * Event handler for when a menu item is clicked.
     *
     * @param item
     * @return
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final Intent[] nextPage = new Intent[1];

        switch (item.getItemId()) {

            case R.id.newsfeedReadArticleHelp:
                nextPage[0] = new Intent(NewsfeedReadArticle.this, DictionaryActivity.class);
                startActivity(nextPage[0]);
                break;


            case R.id.newsfeedReadArticleMenu:

                AlertDialog.Builder builder = new AlertDialog.Builder(NewsfeedReadArticle.this);
                @SuppressLint("InflateParams") View details = getLayoutInflater().inflate(R.layout.newsfeed_popup_menu, null);

                Button mainMenu = details.findViewById(R.id.newsfeedPopUpGoToMainMenu);

                Button cancel = details.findViewById(R.id.newsfeedPopUpCancelButton);

                Button viewSavedArticles = details.findViewById(R.id.newsfeedPopUpGoToSavedArticles);

                Button visitUrl = details.findViewById(R.id.newsfeedPopUpVisitUrl);

                builder.setView(details);
                AlertDialog dialog = builder.create();
                dialog.show();

                mainMenu.setOnClickListener(c -> {

                    nextPage[0] = new Intent(NewsfeedReadArticle.this, MainActivity.class);
                    startActivity(nextPage[0]);

                });

                viewSavedArticles.setOnClickListener(c->{

                    nextPage[0] = new Intent(NewsfeedReadArticle.this, NewsfeedSavedArticles.class);
                    startActivity(nextPage[0]);


                });

                cancel.setOnClickListener(c->{
                    dialog.hide();
                });

                visitUrl.setOnClickListener(c->{
                    String url = newsUrl;
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                });


                break;


        }
        return true;
    }

}
