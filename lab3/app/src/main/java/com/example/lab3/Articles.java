package com.example.lab3;

import java.util.ArrayList;
import java.util.List;

public class Articles {
    public List<Article> Articles;

    public Articles(){
        Articles = new ArrayList<Article>();
    }

    public List<Article> getList(){
        return Articles;
    }
}
