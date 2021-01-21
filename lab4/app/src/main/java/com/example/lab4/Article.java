package com.example.lab4;

import com.google.gson.annotations.SerializedName;

public class Article {
    @SerializedName("id")
    public Integer id;

    @SerializedName("userId")
    public Integer userId;

    @SerializedName("title")
    public String title;

    @SerializedName("body")
    public String body;

    public Article() {
        super();
    }

    public Article(Integer i, Integer u, String t, String b) {
        id = i;
        userId = u;
        title = t;
        body = b;
    }
}
