package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ArrayList<Day> dayList;
    CalendarView myCalendar;
    ListView listView;
    Day selectedDay;
    Button newActivity;
    int taskPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        myCalendar = (CalendarView) findViewById(R.id.calendar);
        newActivity = (Button) findViewById(R.id.newActivity);
        dayList = new ArrayList<>();

        addSomeData();
        selectedDay = dayList.get(0);
        updateData();

        myCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                boolean dayIsInList = false;

                for (int i = 0; i < dayList.size(); i++){
                    selectedDay = dayList.get(i);
                    if (selectedDay.Year == year && selectedDay.Month == month+1 && selectedDay.DayOfMonth == dayOfMonth){
                        dayIsInList = true;
                        break;
                    }
                }

                if (dayIsInList == false){
                    selectedDay = new Day(year, month, dayOfMonth);
                    dayList.add(selectedDay);
                }

                updateData();
            }
        });

        newActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTask.class);
                MainActivity.this.startActivityForResult(intent, 1);
            }
        });
    }

    private void createNotification(String notificationMessage, long delay){
        scheduleNotification(getNotification(notificationMessage), delay);
    }

    private void scheduleNotification(Notification notification, long delay) {
        Intent notificationIntent = new Intent(this, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String notificationMessage) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setContentTitle("Notificare Momento");
        builder.setContentText(notificationMessage);

        return builder.build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String name = data.getExtras().getString("taskName").toString();
        int hour = Integer.parseInt(data.getExtras().getString("taskHour").toString());
        int min = Integer.parseInt(data.getExtras().getString("taskMinutes").toString());

        if (data.getExtras().containsKey("update")){
            selectedDay.TaskList.get(taskPosition).TaskName = name;
            selectedDay.TaskList.get(taskPosition).Hours = hour;
            selectedDay.TaskList.get(taskPosition).Minutes = min;
        }
        if (data.getExtras().containsKey("add")){
            selectedDay.TaskList.add(new Task(name, hour, min));

            Calendar c = Calendar.getInstance();
            c.set(selectedDay.Year,  selectedDay.Month-1, selectedDay.DayOfMonth, hour, min, 0);
            Long date = c.getTimeInMillis();
            Long dateNow2 = System.currentTimeMillis();
            long secondsDifference = date - dateNow2;

            createNotification(hour + " : " + min + " " + name, secondsDifference);
        }

        super.onActivityResult(requestCode, resultCode, data);

        updateData();
    }

    private void updateData(){
        listView.setAdapter(new MyListAdaper(MainActivity.this, R.layout.list_item, selectedDay.TaskList));
    }

    private void addSomeData()
    {
        Day selectedDa = new Day(2020,12,15);
        selectedDa.TaskList.add(new Task("dentist",14,10));
        selectedDa.TaskList.add(new Task("magazin", 15,30));

        Day selectedDa1 = new Day(2020,12,16);
        selectedDa1.TaskList.add(new Task("universitate", 10,15));
        selectedDa1.TaskList.add(new Task("biblioteca", 15,30));

        dayList.add(selectedDa);
        dayList.add(selectedDa1);
    }

    private class MyListAdaper extends ArrayAdapter<Task> {
        private int layout;
        private ArrayList<Task> mObjects;
        private MyListAdaper(Context context, int resource, ArrayList<Task> objects) {
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
                ViewHolder viewHolder = new ViewHolder();

                viewHolder.title = (TextView) convertView.findViewById(R.id.list_item_name);
                viewHolder.hour = (TextView) convertView.findViewById(R.id.hour);
                viewHolder.min = (TextView) convertView.findViewById(R.id.min);

                viewHolder.buttonEdit = (Button) convertView.findViewById(R.id.list_item_edit);
                viewHolder.buttonRemove = (Button) convertView.findViewById(R.id.list_item_delete);

                viewHolder.buttonRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedDay.TaskList.remove(position);
                        updateData();
                    }
                });

                viewHolder.buttonEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        taskPosition = position;

                        Intent intent = new Intent(MainActivity.this, UpdateTask.class);

                        intent.putExtra("taskName", selectedDay.TaskList.get(position).TaskName.toString());
                        intent.putExtra("taskHour", selectedDay.TaskList.get(position).Hours);
                        intent.putExtra("taskMinutes", selectedDay.TaskList.get(position).Minutes);

                        MainActivity.this.startActivityForResult(intent, 1);

                        updateData();
                    }
                });

                convertView.setTag(viewHolder);
            }

            mainViewholder = (ViewHolder) convertView.getTag();
            mainViewholder.title.setText(getItem(position).TaskName);
            mainViewholder.hour.setText(Integer.toString(getItem(position).Hours));
            mainViewholder.min.setText(Integer.toString(getItem(position).Minutes));

            return convertView;
        }
    }

    public class ViewHolder {
        TextView title;
        TextView hour;
        TextView min;
        Button buttonEdit;
        Button buttonRemove;
    }
}