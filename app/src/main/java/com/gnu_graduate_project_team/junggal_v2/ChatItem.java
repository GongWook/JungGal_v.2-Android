package com.gnu_graduate_project_team.junggal_v2;

public class ChatItem {

    int viewType; //화면 타입
    private String title;
    private String message;
    private String time;
    Object object;

    public ChatItem(int viewType, ChatVO chat) {
        this.viewType = viewType;
        this.object = chat;
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

    @Override
    public String toString() {
        return "ChatItem{" +
                "viewType=" + viewType +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", time='" + time + '\'' +
                ", object=" + object +
                '}';
    }
}
