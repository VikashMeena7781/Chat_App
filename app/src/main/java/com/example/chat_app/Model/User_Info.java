package com.example.chat_app.Model;

public class User_Info {
    private String username,id,ImageUrl,status,search;

    public User_Info(String username, String id,  String ImageUrl,String status,String search) {
        this.username = username;
        this.id = id;
        this.ImageUrl=ImageUrl;
        this.search=search;
        this.status=status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User_Info() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
    public String getImageUrl(){
        return this.ImageUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}

