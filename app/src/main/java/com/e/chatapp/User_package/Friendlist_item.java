package com.e.chatapp.User_package;

public class Friendlist_item {
    private String name;
    private int imageId;

    public Friendlist_item(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getFriendName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
