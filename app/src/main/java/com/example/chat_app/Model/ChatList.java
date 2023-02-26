package com.example.chat_app.Model;

public class ChatList {
    public String id ;
    public String last_time_msg;


    public ChatList(String id, String last_time_msg) {
        this.last_time_msg = last_time_msg;
        this.id = id;
    }

    public ChatList() {
    }

    public String getLast_time_msg() {
        return last_time_msg;
    }

    public void setLast_time_msg(String last_time_msg) {
        this.last_time_msg = last_time_msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

