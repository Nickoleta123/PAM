package com.example.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //progress Data
    AnimationDrawable rocketAnimation;
    ImageView imageView;

    //fields Data
    TextView userIdText;
    TextView userIdValue;
    TextView titleText;
    TextView titleValue;
    TextView summaryText;
    TextView summaryValue;

    Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  progress
        imageView = (ImageView) findViewById(R.id.imageView);
        refreshButton = (Button) findViewById(R.id.refreshButton);

        // fields
        userIdText = (TextView) findViewById(R.id.userIdText);
        userIdValue = (TextView) findViewById(R.id.userIdValue2);

        titleText = (TextView) findViewById(R.id.titleText);
        titleValue = (TextView) findViewById(R.id.titleValue2);

        summaryText = (TextView) findViewById(R.id.summaryText);
        summaryValue = (TextView) findViewById(R.id.summaryValue2);

        userIdText.setVisibility(View.INVISIBLE);
        userIdValue.setVisibility(View.INVISIBLE);
        titleText.setVisibility(View.INVISIBLE);
        titleValue.setVisibility(View.INVISIBLE);
        summaryText.setVisibility(View.INVISIBLE);
        summaryValue.setVisibility(View.INVISIBLE);

        imageView.setVisibility(View.VISIBLE);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgress();

                sendRequestId("https://jsonplaceholder.typicode.com/posts/1");
            }
        });
    }

    private void showProgress(){
        userIdText.setVisibility(View.INVISIBLE);
        userIdValue.setVisibility(View.INVISIBLE);
        titleText.setVisibility(View.INVISIBLE);
        titleValue.setVisibility(View.INVISIBLE);
        summaryText.setVisibility(View.INVISIBLE);
        summaryValue.setVisibility(View.INVISIBLE);

        imageView.setVisibility(View.VISIBLE);

        imageView.setBackgroundResource(R.drawable.list_item);

        rocketAnimation = (AnimationDrawable) imageView.getBackground();
        rocketAnimation.start();
    }

    private void showAllData(){
        userIdText.setVisibility(View.VISIBLE);
        userIdValue.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.VISIBLE);
        titleValue.setVisibility(View.VISIBLE);
        summaryText.setVisibility(View.VISIBLE);
        summaryValue.setVisibility(View.VISIBLE);

        imageView.setVisibility(View.INVISIBLE);
    }

    private void sendRequestId(String url){
        final RequestQueue queue = Volley.newRequestQueue(this);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Gson gson = new Gson();
                Article currentArticle = gson.fromJson(response, Article.class);

                userIdValue.setText(Integer.toString(currentArticle.userId));
                titleValue.setText(currentArticle.title);
                summaryValue.setText(currentArticle.body);

                rocketAnimation.stop();

                showAllData();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                queue.add(stringRequest);
            }
        }, 5000);
    }
}