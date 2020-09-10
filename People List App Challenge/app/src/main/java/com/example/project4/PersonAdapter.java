package com.example.project4;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonAdapter extends BaseAdapter {

    Activity mActivity;
    MyFriends myFriends;

    public PersonAdapter(Activity mActivity, MyFriends myFriends) {
        this.mActivity = mActivity;
        this.myFriends = myFriends;
    }

    @Override
    public int getCount() {
        return myFriends.getMyFriendsList().size();
    }

    @Override
    public Person getItem(int position) {
        return myFriends.getMyFriendsList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View onePersonLine;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        onePersonLine = inflater.inflate(R.layout.person_one_line, parent, false);

        TextView tv_personName = onePersonLine.findViewById(R.id.tv_personName);
        TextView tv_personAddress = onePersonLine.findViewById(R.id.tv_personAddress);
        TextView tv_personPhone = onePersonLine.findViewById(R.id.tv_personPhone);
        TextView tv_personEmail = onePersonLine.findViewById(R.id.tv_personEmail);
        TextView tv_personURL = onePersonLine.findViewById(R.id.tv_personURL);

        Person p = this.getItem(position);

        tv_personName.setText(p.getName());
        tv_personAddress.setText(p.getAddress());
        tv_personPhone.setText(p.getPhoneNumber());
        tv_personEmail.setText(p.getEmail());
        tv_personURL.setText(p.getUrl());

        return onePersonLine;
    }
}
