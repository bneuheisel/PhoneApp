package com.example.project4;

import android.media.Image;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class Person implements Comparable<Person> {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String url;

    public Person(String name, String address, String phoneNumber, String email, String url) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.url = url;
    }

    //Compare for sorting
    @Override
    public int compareTo(Person other) {
        return this.name.compareTo(other.name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
