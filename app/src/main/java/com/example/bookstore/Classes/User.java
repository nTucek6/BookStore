package com.example.bookstore.Classes;

public class User {

    private String userUID;
    private String Name;
    private String Surname;

    public User(){}

    public User(String userUID, String name, String surname) {
        this.userUID = userUID;
        this.Name = name;
        this.Surname = surname;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }
}
