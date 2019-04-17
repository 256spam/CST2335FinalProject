package com.example.cst2335finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DictionaryAdapter extends ArrayAdapter<DictionaryDefinition> {
    Context context;
    int resource;
    ArrayList<DictionaryDefinition> DefinitionList = null;

    /**
     * all code created via the use of inclass examples, lab work and official documentation from developer.android.com
     */

    public DictionaryAdapter(Context c, int r, ArrayList<DictionaryDefinition> ml){
        super(c,r,ml);
        context = c;
        resource = r;
        DefinitionList = ml;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        DictionaryDefinition definition = DefinitionList.get(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.dict_listview_layout, parent, false);

        ImageView icon = convertView.findViewById(R.id.icon);
        TextView title = convertView.findViewById(R.id.dictDefinitonTitle);
        TextView shortDefinition = convertView.findViewById(R.id.dictDefinitonShort);

        icon.setImageResource(R.drawable.main_dic);
        title.setText(definition.getDefinitonTitle());
        shortDefinition.setText(definition.getDefinition());
        return convertView;
    }
}

