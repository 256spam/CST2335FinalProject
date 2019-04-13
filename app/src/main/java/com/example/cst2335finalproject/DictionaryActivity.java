package com.example.cst2335finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DictionaryActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText searchFeild;
    Button searchButton;
    Button toggleMode;
    Button helpButton;
    ProgressBar progress;
    ListView definitionsList;
    DatabaseOpener dataOpener;
    SQLiteDatabase db;
    ArrayList<DictionaryDefinition> definitions;
    DictionaryAdapter adapter;
    boolean currentMode;

    final static String TABLE_NAME = "SavedDefinitions";
    final static String COL_TITLE = "TITLE";
    final static String COL_WORDCLASS = "WORDCLASS";
    final static String COL_DEFINITION = "DEFINITION";

    /**
     * assign layout to activity
     * assign all layout elements to respective variables
     * prepare the share preferences editor
     * configure helpbutton to display help menu when pressed
     * configure toggle button to switch the boolean state and then launch the method to update the list based on mode
     * configure search button to get value from the edittext, then execute a query with the search term injected into the url
     * create and configure adapter for definition list
     * set the click listener for the definitionlist to store data of the clicked item into a bundle and pass it to fragment, depending on if a tablet is in use, or a phone
     * load the curent mode for the dictionary list from shared preferences
     * load the last search from shared preferences into the edittext
     * if the current mode is to view the local storage, load the list from the database
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        toolbar = findViewById(R.id.DictToolbar);
        searchFeild = findViewById(R.id.DictSearchFeild);
        searchButton = findViewById(R.id.DictSearchButton);
        toggleMode = findViewById(R.id.DictToggleButton);
        helpButton = findViewById(R.id.DictHelpButton);
        progress = findViewById(R.id.DictProgressBar);
        definitionsList = findViewById(R.id.DictListView);
        definitions = new ArrayList<>();
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        setSupportActionBar(toolbar);
        dataOpener = new DatabaseOpener(this);
        db = dataOpener.getWriteableDatabase();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();


        helpButton.setOnClickListener( c -> {
            AlertDialog.Builder alert = createHelpMenu();
            alert.show();
        });

        toggleMode.setOnClickListener( c -> {
            if(currentMode == false) {
                currentMode = true;
            }else{
                currentMode = false;
            }
            editor.putBoolean("DictState", currentMode);
            editor.commit();
            toggleMode();
        });

        searchButton.setOnClickListener( c -> {
            progress.setVisibility(View.VISIBLE);
            if (searchFeild.getText() != null) {
                editor.putString("DictSearch", String.valueOf(searchFeild.getText()));
                editor.commit();
                definitions.clear();
                DictionaryQuery query = new DictionaryQuery();
                query.execute("https://www.dictionaryapi.com/api/v1/references/sd3/xml/" + searchFeild.getText() + "?key=4556541c-b8ed-4674-9620-b6cba447184f");
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new DictionaryAdapter(getApplicationContext(), R.layout.dict_listview_layout,definitions);
        definitionsList.setAdapter(adapter);

        definitionsList.setOnItemClickListener( (list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putLong("ID",definitions.get(position).getID());
            dataToPass.putString("Title",definitions.get(position).getDefinitonTitle());
            dataToPass.putString("WordClass",definitions.get(position).getWordClass());
            dataToPass.putString("Definition",definitions.get(position).getDefinition());
            Log.e("DEBUG","completed data bundle");

            if(isTablet)
            {
                Log.e("DEBUG","is tablet");
                DictionaryFragment dFragment = new DictionaryFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                Log.e("DEBUG","loaded data bundle");
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .addToBackStack("AnyName") //make the back button undo the transaction
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Log.e("DEBUG","isnt tablet");
                Intent nextActivity = new Intent(DictionaryActivity.this, DictionaryEmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                Log.e("DEBUG","loaded data bundle");
                startActivityForResult(nextActivity, 345); //make the transition
            }
        });

        currentMode = pref.getBoolean("DictState", false);
        toggleMode();

        searchFeild.setText(pref.getString("DictSearch", null));
        if (searchFeild.getText() != null){
            definitions.clear();
            DictionaryQuery query = new DictionaryQuery();
            query.execute("https://www.dictionaryapi.com/api/v1/references/sd3/xml/" + searchFeild.getText() + "?key=4556541c-b8ed-4674-9620-b6cba447184f");
            adapter.notifyDataSetChanged();
        }

        if (currentMode == true) {
            Cursor csr = db.rawQuery("SELECT * from " + TABLE_NAME, null);
            csr.moveToFirst();
            for (int i = 0; i < csr.getCount(); i++) {
                long idnum = csr.getLong(csr.getColumnIndex("id"));
                String title = csr.getString(csr.getColumnIndex(COL_TITLE));
                String wClass = csr.getString(csr.getColumnIndex(COL_WORDCLASS));
                String definition = csr.getString(csr.getColumnIndex(COL_DEFINITION));
                addToDefinitions(idnum, title, wClass, definition);
                csr.moveToNext();
            }
        }
    }


    /**
     * create builder
     * set title of alert and message
     * add close button to alert
     * @return the completed alert dialog help menu.
     */
    public AlertDialog.Builder createHelpMenu(){
        AlertDialog.Builder adb = new AlertDialog.Builder(DictionaryActivity.this);
        adb.setTitle(R.string.dictHelpMenuTitle);
        adb.setMessage(R.string.dictHelpMenuText);
        adb.setNegativeButton(R.string.dictCloseOption, null);
        return adb;
    }

    /**
     * create a new dictionaryDefinition object with the given variables and then store it in the definition list
     * @param id id for the object
     * @param title title for the definition
     * @param longDef definition for the definition
     * @param wordClass the wordclass for the definition
     */
    void addToDefinitions(Long id, String title, String longDef, String wordClass){
        DictionaryDefinition definition = new DictionaryDefinition(id,title,longDef,wordClass);
        definitions.add(definition);
    }

    /**
     * Check current state of the definition list when toggled
     * create popup, disable or enable search depending on state.
     * purge current list. If in search mode, query current search feild .
     */
    public void toggleMode(){
        if(currentMode == true) {
            Toast toast1 = Toast.makeText(getApplicationContext(), "Loading local storage", Toast.LENGTH_SHORT);
            toast1.show();
            searchButton.setEnabled(false);
            definitions.clear();
            adapter.notifyDataSetChanged();
            Cursor csr = db.rawQuery("SELECT * from " + TABLE_NAME, null);
            csr.moveToFirst();
            for(int i = 0; i < csr.getCount(); i++){
                long idnum = csr.getLong(csr.getColumnIndex( "id"));
                String title = csr.getString(csr.getColumnIndex( COL_TITLE ));
                String wClass = csr.getString(csr.getColumnIndex( COL_WORDCLASS ));
                String definition = csr.getString(csr.getColumnIndex( COL_DEFINITION ));
                addToDefinitions(idnum, title, definition, wClass);
                csr.moveToNext();
            }
            adapter.notifyDataSetChanged();
        }else if (currentMode == false){
            Toast toast1 = Toast.makeText(getApplicationContext(), "Loading live search", Toast.LENGTH_SHORT);
            toast1.show();
            searchButton.setEnabled(true);
            definitions.clear();
            adapter.notifyDataSetChanged();
            DictionaryQuery query = new DictionaryQuery();
            query.execute("https://www.dictionaryapi.com/api/v1/references/sd3/xml/" + searchFeild.getText() + "?key=4556541c-b8ed-4674-9620-b6cba447184f");
            adapter.notifyDataSetChanged();
        }else{
            Toast toast1 = Toast.makeText(getApplicationContext(), "An error has occurred. Please Try again later", Toast.LENGTH_SHORT);
            toast1.show();
        }
    }


    /**
     * Inflate the main_menu_nav menu
     * @param menu Menu with which to inflate into
     * @return true when successfully completed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu_nav, menu);
        return true;
    }

    /**
     * Controls switching between the 4 sections of the program
     * @param item the selected menu item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent nextPage;

        switch(item.getItemId()){

            case R.id.dictionaryActivityMenuItem:
                nextPage = new Intent(DictionaryActivity.this, MainActivity.class);
                startActivity(nextPage);
                break;

            case R.id.newsfeedActivityMenuItem:
                nextPage = new Intent(DictionaryActivity.this, NewsfeedActivity.class);
                startActivity(nextPage);
                break;

            case R.id.flightStatusTrackerMenuItem:
                nextPage = new Intent(DictionaryActivity.this, FlightTrackerActivity.class);
                startActivity(nextPage);
                break;


            case R.id.newYorkTimesMenuItem:
                nextPage = new Intent(DictionaryActivity.this, NYTActivity.class);
                startActivity(nextPage);
                break;

        }
        return true;
    }

    /**
     * if the result of an activity is OK
     * load the id from data
     * if the final option was to delete, initate the delete sequence
     * if the option was to save, initate the save sequence and create a snackbar saying it is being saved
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK) //if you hit a button
        {
            long id = data.getLongExtra("ID", 0);
            boolean isDelete = data.getBooleanExtra("TYPE", false);
            if (isDelete == true) {
                deleteDefinitionId((int) id);
            }else{
                saveDefinitiion(data.getStringExtra("TITLE"),data.getStringExtra("WORDCLASS"),data.getStringExtra("DEFINITION"));
                Snackbar saveSnackbar = Snackbar.make(findViewById(android.R.id.content),R.string.dictWIP,Snackbar.LENGTH_LONG);
                saveSnackbar.setAction(R.string.dictUndo,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        saveSnackbar.dismiss();
                    }
                });
                saveSnackbar.show();
            }
        }
    }
    /**
     * async query to pull information from the server using supplied url string when executed.
     * 1: establish connection
     * 2: start query
     * 3: create parser
     * 4: every time a new entry is encountered, create a definition and add it to the list
     * 5: whenever a definition or word class is encountered, add it to the most recent entry
     * 6: continue until all steps are completed
     */
    class DictionaryQuery extends AsyncTask<String, Integer, String[]>{

        @Override
        protected String[] doInBackground(String... strings) {
            //String results[] = new String[5];
            try {
                progress.setVisibility(View.VISIBLE);
                publishProgress(0);
                //Establish connection
                String urlString = strings[0];
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                //Starts the query
                conn.connect();
                Log.i("DictionaryQuery", "connection establishes");

                //Create XML parser
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(), null);
                parser.nextTag();
                Log.e("DictionaryQuery", "pullparser created");

                int entrycount = -1;
                while(parser.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = parser.getName();
                        if (tagName.equals("entry")) {
                            Log.i("DictionaryQuery", "Entry Created");
                            entrycount++;
                            definitions.add(new DictionaryDefinition());
                            definitions.get(entrycount).setDefinitionTitle(parser.getAttributeValue(null, "id"));
                            publishProgress(25);
                        }
                        if (tagName.equals("fl")) {
                            parser.next();
                            definitions.get(entrycount).setWordClass(parser.getText());
                            publishProgress(50);
                        }
                        if (tagName.equals("dt")) {
                            parser.next();
                            definitions.get(entrycount).setDefinition(parser.getText());
                            publishProgress(75);
                        }
                    }
                    parser.next();
                }
                publishProgress(100);
                progress.setVisibility(View.INVISIBLE);

            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage());
                ex.printStackTrace();
            }
            return null;
        }
    }

    /**
     * the database class
     */
    public class DatabaseOpener extends SQLiteOpenHelper{
        final static String DATABASE_NAME = "dictDB";
        final static int VERSION = 3;
        final static String TABLE_NAME = "SavedDefinitions";
        final static String COL_TITLE = "TITLE";
        final static String COL_WORDCLASS = "WORDCLASS";
        final static String COL_DEFINITION = "DEFINITION";

        /**
         * database object with given context
         * @param context
         */
        public DatabaseOpener(Context context) {
            super(context, DATABASE_NAME, null, VERSION);
        }

        /**
         * runs when a new DB is created as no other one was existng with the name.
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " +TABLE_NAME+ "( id INTEGER PRIMARY KEY AUTOINCREMENT, TITLE text, WORDCLASS text, DEFINITION text);");
        }

        /**
         * if the DB version in the existing program is newer, drop existing table and make a new one
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
            onCreate(db);
        }

        /**
         * when DB is opened, execute the super version of onOpen
         * @param db
         */
        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
        }

        /**
         * return the
         * @return
         */
        public SQLiteDatabase getWriteableDatabase() {
            return super.getWritableDatabase();
        }
    }

    /**
     * removes the object from the current DB with the given idea
     * if the active view is showing the local storage, perge the list and reload the database
     * @param id The id of the object to delete
     */
    public void deleteDefinitionId(int id)
    {
        Log.i("Delete this message:" , " id="+id);
        db.delete(TABLE_NAME, "id=?", new String[]{Long.toString(id)});
        if (currentMode == true){
            definitions.clear();
            Cursor csr = db.rawQuery("SELECT * from " + TABLE_NAME, null);
            csr.moveToFirst();
            for(int i = 0; i < csr.getCount(); i++){
                long idnum = csr.getLong(csr.getColumnIndex( "id"));
                String title = csr.getString(csr.getColumnIndex( COL_TITLE ));
                String wClass = csr.getString(csr.getColumnIndex( COL_WORDCLASS ));
                String definition = csr.getString(csr.getColumnIndex( COL_DEFINITION ));
                addToDefinitions(idnum, title, wClass, definition);
                csr.moveToNext();
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * when run, adds a definition to the database with the given title, wordclass and definition and an incrementally generated ID
     * if the current view is local storage, run the method to add the new definition to the listview
     * @param title title of new definition entry
     * @param wordclass wordclass of new definition entry
     * @param definition definition of new definition entry
     */
    public void saveDefinitiion(String title, String wordclass, String definition){
        ContentValues cv = new ContentValues();
        cv.put(dataOpener.COL_TITLE, title);
        cv.put(dataOpener.COL_WORDCLASS, wordclass);
        cv.put(dataOpener.COL_DEFINITION, definition);
        long id = db.insert(TABLE_NAME, "NullColumnName", cv);
        if (currentMode == true){
            addToDefinitions(id,title,wordclass,definition);
            adapter.notifyDataSetChanged();
        }
    }


}
