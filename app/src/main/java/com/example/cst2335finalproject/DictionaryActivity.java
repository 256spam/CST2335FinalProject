package com.example.cst2335finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    ArrayList<DictionaryDefinition> definitions;
    DictionaryAdapter adapter;

    /**
     *assign layout to activity
     * assign layout objects to variables
     * activate toolbar
     * define basic variables
     * set listeners for buttons
     * create and define adapter for listview
     * set listview item click reaction
     * assign alert dialog to show when listview item is clicked.
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

        setSupportActionBar(toolbar);

        progress.setVisibility(View.VISIBLE);

        //temporary feature to add stand-in entries
        addToDefinitions("Yeet", "Throwing with force", "The act of throwing an item with the goal of force and speed. Past tense is yote.","Verb");
        addToDefinitions("Kobe", "Throwing with accuracy", "The act of throwing an item with the goal of accuracy and distance.","Verb");

        helpButton.setOnClickListener( c -> {
            AlertDialog.Builder alert = createHelpMenu();
            alert.show();
        });

        toggleMode.setOnClickListener( c -> {
            Toast toast1 = Toast.makeText(getApplicationContext(), "Feature unfinished, come back later", Toast.LENGTH_SHORT);
            toast1.show();
        });

        searchButton.setOnClickListener( c -> {
            if (searchFeild.getText() != null) {
                definitions.clear();
                DictionaryQuery query = new DictionaryQuery();
                query.execute("https://www.dictionaryapi.com/api/v1/references/sd3/xml/" + searchFeild.getText() + "?key=4556541c-b8ed-4674-9620-b6cba447184f");
                adapter.notifyDataSetChanged();
            }
        });

        adapter = new DictionaryAdapter(getApplicationContext(), R.layout.dict_listview_layout,definitions);
        definitionsList.setAdapter(adapter);

        definitionsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert = createDefinitionAlertDialog(parent, position);
                alert.show();
            }
        });

    }


    /**
     * create builder
     * set title of alert and message
     * add saving definition to local list of values
     * add snackbar to positive button with undo button
     * add close button to alert
     * @param parent the parent adapter view
     * @param position the position in the adapterview to use
     * @return the completed alert dialog
     */
    public AlertDialog.Builder createDefinitionAlertDialog(AdapterView<?> parent, int position){
        AlertDialog.Builder adb = new AlertDialog.Builder(DictionaryActivity.this);
        adb.setTitle(R.string.dictDefinitionTitle);
        adb.setMessage(definitions.get(position).getDefinitonTitle() +"\n" +definitions.get(position).getWordClass()+"\n" +definitions.get(position).getDefinition());


        adb.setPositiveButton(R.string.dictSave, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //todo implement saving


                Snackbar saveSnackbar = Snackbar.make(findViewById(android.R.id.content),R.string.dictWIP,Snackbar.LENGTH_LONG);
                saveSnackbar.setAction(R.string.dictUndo,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //todo implement undoing


                        Toast toast1 = Toast.makeText(getApplicationContext(), R.string.dictWIP, Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                });
                saveSnackbar.show();
            }
        });
        adb.setNegativeButton(R.string.dictCloseOption, null);
        return adb;
    }

    public AlertDialog.Builder createHelpMenu(){
        AlertDialog.Builder adb = new AlertDialog.Builder(DictionaryActivity.this);
        adb.setTitle(R.string.dictHelpMenuTitle);
        adb.setMessage(R.string.dictHelpMenuText);
        adb.setNegativeButton(R.string.dictCloseOption, null);
        return adb;
    }

    /**
     * create new definition and add it to the list of definitions
     * @param title title of definition
     * @param shortDef short definition
     * @param longDef long definition
     */
    void addToDefinitions(String title, String shortDef, String longDef, String wordClass){
        DictionaryDefinition definition = new DictionaryDefinition(title,shortDef,longDef,wordClass);
        definitions.add(definition);
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
     *
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

            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage());
                ex.printStackTrace();
            }
            return null;
        }
    }
}
