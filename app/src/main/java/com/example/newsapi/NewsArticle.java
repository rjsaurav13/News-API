package com.example.newsapi;

public class NewsArticle {
    private String headline;
    private String url;
    private String publishedAt;

    public NewsArticle(String headline, String url) {
        this.headline = headline;
        this.url = url;
    }
    public String getPublishedAt() {
        return publishedAt;
    }

    public String getHeadline() {
        return headline;
    }

    public String getUrl() {
        return url;
    }
}
