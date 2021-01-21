package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddTask extends AppCompatActivity {

    EditText name;
    EditText hour;
    EditText minutes;
    Button addActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        name = (EditText) findViewById(R.id.taskName);
        hour = (EditText) findViewById(R.id.addHours);
        minutes = (EditText) findViewById(R.id.addMinutes);

        addActivity = (Button) findViewById(R.id.add);

        addActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();
                output.putExtra("taskName", name.getText().toString());
                output.putExtra("taskHour", hour.getText().toString());
                output.putExtra("taskMinutes", minutes.getText().toString());

                output.putExtra("add", "true");

                setResult(RESULT_OK, output);

                finish();
            }
        });
    }
}