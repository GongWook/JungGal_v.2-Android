package com.gnu_graduate_project_team.junggal_v2;

public class AlarmItem {


    int viewType; //화면 타입
    private String title;
    private String message;
    private String time;
    Object object;

    public AlarmItem()
    {

    }

    public AlarmItem(int viewType, String title, String message, String time, Object object) {
        this.viewType = viewType;
        this.title = title;
        this.message = message;
        this.time = time;
        this.object = object;
    }

    public AlarmItem(int viewType, AlarmVO alarm) {
        this.viewType = viewType;
        message = alarm.getMessage();
        object =alarm;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
