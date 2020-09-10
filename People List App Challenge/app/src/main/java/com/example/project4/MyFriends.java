package com.example.project4;

import java.util.ArrayList;
import java.util.List;

public class MyFriends {

    List<Person> myFriendsList;

    public MyFriends(List<Person> myFriendsList) {
        this.myFriendsList = myFriendsList;
    }

    public MyFriends(){
        String[] startingNames = {"Anselm", "Beatrice", "Carlisle"};
        this.myFriendsList = new ArrayList<>();
        for(int i=0; i<startingNames.length; i++){
            Person p = new Person(startingNames[i], "address", "123456", "hello@gcu.edu", "testing.com");
            myFriendsList.add(p);
        }
    }

    public List<Person> getMyFriendsList() {
        return myFriendsList;
    }

    public void setMyFriendsList(List<Person> myFriendsList) {
        this.myFriendsList = myFriendsList;
    }
}
