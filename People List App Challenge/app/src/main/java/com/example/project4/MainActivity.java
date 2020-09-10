package com.example.project4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_sortABC, btn_add;
    ListView lv_friendsList;

    PersonAdapter adapter;
    MyFriends myFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        btn_sortABC = findViewById(R.id.btn_sortABC);
        btn_add = findViewById(R.id.btn_add);
        lv_friendsList = findViewById(R.id.lv_friendsList);

        myFriends = ((MyApplication) this.getApplication()).getMyFriends();
        adapter = new PersonAdapter(MainActivity.this, myFriends);

        lv_friendsList.setAdapter(adapter);

        //listen for incoming messages
        Bundle incomingMessages = getIntent().getExtras();

        if(incomingMessages != null){
            //capture incoming data
            String name = incomingMessages.getString("name");
            String address = incomingMessages.getString("address");
            String phone = incomingMessages.getString("phone");
            String email = incomingMessages.getString("email");
            String url = incomingMessages.getString("url");
            int positionEdited = incomingMessages.getInt("edit");

            //create new person object
            Person p = new Person(name, address, phone, email, url);

            //add person to list and update adapter
            if(positionEdited > -1){
                myFriends.getMyFriendsList().remove(positionEdited);
            }
            myFriends.getMyFriendsList().add(p);

            adapter.notifyDataSetChanged();
        }

        btn_add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), NewPersonForm.class);
                startActivity(i);
            }
        } );

        btn_sortABC.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(myFriends.getMyFriendsList());
                adapter.notifyDataSetChanged();
            }
        } );

        lv_friendsList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editPerson(position);
            }
        } );
    }
    public void editPerson(int position){
        Intent i = new Intent(getApplicationContext(), NewPersonForm.class);

        //get contents of person at position
        Person p = myFriends.getMyFriendsList().get(position);

        i.putExtra("edit", position);
        i.putExtra("name", p.getName());
        i.putExtra("address", p.getAddress());
        i.putExtra("phone", p.getPhoneNumber());
        i.putExtra("email", p.getEmail());
        i.putExtra("url", p.getUrl());

        startActivity(i);
    }
}
