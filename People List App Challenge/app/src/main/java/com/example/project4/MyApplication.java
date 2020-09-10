package com.example.project4;

import android.app.Application;

public class MyApplication extends Application {
    private MyFriends myFriends = new MyFriends();

    public MyFriends getMyFriends() {
        return myFriends;
    }

    public void setMyFriends(MyFriends mFriends) {
        this.myFriends = mFriends;
    }
}
