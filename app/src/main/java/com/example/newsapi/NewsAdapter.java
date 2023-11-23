package com.example.newsapi;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsArticle> {
    public NewsAdapter(Context context, ArrayList<NewsArticle> newsList) {
        super(context, 0, newsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NewsArticle newsArticle = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }

        TextView headlineTextView = convertView.findViewById(R.id.headlineTextView);
        headlineTextView.setText(newsArticle.getHeadline());

        return convertView;
    }
}
