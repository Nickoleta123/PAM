package com.example.lab1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.app.SearchManager;
import android.widget.EditText;

import java.nio.channels.Channel;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    EditText editTextSearch;
    Button btnSearch;
    RadioGroup radioCamera;
    Button openCamera;
    Uri image_uri;
    Button randomButton;
    Button notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openCamera = (Button) findViewById(R.id.openCameraId);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });

        btnSearch = findViewById(R.id.googleSearchId);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSearch();
            }
        });

        randomButton = (Button) findViewById(R.id.randomButton);
        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomNumber();
            }
        });

        notification = (Button) findViewById(R.id.notificationButton);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNotification();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            super.onActivityResult(requestCode, resultCode, data);

            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra("picture", image_uri);

            startActivity(intent);
        }
    }



    private void openCamera(){

        radioCamera = (RadioGroup) findViewById(R.id.radioGroup);

        //                if(radioCamera.getCheckedRadioButtonId() == R.id.frontCamera){
        //                    Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
        //                    startActivity(intent2);
        //                }
        //                if(radioCamera.getCheckedRadioButtonId() == R.id.backCamera){
        //                    Intent intent2 = new Intent("android.media.action.IMAGE_CAPTURE");
        //                    startActivity(intent2);
        //                }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent,1001);
    }

    private void getRandomNumber(){
        Random rand = new Random();
        int next = rand.nextInt(100);

        randomButton.setText(String.format("Random number (%s)", next));
    }

    private void googleSearch(){
        editTextSearch = findViewById(R.id.editTextSearchId);
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        String term = editTextSearch.getText().toString();
        intent.putExtra(SearchManager.QUERY, term);
        startActivity(intent);
    }

    private void createNotification(){
        scheduleNotification(getNotification(), 20000);
    }

    private void scheduleNotification(Notification notification, int delay) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle("Notificare");
        builder.setContentText("Notificare pentru laborator 1");

        return builder.build();
    }
}

