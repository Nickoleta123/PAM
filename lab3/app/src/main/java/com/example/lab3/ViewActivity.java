package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class ViewActivity extends AppCompatActivity {

    Article currentArticle;
    Button saveArticle;
    TextView title;
    TextView userId;
    TextView body;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        title = (TextView) findViewById(R.id.titleValue2);
        userId = (TextView) findViewById(R.id.userIdValue2);
        body = (TextView) findViewById(R.id.summaryValue2);

        saveArticle = (Button) findViewById(R.id.saveArticle2);

        saveArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();

                output.putExtra("isSavedItem", currentArticle.id.toString());
                setResult(RESULT_OK, output);
                finish();
            }
        });

        sendRequest();
    }

    private void sendRequest(){
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = getIntent().getExtras().getString("url");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                currentArticle = gson.fromJson(response, Article.class);

                title.setText(currentArticle.title);
                body.setText(currentArticle.body);
                userId.setText(Integer.toString(currentArticle.userId));


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);
    }
}