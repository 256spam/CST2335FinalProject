package com.example.cst2335finalproject;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class DictionaryFragment extends Fragment {

    private boolean isTablet;
    private Bundle dataFromActivity;
    private long id;

    /**
     * sets the is tablet parameter for the fragment
     * @param tablet
     */
    public void setTablet(boolean tablet) { isTablet = tablet; }

    /**
     * load the data from the bundle into the feilds of the fragment
     * configure the delete button to if its a phone, return to the main activity and initate the delete sequence, or initate the delete sequence by itself if in tablet form
     * configure the save button to if its a phone, return to the main activity and initate the save sequence, or initate the save sequence by itself if in tablet form
     * @param inflater the supplied inflator
     * @param container the viewgroup the fragment is contained in
     * @param savedInstanceState the bundle of data based
     * @return the results of the fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong("ID");

        View result =  inflater.inflate(R.layout.dict_fragment, container, false);

        TextView title = result.findViewById(R.id.title);
        title.setText(dataFromActivity.getString("Title"));

        TextView wordclass = result.findViewById(R.id.wordclass);
        wordclass.setText(dataFromActivity.getString("WordClass"));

        TextView definition = result.findViewById(R.id.definition);
        definition.setText(dataFromActivity.getString("Definition"));

        Button deleteButton = (Button)result.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener( clk -> {

            if(isTablet) { //both the list and details are on the screen:
                DictionaryActivity parent = (DictionaryActivity)getActivity();
                parent.deleteDefinitionId((int)id);
                parent.getSupportFragmentManager().beginTransaction().remove(this).commit();
            }
            else
            {
                DictionaryEmptyActivity parent = (DictionaryEmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra("ID", dataFromActivity.getLong("ID"));
                backToFragmentExample.putExtra("TYPE", true);

                parent.setResult(Activity.RESULT_OK, backToFragmentExample); //send data back to FragmentExample in onActivityResult()
                parent.finish(); //go back
            }
        });
        Button saveButton = (Button)result.findViewById(R.id.saveButton);
        saveButton.setOnClickListener( clk -> {

            if(isTablet) {
                DictionaryActivity parent = (DictionaryActivity)getActivity();
                parent.saveDefinitiion(dataFromActivity.getString("Title"),dataFromActivity.getString("WordClass"),dataFromActivity.getString("Definition"));

                Snackbar saveSnackbar = Snackbar.make(result.findViewById(android.R.id.content),R.string.dictWIP,Snackbar.LENGTH_LONG);
                saveSnackbar.setAction(R.string.dictUndo,new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        saveSnackbar.dismiss();
                    }
                });
                saveSnackbar.show();
            }
            else
            {
                DictionaryEmptyActivity parent = (DictionaryEmptyActivity) getActivity();
                Intent backToFragmentExample = new Intent();
                backToFragmentExample.putExtra("ID", dataFromActivity.getLong("ID"));
                backToFragmentExample.putExtra("TYPE", false);
                backToFragmentExample.putExtra("TITLE", dataFromActivity.getString("Title"));
                backToFragmentExample.putExtra("WORDCLASS", dataFromActivity.getString("WordClass"));
                backToFragmentExample.putExtra("DEFINITION", dataFromActivity.getString("Definition"));

                parent.setResult(Activity.RESULT_OK, backToFragmentExample);
                parent.finish();
            }
        });
        return result;
    }
}
