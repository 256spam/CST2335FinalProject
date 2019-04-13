package com.example.cst2335finalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Main newsfeed activity page where users make their selections
 * global variables are declared here for later use.
 */
public class NewsfeedActivity extends AppCompatActivity {

    ArrayList<NewsHolder> newsItems;
    ArrayList<NewsQuery> networkThreads;

    SharedPreferences sp;
    private EditText searchCriteria;
    private ProgressBar newsfeedProgBar;
    private String searchTerm;
    NewsFeedAdapter adapter;
    Toolbar toolbar;
    NewsfeedDatabaseOpener dbOpener;
    NewsfeedSavedDatabase savedDB;
    SQLiteDatabase db, dbs;
    int positionClicked;

    public NewsfeedActivity() {
        newsItems = new ArrayList<>();
        networkThreads = new ArrayList<>();
        searchTerm = null;
        positionClicked = 0;
    }

    /**
     * This function is where the newsfeed activity layout and toolbar are set.
     * Here a listview is populated with an adapter, buttons are given context as well as on click listeners
     * are set on buttons so event handlers can be managed.
     *
     * @param savedInstanceState
     */
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ListView newsfeedList = findViewById(R.id.newsfeedList);
        searchCriteria = findViewById(R.id.searchCriteria);
        Button searchButton = findViewById(R.id.searchButton);
        newsfeedProgBar = findViewById(R.id.newsfeedProgBar);

        sp = getSharedPreferences("ReserveName", Context.MODE_PRIVATE);
        String savedSearchTerm = sp.getString("ReserveName", "CST2335");
        searchCriteria.setText(savedSearchTerm);

        adapter = new NewsFeedAdapter();
        newsfeedList.setAdapter(adapter);

        //get a datbase
        dbOpener = new NewsfeedDatabaseOpener(this);
        savedDB = new NewsfeedSavedDatabase(this);

        db = dbOpener.getWritableDatabase();
        dbs = savedDB.getWritableDatabase();



        //query all the results from the database:
        String [] columns = {NewsfeedDatabaseOpener.COL_ID, NewsfeedDatabaseOpener.COL_Title, NewsfeedDatabaseOpener.COL_URL, NewsfeedDatabaseOpener.COL_TEXT};
        Cursor results = db.query(false, NewsfeedDatabaseOpener.TABLE_NAME, columns, null, null, null, null, null, null);


        //find the column indices:
        int titleColumnIndex = results.getColumnIndex(NewsfeedDatabaseOpener.COL_Title);
        int urlColumnIndex = results.getColumnIndex(NewsfeedDatabaseOpener.COL_URL);
        int textColumnIndex = results.getColumnIndex(NewsfeedDatabaseOpener.COL_TEXT);
        int idColumnIndex = results.getColumnIndex(NewsfeedDatabaseOpener.COL_ID);


        while(results.moveToNext())
        {
            String title = results.getString(titleColumnIndex);
            String url = results.getString(urlColumnIndex);
            String text = results.getString(textColumnIndex);
            long id = results.getLong(idColumnIndex);
            newsItems.add(new NewsHolder(title, url, text, id));
        }

        searchButton.setOnClickListener(c -> {

            newsfeedProgBar.setVisibility(View.VISIBLE);

            try {

                db.execSQL("delete from "+ "Newsfeed");

                newsItems.clear();

                networkThreads.add(new NewsQuery());

                Toast.makeText(NewsfeedActivity.this, "Results for " + searchCriteria.getText().toString(), Toast.LENGTH_LONG).show();

                searchTerm = searchCriteria.getText().toString();

                networkThreads.get(networkThreads.size() - 1).execute();

            } catch (Exception ex) {
                Log.e("Getting Results failed", ex.getMessage());
            }

        });


        newsfeedList.setOnItemClickListener((random, view, position, id) -> {

            positionClicked = position;

            Log.i("Item click", "Item " + position + " clicked.");
            Snackbar.make(random, "Good Choice!", Snackbar.LENGTH_LONG).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(NewsfeedActivity.this);
            @SuppressLint("InflateParams") View details = getLayoutInflater().inflate(R.layout.newsfeed_item_details, null);

            TextView newsItem = details.findViewById(R.id.dialogNewsItem);
            newsItem.setText(newsItems.get(position).getNewsItem());
            newsItem.setGravity(Gravity.CENTER);

            TextView newsSubItem = details.findViewById(R.id.dialogNewsSubItem);
            newsSubItem.setText("Article available here:  " + newsItems.get(position).getNewsSubItem());

            Button ok = details.findViewById(R.id.newsfeedDialogOkButton);

            Button cancel = details.findViewById(R.id.newsfeedDialogCancelButton);

            Button delete = details.findViewById(R.id.newsfeedDialogDeleteItem);

            Button save = details.findViewById(R.id.newsfeedDialogAddItem);

            builder.setView(details);
            AlertDialog dialog = builder.create();
            dialog.show();

            cancel.setOnClickListener(c->{dialog.hide();});

            ok.setOnClickListener(c->{

                NewsHolder chosenOne = newsItems.get(position);
                Intent nextPage = new Intent(NewsfeedActivity.this, NewsfeedReadArticle.class);
                nextPage.putExtra("Title", chosenOne.getNewsItem());
                nextPage.putExtra("Url", chosenOne.getNewsSubItem());
                nextPage.putExtra("Text", chosenOne.getNewsText());
                nextPage.putExtra("Id", id);

                startActivity(nextPage);

            });

            delete.setOnClickListener(c->{
                db.delete( NewsfeedDatabaseOpener.TABLE_NAME,  NewsfeedDatabaseOpener.COL_ID + "=?", new String[] {Long.toString(id)});
                newsItems.remove(position);
                adapter.notifyDataSetChanged();
                dialog.hide();

            });

            save.setOnClickListener(c->{

                //add to the database and get the new ID
                ContentValues newRowValues = new ContentValues();
                //put string title in the Title column:
                newRowValues.put(NewsfeedDatabaseOpener.COL_Title, newsItems.get(position).getNewsItem());
                //put string url in the URL column:
                newRowValues.put(NewsfeedDatabaseOpener.COL_URL, newsItems.get(position).getNewsSubItem());
                //put string text in the TEXT column:
                newRowValues.put(NewsfeedDatabaseOpener.COL_TEXT, newsItems.get(position).getNewsText());
                //insert in the database:
                long newId = dbs.insert(NewsfeedDatabaseOpener.TABLE_NAME, null, newRowValues);

                Toast.makeText(NewsfeedActivity.this, "Item Saved!", Toast.LENGTH_LONG).show();
                dialog.hide();
                });

        });

        newsfeedProgBar.setVisibility(View.INVISIBLE);

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
        inflater.inflate(R.menu.newsfeed_nav, menu);

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

            case R.id.dictionaryActivityMenuItem:
                nextPage[0] = new Intent(NewsfeedActivity.this, DictionaryActivity.class);
                startActivity(nextPage[0]);
                break;

            case R.id.newsfeedActivityMenuItem:
                nextPage[0] = new Intent(NewsfeedActivity.this, NewsfeedActivity.class);
                startActivity(nextPage[0]);
                break;

            case R.id.flightStatusTrackerMenuItem:
                nextPage[0] = new Intent(NewsfeedActivity.this, FlightTrackerActivity.class);
                startActivity(nextPage[0]);
                break;


            case R.id.newYorkTimesMenuItem:
                nextPage[0] = new Intent(NewsfeedActivity.this, NYTActivity.class);
                startActivity(nextPage[0]);
                break;


            case R.id.help:

                AlertDialog.Builder builder = new AlertDialog.Builder(NewsfeedActivity.this);
                @SuppressLint("InflateParams") View details = getLayoutInflater().inflate(R.layout.newsfeed_help_dialog, null);

                builder.setView(details);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.menu:

                AlertDialog.Builder menuBuilder = new AlertDialog.Builder(NewsfeedActivity.this);
                @SuppressLint("InflateParams") View menuDetails = getLayoutInflater().inflate(R.layout.newsfeed_popup_menu, null);

                Button mainMenu = menuDetails.findViewById(R.id.newsfeedPopUpGoToMainMenu);

                Button cancel = menuDetails.findViewById(R.id.newsfeedPopUpCancelButton);

                Button viewSavedArticles = menuDetails.findViewById(R.id.newsfeedPopUpGoToSavedArticles);

                Button visitUrl = menuDetails.findViewById(R.id.newsfeedPopUpVisitUrl);
                visitUrl.setText("Undecided");

                menuBuilder.setView(menuDetails);
                AlertDialog menuDialog = menuBuilder.create();
                menuDialog.show();

                mainMenu.setOnClickListener(c -> {

                    nextPage[0] = new Intent(NewsfeedActivity.this, MainActivity.class);
                    startActivity(nextPage[0]);

                });

                viewSavedArticles.setOnClickListener(c->{

                    nextPage[0] = new Intent(NewsfeedActivity.this, NewsfeedSavedArticles.class);
                    startActivity(nextPage[0]);


                });

                cancel.setOnClickListener(c->{
                    menuDialog.hide();
                });




                break;

        }
        return true;
    }

    /**
     * Creates an adapter that populates a list view with the desired view and information by getting data from the NewsHolder
     */
    protected class NewsFeedAdapter extends BaseAdapter {

        /**
         * Function that get's how many items there are that need to be populated. newsItems array list size
         * is returned as this is how many Items there are.
         *
         * @return
         */
        @Override
        public int getCount() {
            return newsItems.size();
        }

        /**
         * returns what newsItem object the function is at based on what position is passed into the function.
         *
         * @param position
         * @return
         */
        public NewsHolder getItem(int position) {
            return newsItems.get(position);
        }

        /**
         * create a view by setting the layout and populating it with data from the newsItem arrayList that holds NewsHolder objects.
         * The position variable is used to go through each element in the array list and set each row of the view.
         *
         * @param position
         * @param old
         * @param parent
         * @return
         */
        @SuppressLint({"SetTextI18n", "ViewHolder"})
        public View getView(int position, View old, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View view;

            NewsHolder thisRow = getItem(position);
            view = inflater.inflate(R.layout.newsfeed_layout, parent, false);

            TextView newsTitle = view.findViewById(R.id.newsFeedItem);
            TextView newsUrl = view.findViewById(R.id.newsFeedSubItem);

            newsTitle.setText(thisRow.getNewsItem());
            newsUrl.setText(thisRow.getNewsSubItem());

            return view;

        }

        public long getItemId(int position) {
            return getItem(position).getId();
        }
    }


    /**
     * Class that creates a 'news item' object that has item, sub item and id. These objectes will be called later from an array list
     * to populate the listview
     */
    private class NewsHolder {
        String newsItem;
        String newsSubItem;
        String newsText;
        protected long id;

        private NewsHolder(String newsItem, String newsSubItem, String newsText, long id) {
            setNewsItem(newsItem);
            setNewsSubItem(newsSubItem);
            setNewsText(newsText);
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

        private String getNewsText() {return newsText;}

        private void setNewsText(String newsText){this.newsText = newsText;}
        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }
    }


    /**
     * This AsyncTask is used to fetch data from a webpage and store in in local strings. This data will then be used to create a
     * NewsHolder object with the required data that was pulled from a webpage.
     */
    public class NewsQuery extends AsyncTask<String, Integer, String> {

        String title = null;
        String urlText = null;
        String language = "Other";
        String articleText = null;
        boolean isUsed = false;
        int index = 0;

        @Override
        protected String doInBackground(String... args) {

            //Get news data
            try {
                //create the network connection:
                URL url = new URL("http://webhose.io/filterWebContent?token=4a9ad714-8d42-4c9d-bd4f-eadf3694ed32&format=xml&sort=crawled&q=" + searchTerm);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inStream = urlConnection.getInputStream();

                //create a pull parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(inStream, "UTF-8");

                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = xpp.getName(); //get the name of the starting tag: <tagName>

                        if (tagName.equals("title")) {
                            title = xpp.nextText();
                            Log.d("AsyncTask", "Found parameter title: " + title);
                        }

                        if (tagName.equals("url")) {
                            urlText = xpp.nextText();
                            Log.d("AsyncTask", "Found parameter url: " + urlText);
                        }

                        if (tagName.equals("language")) {
                            language = xpp.nextText();
                            Log.d("AsyncTask", "Found parameter language: " + language);
                        }

                        if (tagName.equals("text")){
                            articleText = xpp.nextText();
                        }
                    }

                    if (title != null && title != "" && urlText != null && language.equals("english")) {

                        isUsed = false;

                        for (int i = 0; i < newsItems.size(); i++) {

                            if (newsItems.get(i).newsSubItem.equals(urlText) || (newsItems.get(i).newsItem.equals(title))) {
                                Log.d("AsyncTask", "Article Already found: " + urlText);
                                isUsed = true;
                                break;
                            }
                        }

                        if (isUsed == false) {

                            //add to the database and get the new ID
                            ContentValues newRowValues = new ContentValues();
                            //put string title in the Title column:
                            newRowValues.put(NewsfeedDatabaseOpener.COL_Title, title);
                            //put string url in the URL column:
                            newRowValues.put(NewsfeedDatabaseOpener.COL_URL, urlText);
                            //put string text in the TEXT column:
                            newRowValues.put(NewsfeedDatabaseOpener.COL_TEXT, articleText);
                            //insert in the database:
                            long newId = db.insert(NewsfeedDatabaseOpener.TABLE_NAME, null, newRowValues);

                            newsItems.add(new NewsHolder(title, urlText, articleText, newId));
                            index++;

                        }

                    }

                    xpp.next(); //advance to next XML event
                }

            } catch (Exception ex) {
                Log.e("Getting news failed", ex.getMessage());
            }

            return "Task complete";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(String s) {
            adapter.notifyDataSetChanged();
            newsfeedProgBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //get an editor object
        SharedPreferences.Editor editor = sp.edit();

        //save what was typed under the name "ReserveName"
        String textEntered = searchCriteria.getText().toString();
        editor.putString("ReserveName", textEntered);

        //write it to disk:
        editor.commit();
    }

}

