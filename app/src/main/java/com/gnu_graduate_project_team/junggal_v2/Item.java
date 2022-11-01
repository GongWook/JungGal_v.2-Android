package com.gnu_graduate_project_team.junggal_v2;

public class Item {
    private String content;
    private String name;
    private String time;
    private int viewType;
    Object object;

    public Item(String content, String name , String time, int viewType) {
        this.content = content;
        this.name = name;
        this.time = time;
        this.viewType = viewType;
    }

    public Item(int viewType, Object object) {
        this.viewType = viewType;
        this.object = object;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public int getViewType() {
        return viewType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
