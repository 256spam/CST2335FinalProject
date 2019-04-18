/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * Description: shows the list of searched articles
 * **/

package com.example.cst2335finalproject.NYTArticle;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.example.cst2335finalproject.R;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NytSavedArticle extends AppCompatActivity {

    //2 fields are for database

    //4 fields are for fragment using
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;

    NytDataBaseHelper dbInitiate;
    SQLiteDatabase db;
    //toolbar
    Toolbar tBar;
   //article list
    ArrayList<Article> savedArticleList;
    //saved article word
    SavedWordsAdapter adt;
    //selected article
    Article selectArticle;

    /**
     *  this method runs when click on "view saved word list"
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(com.example.cst2335finalproject.R.layout.activity_nyt_saved_articles);
        tBar = (Toolbar)findViewById(com.example.cst2335finalproject.R.id.toolbar);

        setSupportActionBar(tBar);


        // get savedWordList array list for first time running
        if (savedArticleList == null) {
            savedArticleList = new ArrayList<>();
        }

        //get a database:
        dbInitiate = new NytDataBaseHelper(this);
        db = dbInitiate.getWritableDatabase();

        // find all data and put them into message list
        this.findAllData(db);

        // get the "ListView" object
        ListView theList = findViewById(com.example.cst2335finalproject.R.id.saved_article_list);
        // get fragment
        boolean isTablet = findViewById(R.id.frameLayout) != null; //check if the FrameLayout is loaded
        // initial the adapter with article history list
        adt = new SavedWordsAdapter(savedArticleList);
        // the list should show up now
        theList.setAdapter(adt);

//        System.out.println("your here" + savedArticleList.get(0).getArticleID());
//        System.out.println("your here" + savedArticleList.get(0).getOrganization());
//        System.out.println("your here" + savedArticleList.get(0).getTitle());



            theList.setOnItemClickListener( (list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, savedArticleList.get(position).getTitle() );
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);


            if(isTablet)
            {
                DetailFragment dFragment = new DetailFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frameLayout, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(NytSavedArticle.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EMPTY_ACTIVITY && resultCode == RESULT_OK) {
            // for phone, check if deleted a message
            long deletedId = data.getLongExtra("deletedId", 0);
            // delete from database
            // dbOpener.deleteRow(db, deletedId);
            deleteMessageId((int)deletedId);
        }
    }

    /**
     *  delete the message from view list by it's id, and update the list
     * @param id
     */

    public void deleteMessageId(int id)
    {
        Log.i("Delete this message:" , " id="+id);
        String str="";
        Cursor c;
        String [] cols = {NytDataBaseHelper.COL_ID, NytDataBaseHelper.COL_Article};
        c = db.query(false, NytDataBaseHelper.TABLE_NAME, cols, null, null, null, null, null, null);
        if(c.moveToFirst()) {
            for (int i =0; i<id; i++) {
                c.moveToNext();
            }
            str = c.getString(c.getColumnIndex(NytDataBaseHelper.COL_ID));
        }
        int x = db.delete(NytDataBaseHelper.TABLE_NAME, NytDataBaseHelper.COL_ID+"=?", new String[] {str});
        Log.i("ViewContact", "Deleted " + x + " rows");
        savedArticleList.remove(id);
        adt.notifyDataSetChanged();
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
        inflater.inflate(R.menu.toolbar_menu_dictionary_rl, menu);
        return true;
    }

    /**
     * when click on the icons
     * @param item
     * @return
     */


    /**
     * to find all the data, and put them into message list
     */
    private void findAllData(SQLiteDatabase db){
        Log.e("you ", " are looking for all the data");
        //query all the results from the database:
        String [] columns = {NytDataBaseHelper.COL_ID, NytDataBaseHelper.COL_Article};
        Cursor results = db.query(false, NytDataBaseHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        //find the column indices:
        int idColIndex = results.getColumnIndex(NytDataBaseHelper.COL_ID);
        int contentColumnIndex = results.getColumnIndex(NytDataBaseHelper.COL_Article);
//        int urlColumnIndex = results.getColumnIndex(NytDataBaseHelper.COL_Url);
        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String id = results.getString(idColIndex);
            String content = results.getString(contentColumnIndex);
            //add the new Contact to the array list:
            String content1 = results.getString(contentColumnIndex);
            //add the new Contact to the array list:
            savedArticleList.add(new Article(content,content1,id));

        }
    }

    //This class needs 4 functions to work properly:
    protected class SavedWordsAdapter<E> extends BaseAdapter {
        private List<E> dataCopy = null;
        //Keep a reference to the data:
        public SavedWordsAdapter(List<E> originalData) {
            dataCopy = originalData;
        }
        public SavedWordsAdapter(E[] array) {
            dataCopy = Arrays.asList(array);
        }
        public SavedWordsAdapter() {
        }
        // return  the size
        @Override
        public int getCount() {
            return dataCopy.size();
        }
        // return the contents that shows up
        @Override
        public E getItem(int position) {
            return dataCopy.get(position);
        }


        /***
         *  this method will add to the list view
         *   @param position: locate the pos and add to the bott
         *   @return the new view
         **/
        @Override
        public View getView(int position, View old, ViewGroup parent) {
            View newView = null;
            LayoutInflater inflater = getLayoutInflater();
            selectArticle = (Article)getItem(position);
            newView = inflater.inflate(com.example.cst2335finalproject.R.layout.activity_nyt_article_list, parent, false);
            TextView idView = (TextView) newView.findViewById(com.example.cst2335finalproject.R.id.article_id);
            TextView contentView = (TextView) newView.findViewById(com.example.cst2335finalproject.R.id.a_article);

            //Get the string to go in row: position
            String id = ((Article) getItem(position)).getTitle();
//            String content = ((Article) getItem(position)).getTitle();
            //Set the text of the text view
            idView.setText("    " + id);
//            contentView.setText(content);
            return newView;
        }

        // gets the Item id to add to the position
        @Override
        public long getItemId(int position) {
            return (long)position;
        }
    }
}