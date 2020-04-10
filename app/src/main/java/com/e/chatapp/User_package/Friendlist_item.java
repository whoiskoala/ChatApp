package com.e.chatapp.User_package;

public class Friendlist_item {
    public String username;
    public String email;
    public String image;

    public Friendlist_item(){

    }

    public Friendlist_item(String username, String image, String email) {
        this.username = username;
        this.image = image;
        this.email = email;
    }

    public String getFriendName() {
        return username;
    }
    public void setFriendName(String username) {
        this.username = username;
    }

    public String getFriendEmail() {
        return email;
    }
    public void setFriendEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
}
