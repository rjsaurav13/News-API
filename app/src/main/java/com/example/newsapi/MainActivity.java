package com.example.newsapi;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private ArrayList<NewsArticle> newsList;
    private NewsAdapter newsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(this, newsList);

        ListView listView = findViewById(R.id.newsListView);
        listView.setAdapter(newsAdapter);

        // Set click listener for the ListView items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                openNewsInBrowser(position);
            }
        });

        new FetchNewsTask().execute();
    }

    private void openNewsInBrowser(int position) {
        NewsArticle newsArticle = newsList.get(position);
        String url = newsArticle.getUrl();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }


    public void sortOldToNew(View view) {
        Collections.sort(newsList, new Comparator<NewsArticle>() {
            @Override
            public int compare(NewsArticle article1, NewsArticle article2) {
                return article1.getPublishedAt().compareTo(article2.getPublishedAt());
            }
        });
        newsAdapter.notifyDataSetChanged();
    }
    public void onCreate() {

        FirebaseApp.initializeApp(this);
    }



    public void sortNewToOld(View view) {
        Collections.sort(newsList, new Comparator<NewsArticle>() {
            @Override
            public int compare(NewsArticle article1, NewsArticle article2) {
                return article2.getPublishedAt().compareTo(article1.getPublishedAt());
            }
        });
        newsAdapter.notifyDataSetChanged();
    }


    private class FetchNewsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://candidate-test-data-moengage.s3.amazonaws.com/Android/news-api-feed/staticResponse.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder result = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }

                bufferedReader.close();
                inputStream.close();
                connection.disconnect();

                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                parseJson(s);
            }
        }

        private void parseJson(String json) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                JSONArray articles = jsonObject.getJSONArray("articles");

                for (int i = 0; i < articles.length(); i++) {
                    JSONObject article = articles.getJSONObject(i);
                    String headline = article.getString("title");
                    String url = article.getString("url");

                    NewsArticle newsArticle = new NewsArticle(headline, url);
                    newsList.add(newsArticle);
                }

                newsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
