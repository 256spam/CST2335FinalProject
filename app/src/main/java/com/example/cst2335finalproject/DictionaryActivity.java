package com.example.cst2335finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
        progress.setProgress(50);

        //temporary feature to add stand-in entries
        addToChat("Yeet", "Throwing with force", "The act of throwing an item with the goal of force and speed. Past tense is yote.");
        addToChat("Kobe", "Throwing with accuracy", "The act of throwing an item with the goal of accuracy and distance.");

        helpButton.setOnClickListener( c -> {
            Toast toast1 = Toast.makeText(getApplicationContext(), "Author, Peter Best. Version number: 0.2", Toast.LENGTH_SHORT);
            toast1.show();
        });

        toggleMode.setOnClickListener( c -> {
            Toast toast1 = Toast.makeText(getApplicationContext(), "Feature unfinished, come back later", Toast.LENGTH_SHORT);
            toast1.show();
        });

        searchButton.setOnClickListener( c -> {
            Toast toast1 = Toast.makeText(getApplicationContext(), "Feature unfinished, come back later", Toast.LENGTH_SHORT);
            toast1.show();
        });

        adapter = new DictionaryAdapter(getApplicationContext(), R.layout.dict_listview_layout,definitions);
        definitionsList.setAdapter(adapter);

        definitionsList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert = createAlertDiaglog(parent, position);
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
    public AlertDialog.Builder createAlertDiaglog(AdapterView<?> parent, int position){
        AlertDialog.Builder adb = new AlertDialog.Builder(DictionaryActivity.this);
        adb.setTitle("Definition");
        adb.setMessage(" selected Item is: " +definitions.indexOf(parent.getItemAtPosition(position)));


        adb.setPositiveButton("Save Definiton", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //todo implement saving


                Snackbar saveSnackbar = Snackbar.make(findViewById(android.R.id.content),"Item saved to local storage. (WIP)",Snackbar.LENGTH_LONG);
                saveSnackbar.setAction("Undo",new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //todo implement undoing


                        Toast toast1 = Toast.makeText(getApplicationContext(), "Feature unfinished, come back later.", Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                });
                saveSnackbar.show();
            }
        });
        adb.setNegativeButton("Close", null);
        return adb;
    }

    /**
     * create new definition and add it to the list of definitions
     * @param title title of definition
     * @param shortDef short definition
     * @param longDef long definition
     */
    void addToChat(String title, String shortDef, String longDef){
        DictionaryDefinition definition = new DictionaryDefinition(title,shortDef,longDef);
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
}
