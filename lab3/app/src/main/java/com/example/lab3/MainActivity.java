package com.example.lab3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button savedArticles;
    Button onlineArticles;
    List<Article> articles;
    List<Article> fileArticles;
    ListView listView;
    String fileName = "saved.txt";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fileArticles = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listview);
        savedArticles = (Button) findViewById(R.id.savedArticles);
        onlineArticles = (Button) findViewById(R.id.onlineArticles);

        sendRequest();


        savedArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readFromFile();
            }
        });

        onlineArticles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });
    }

    private void updateSavedViewList(){
        listView.setAdapter(new MySavesListAdapter(MainActivity.this, R.layout.saved_list_item, fileArticles));
    }

    private void writeInFile(){
        try {
            final String TESTSTRING = new Gson().toJson(fileArticles);

            FileOutputStream fOut = openFileOutput(fileName, MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);

            osw.write(TESTSTRING);

            osw.flush();
            osw.close();

            readFromFile();
        } catch (IOException ioe)
        {
            System.out.println("lol");
            ioe.printStackTrace();
        }
    }

    private void readFromFile() {
        FileInputStream fIn = null;
        try {
            fIn = openFileInput(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        InputStreamReader isr = new InputStreamReader(fIn);
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            System.out.println("lol");
        }

        Gson gson = new Gson();
        fileArticles = new ArrayList<>(Arrays.asList(gson.fromJson(text.toString(), Article[].class)));
        updateSavedViewList();
    }

    private void sendRequest(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://jsonplaceholder.typicode.com/posts";

        System.out.println(url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        articles = Arrays.asList(gson.fromJson(response, Article[].class));
                        listView.setAdapter(new MyListAdapter(MainActivity.this, R.layout.list_item, articles));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);
    }

    private class MyListAdapter extends ArrayAdapter<Article> {
        private int layout;
        private List<Article> mObjects;
        private MyListAdapter(Context context, int resource, List<Article> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();

                viewHolder.userId = (TextView) convertView.findViewById(R.id.userIdValue);
                viewHolder.title = (TextView) convertView.findViewById(R.id.titleValue);
                viewHolder.body = (TextView) convertView.findViewById(R.id.summaryValue);

                viewHolder.viewButton = (Button) convertView.findViewById(R.id.viewItem);

                viewHolder.viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, ViewActivity.class);

                        intent.putExtra("url", "https://jsonplaceholder.typicode.com/posts/"+viewHolder.userId.getTag());

                        MainActivity.this.startActivityForResult(intent, 0);
                    }
                });

                convertView.setTag(viewHolder);
            }

            mainViewholder = (ViewHolder) convertView.getTag();

            mainViewholder.userId.setText(Integer.toString(getItem(position).userId));
            mainViewholder.userId.setTag(getItem(position).id);
            mainViewholder.title.setText(getItem(position).title);
            mainViewholder.body.setText(getItem(position).body);

            return convertView;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("loh");
        System.out.println("on result" + data.getExtras().containsKey("isSavedItem"));

        if (data.getExtras().containsKey("isSavedItem")){
            sendRequestId("https://jsonplaceholder.typicode.com/posts/"+data.getExtras().getString("isSavedItem"));
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void sendRequestId(String url){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Article currentArticle = gson.fromJson(response, Article.class);

                fileArticles.add(currentArticle);

                writeInFile();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);
    }

    private class MySavesListAdapter extends ArrayAdapter<Article> {
        private int layout;
        private List<Article> mObjects;
        private MySavesListAdapter(Context context, int resource, List<Article> objects) {
            super(context, resource, objects);
            mObjects = objects;
            layout = resource;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mainViewholder = null;
            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(layout, parent, false);
                final ViewHolder viewHolder = new ViewHolder();

                viewHolder.userId = (TextView) convertView.findViewById(R.id.userIdValueSav);
                viewHolder.title = (TextView) convertView.findViewById(R.id.titleValueSav);
                viewHolder.body = (TextView) convertView.findViewById(R.id.summaryValueSav);

                viewHolder.viewButton = (Button) convertView.findViewById(R.id.viewItemSav);

                viewHolder.viewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("look");
                        int index=0;
                        for (int i=0; i< fileArticles.size(); i++){
                            if (fileArticles.get(i).id == viewHolder.userId.getTag()){
                                index = i;
                            }
                        }
                        fileArticles.remove(index);
                        writeInFile();
                    }
                });

                convertView.setTag(viewHolder);
            }

            mainViewholder = (ViewHolder) convertView.getTag();

            mainViewholder.userId.setText(Integer.toString(getItem(position).userId));
            mainViewholder.userId.setTag(getItem(position).id);
            mainViewholder.title.setText(getItem(position).title);
            mainViewholder.body.setText(getItem(position).body);

            return convertView;
        }
    }

    public class ViewHolder {
        TextView userId;
        TextView title;
        TextView body;
        Button viewButton;
    }
}