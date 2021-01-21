package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity2 extends AppCompatActivity {

    ImageView imgv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        imgv2 = (ImageView) findViewById(R.id.imageView);

        Intent intent = getIntent();
        Uri ur = intent.getParcelableExtra("picture");

        imgv2.setImageURI(ur);
    }
}