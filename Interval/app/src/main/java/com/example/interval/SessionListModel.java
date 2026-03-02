package com.example.interval;

public class SessionListModel {
    private int id;
    private String title, date;
    private int focusTime, restTime;

    public SessionListModel(int id, String title, int focusTime, int restTime, String date) {
        this.id        = id;
        this.title     = title;
        this.focusTime = focusTime;
        this.restTime  = restTime;
        this.date      = date;
    }

    public int getId()         { return id; }
    public String getTitle()   { return title; }
    public int getFocusTime()  { return focusTime; }
    public int getRestTime()   { return restTime; }
    public String getDate()    { return date; }
}