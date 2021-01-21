package com.example.lab2;

import java.util.ArrayList;
import java.util.Date;

public class Day {
    public int Year;
    public int Month;
    public int DayOfMonth;

    public ArrayList<Task> TaskList;

    public Day( int y, int m, int d){
        Year = y;
        Month = m;
        DayOfMonth = d;

        TaskList = new ArrayList<Task>();
    }
}
