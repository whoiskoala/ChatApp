package com.e.chatapp.User_package;

public class User {
    private String userID;
    private String username;
    private String password;
    private String email;
    private String birthday;

    public User(){

    }

    public User( String username,  String email, String birthday,String password){
//        this.userID = userID;
        this.username = username;
        this.email = email;
        this.birthday = birthday;
        this.password = password;
    }

//    public String getUserID(){
//        return userID;
//    }
//    public void setUserID(String userID) {
//        this.userID = userID;
//    }
//
    public String getUsername(){
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday(){
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
