package com.example.bookstore.Classes;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String userUID;
    private String Name;
    private String Surname;
    private String Address;
    private String City;

    public User(){}

    public User(String userUID, String name, String surname,String address,String city) {
        this.userUID = userUID;
        this.Name = name;
        this.Surname = surname;
        this.Address = address;
        this.City = city;
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

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userUID", getUserUID());
        result.put("name", getName());
        result.put("surname", getSurname());
        result.put("address", getAddress());
        result.put("city", getCity());
        return result;
    }

}
