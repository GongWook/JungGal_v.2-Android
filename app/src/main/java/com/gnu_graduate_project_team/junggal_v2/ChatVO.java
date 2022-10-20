package com.gnu_graduate_project_team.junggal_v2;

import com.google.gson.annotations.SerializedName;

public class ChatVO {

    @SerializedName("chat_id")
    private Integer chat_id;

    @SerializedName("chat_room_id")
    private Integer chat_room_id;

    @SerializedName("sender")
    private String sender;

    @SerializedName("reader")
    private String reader;

    @SerializedName("content")
    private String content;

    @SerializedName("chat_time")
    private String chat_time;

    public ChatVO() {

    }

    public Integer getChat_id() {
        return chat_id;
    }

    public void setChat_id(Integer chat_id) {
        this.chat_id = chat_id;
    }

    public Integer getChat_room_id() {
        return chat_room_id;
    }

    public void setChat_room_id(Integer chat_room_id) {
        this.chat_room_id = chat_room_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReader() {
        return reader;
    }

    public void setReader(String reader) {
        this.reader = reader;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getChat_time() {
        return chat_time;
    }

    public void setChat_time(String chat_time) {
        this.chat_time = chat_time;
    }

    @Override
    public String toString() {
        return "ChatVO{" +
                "chat_id=" + chat_id +
                ", chat_room_id=" + chat_room_id +
                ", sender='" + sender + '\'' +
                ", reader='" + reader + '\'' +
                ", content='" + content + '\'' +
                ", chat_time='" + chat_time + '\'' +
                '}';
    }
}
