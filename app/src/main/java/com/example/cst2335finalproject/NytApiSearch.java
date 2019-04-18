package com.example.cst2335finalproject.NYTArticle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.cst2335finalproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class NytApiSearch extends AppCompatActivity {

    private TextView copyrightView ;
    private ProgressBar progressBar;
    public String aString, urlString,para ;

    String inputWord;
    TextView inputWordView;

    NytDataBaseHelper dbInitiate;
    SQLiteDatabase db;

    private ListView nyList;

    Article article;
    ArrayList<Article> articleList = new ArrayList<>();
    protected String preUrl;
    /**
     *
     * @param savedInstanceState
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_newyork_api);

        preUrl =" https://api.nytimes.com/svc/search/v2/articlesearch.json?q=Tesla&api-key=z0pR0Dz3Ke0loLw2kFTPk3tEPvezSe26";

        NewsFeedQuery networkThread = new NewsFeedQuery();

       //onClick() {
           networkThread.execute(preUrl );
//        "http://torunski.ca/CST2335_XML.xml"
       // }

        nyList = findViewById(R.id.nyt_list);
        progressBar = findViewById(R.id.progressBar_kn);
        progressBar.setVisibility(View.VISIBLE);
        copyrightView = findViewById(R.id.copyright_kn);
        Button saveBtn = findViewById(R.id.nyt_save);

        ArrayList<Article> newsArrayList = new ArrayList<Article>();


//         get user input word
        Intent previousPage = getIntent();
        inputWord = previousPage.getStringExtra("inputWord");
        // clicked on view save word button
            saveBtn.setOnClickListener(c->{
        // save word into database
        //get a database:
            dbInitiate = new NytDataBaseHelper(this);
            db = dbInitiate.getWritableDatabase();
            this.saveWord(db, inputWord);
        });

    }

    /**
     *
     * @param db
     * @param inputWord
     */
    private void saveWord(SQLiteDatabase db, String inputWord){
        // get word content
        //add to the database and get the new ID
        ContentValues newRowValues = new ContentValues();
        //put string word content in the word_content column:
        newRowValues.put(dbInitiate.COL_Article, inputWord);
        //insert into the database:
        long newId = db.insert(dbInitiate.TABLE_NAME, null, newRowValues);
        String saveMsg = "";
        if(newId>0){
            saveMsg = "Article Saved!.";
        }else{
            saveMsg = "Not Completed.";
        }
        // show toast bar if saved successful
        Toast.makeText(NytApiSearch.this, saveMsg, Toast.LENGTH_LONG).show();
    }


    // a subclass of AsyncTask                  Type1    Type2    Type3
    private class NewsFeedQuery extends AsyncTask<String, Integer, String>
    {
        /**
         *
         * @param params
         * @return
         */
        @Override
        protected String doInBackground(String... params) {

            try {
                //get the string url:
                String myUrl = params[0];
                //create the network connection:
                URL url = new URL(myUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 ); // milliseconds
                conn.setConnectTimeout(15000  ); //milliseconds
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                InputStream inStream = conn.getInputStream();

                URL UVurl = new URL("https://api.nytimes.com/svc/search/v2/articlesearch.json?q=Tesla&api-key=z0pR0Dz3Ke0loLw2kFTPk3tEPvezSe26");
                HttpURLConnection UVConnection = (HttpURLConnection) UVurl.openConnection();
                inStream = UVConnection.getInputStream();

                //create a JSON object from the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                JSONObject jObject = new JSONObject(result);
                JSONObject jsonObjectGetNumbers = jObject.optJSONObject("response");
                JSONArray jsonArray = jsonObjectGetNumbers.getJSONArray("docs");

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject c = jsonArray.getJSONObject(i);

                    // Storing each json item in variable

                    urlString = c.getString("web_url");
                    para = c.getJSONObject("byline").getString("original");
                    aString = c.getJSONObject("headline").getString("main");


                    Article article = new Article(urlString, aString, para);
                    articleList.add(article);



//                    ArticleAdapter artAdt = new ArticleAdapter(newsArrayList, getApplicationContext());
//                    ArticleAdapter artAdt1 = new ArticleAdapter(newsArrayList, getApplicationContext());
//                    nyList.setAdapter(artAdt);
//                    nyList.setAdapter(artAdt1);

                }
//                articleList.add(article);
                //END of UV rating

                publishProgress(15);
                publishProgress(50);

            }catch (Exception ex)
            {
                Log.e("Crash!!", ex.getMessage() );
            }

            //return type 3, which is String:
            return "Finished task";
        }


        /**
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Integer... values) {

//            int size = articleList.size();
//            for (int i = 0;  i<size; i++) {
//                ArticleAdapter adt = new ArticleAdapter(articleList, getApplicationContext());
//                nyList.setAdapter(adt);
//            }

            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(15);
            progressBar.setProgress(50);
            progressBar.setMax(100);
        }

        /**
         *
         * @param args
         */
        @Override
        protected void onPostExecute(String args) {
            Log.i("AsyncTask", "onPostExecute" );
            int size = articleList.size();
            for (int i = 0;  i<size; i++) {
                ArticleAdapter adt = new ArticleAdapter(articleList, getApplicationContext());
                nyList.setAdapter(adt);
            }
//            copyrightView.setText(urlString);
        }


    }


}
