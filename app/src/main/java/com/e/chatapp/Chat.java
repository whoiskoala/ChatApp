package com.e.chatapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private String receiver_ID, receiver_Name, receiver_Image;

    private TextView userName, userLastSeen;
    private CircleImageView userImage;

    private Toolbar ChatBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        receiver_ID = getIntent().getExtras().get("user_ID").toString();
        receiver_Name = getIntent().getExtras().get("user_Name").toString();
        receiver_Image = getIntent().getExtras().get("user_Image").toString();

//        Toast.makeText(Chat.this, receiver_ID, Toast.LENGTH_LONG).show();
//        Toast.makeText(Chat.this, receiver_Name, Toast.LENGTH_LONG).show();

        FriendInfoIntial();

        userName.setText(receiver_Name);
        Picasso.get().load(receiver_Image).placeholder(R.drawable.icon_user).into(userImage);
    }

    private void FriendInfoIntial(){

        ChatBar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(ChatBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.user_chat_bar, null);
        actionBar.setCustomView(actionBarView);

        userImage = (CircleImageView) findViewById(R.id.friend_chat_portrait);
        userName = (TextView) findViewById(R.id.friend_chat_name);
        userLastSeen = (TextView) findViewById(R.id.friend_last_seen);
    }
}
