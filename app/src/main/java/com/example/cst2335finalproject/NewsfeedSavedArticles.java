package com.example.cst2335finalproject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;
import java.util.ArrayList;


/**
 * Class that allows user to see their saved articles. It also will let them delete or add an item if they
 * want to.
 */
public class NewsfeedSavedArticles extends AppCompatActivity {

    SavedArticlesAdapter adapter;
    NewsfeedSavedDatabase dbOpener;
    SQLiteDatabase db;
    ArrayList<NewsHolder> savedNewsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsfeed_saved_articles);

        ListView savedNewsArticles = findViewById(R.id.newsfeedSavedArticlesList);
        Button add = findViewById(R.id.newsfeedSavedArticlesAddButton);

        savedNewsItems = new ArrayList<>();

        //get a datbase
        dbOpener = new NewsfeedSavedDatabase(this);
        db = dbOpener.getWritableDatabase();

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
            savedNewsItems.add(new NewsHolder(title, url, text, id));
        }

        adapter = new SavedArticlesAdapter();
        savedNewsArticles.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        savedNewsArticles.setOnItemClickListener((random, view, position, id) -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(NewsfeedSavedArticles.this);
            @SuppressLint("InflateParams") View details = getLayoutInflater().inflate(R.layout.newsfeed_popup_menu, null);

            builder.setView(details);
            AlertDialog dialog = builder.create();
            dialog.show();

            Button visitPage = details.findViewById(R.id.newsfeedPopUpVisitUrl);
            visitPage.setOnClickListener(c->{
                String url = savedNewsItems.get(position).getNewsSubItem();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            });

            Button readArticle = details.findViewById(R.id.newsfeedPopUpGoToMainMenu);
                readArticle.setText("Read Article");
                readArticle.setOnClickListener(c->{
                final Intent[] nextPage = new Intent[1];
                nextPage[0] = new Intent(NewsfeedSavedArticles.this, NewsfeedReadArticle.class);
                    NewsHolder chosenOne = savedNewsItems.get(position);
                    nextPage[0].putExtra("Title", chosenOne.getNewsItem());
                    nextPage[0].putExtra("Url", chosenOne.getNewsSubItem());
                    nextPage[0].putExtra("Text", chosenOne.getNewsText());
                    nextPage[0].putExtra("Id", id);
                startActivity(nextPage[0]);
            });

            Button delete = details.findViewById(R.id.newsfeedPopUpGoToSavedArticles);
            delete.setText("Delete");
            delete.setOnClickListener(c->{
                db.delete( NewsfeedSavedDatabase.TABLE_NAME,  NewsfeedDatabaseOpener.COL_ID + "=?", new String[] {Long.toString(id)});
                savedNewsItems.remove(position);
                adapter.notifyDataSetChanged();
                dialog.hide();
                Toast.makeText(NewsfeedSavedArticles.this, "Item Deleted!", Toast.LENGTH_LONG).show();

            });

            Button cancel = details.findViewById(R.id.newsfeedPopUpCancelButton);
            cancel.setOnClickListener(c->{
                dialog.hide();
            });

        });


        add.setOnClickListener(c->{

            AlertDialog.Builder builder = new AlertDialog.Builder(NewsfeedSavedArticles.this);
            @SuppressLint("InflateParams") View details = getLayoutInflater().inflate(R.layout.newsfeed_add_item_layout, null);

            EditText title = details.findViewById(R.id.newsfeedAddTitleEditText);
            EditText url = details.findViewById(R.id.newsfeedAddURLEditText);
            EditText text = details.findViewById(R.id.newsfeedAddTextEditText);
            Button addItem = details.findViewById(R.id.newsfeedAddButtonADD);
            Button cancel = details.findViewById(R.id.newsfeedAddButtonCANCEL);

            builder.setView(details);
            AlertDialog dialog = builder.create();
            dialog.show();

            addItem.setOnClickListener(b->{

                    //add to the database and get the new ID
                    ContentValues newRowValues = new ContentValues();
                    //put string title in the Title column:
                    newRowValues.put(NewsfeedDatabaseOpener.COL_Title, title.getText().toString());
                    //put string url in the URL column:
                    newRowValues.put(NewsfeedDatabaseOpener.COL_URL, url.getText().toString());
                    //put string text in the TEXT column:
                    newRowValues.put(NewsfeedDatabaseOpener.COL_TEXT, text.getText().toString());
                    //insert in the database:
                    long newId = db.insert(NewsfeedDatabaseOpener.TABLE_NAME, null, newRowValues);

                    savedNewsItems.add(new NewsHolder(title.getText().toString(), url.getText().toString(),text.getText().toString(), savedNewsItems.size()));

                    adapter.notifyDataSetChanged();
                    dialog.hide();


            });

            cancel.setOnClickListener(b->{
                dialog.hide();
            });

        });

    }

    /**
     * Class that holds all the saved news items and is populated when the database querey is ran or if
     * the user adds a custom news item.
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
     * Creates an adapter that populates a list view with the desired view and information by getting data from the NewsHolder
     */
    protected class SavedArticlesAdapter extends BaseAdapter {

        /**
         * Function that get's how many items there are that need to be populated. newsItems array list size
         * is returned as this is how many Items there are.
         *
         * @return
         */
        @Override
        public int getCount() {
            return savedNewsItems.size();
        }

        /**
         * returns what newsItem object the function is at based on what position is passed into the function.
         *
         * @param position
         * @return
         */
        public NewsHolder getItem(int position) {
            return savedNewsItems.get(position);
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

}
