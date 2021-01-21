package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateTask extends AppCompatActivity {

    EditText editedName;
    EditText editedHour;
    EditText editedMinutes;
    Button updateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_task);

        editedName = (EditText)findViewById(R.id.editTaskName);
        editedHour = (EditText)findViewById(R.id.editHour);
        editedMinutes = (EditText)findViewById(R.id.editMinutes);

        updateTask = (Button) findViewById(R.id.updateTask);

        Intent current = getIntent();
        editedName.setText(current.getStringExtra("taskName").toString());
        editedHour.setText(Integer.toString(current.getIntExtra("taskHour",0)));
        editedMinutes.setText(Integer.toString(current.getIntExtra("taskMinutes",0)));


        updateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent output = new Intent();

                output.putExtra("taskName", editedName.getText().toString());
                output.putExtra("taskHour", editedHour.getText().toString());
                output.putExtra("taskMinutes", editedMinutes.getText().toString());
                output.putExtra("update", "true");

                setResult(RESULT_OK, output);

                finish();
            }
        });
    }
}