package com.example.interval;

public class SessionModel {

    private String title;
    private int focusTime;
    private int restTime;

    public SessionModel(String title, int focusTime, int restTime) {
        this.title = title;
        this.focusTime = focusTime;
        this.restTime = restTime;
    }
    public String getTitle() {
        return title;
    }
    public int getFocusTime() {
        return focusTime;
    }
    public int getRestTime() {
        return restTime;
    }
}
