/***
 * Author: Kevin Nghiem
 * Last modified: Mar 25, 2019
 * **/


package com.example.cst2335finalproject.NYTArticle;

import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import com.example.cst2335finalproject.R;


public class ArticleAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Article> article;

    /**
     *
     * @param news
     * @param context
     */

    public ArticleAdapter(ArrayList<Article> news, Context context) {
        this.article = news;
        this.context = context;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    /**
     *
     * @return
     */
    @Override
    public int getCount() {
        return article.size();
    }
    /**
     *
     * @param position
     * @return
     */
    @Override
    public Object getItem(int position) {
        return article.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.activity_article_search_list, parent, false);
        }
        Article currentItem = (Article) getItem(position);
        TextView textViewUrl = convertView.findViewById(R.id.nyt_url);
        TextView textViewSnip = convertView.findViewById(R.id.nyt_snip);
        TextView textViewPara = convertView.findViewById(R.id.nyt_para);



        textViewUrl.setText(currentItem.getTitle());
        textViewSnip.setText(currentItem.getOrganization());
        textViewPara.setText(currentItem.getArticleID());

        return convertView;
    }

}
