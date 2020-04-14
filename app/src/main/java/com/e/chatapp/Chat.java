package com.e.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.e.chatapp.User_package.Message_adapter;
import com.e.chatapp.User_package.Messages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    private String receiver_ID, receiver_Name, receiver_Image, sendID;

    private TextView userName, userLastSeen;
    private CircleImageView userImage;

    private Toolbar ChatBar;
    private FirebaseAuth Auth;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    private Button btn_send;
    private EditText input_text;

    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private Message_adapter message_adapter;
    private RecyclerView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Auth = FirebaseAuth.getInstance();
        sendID = Auth.getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        receiver_ID = getIntent().getExtras().get("user_ID").toString();
        receiver_Name = getIntent().getExtras().get("user_Name").toString();
        receiver_Image = getIntent().getExtras().get("user_Image").toString();

//        Toast.makeText(Chat.this, receiver_ID, Toast.LENGTH_LONG).show();
//        Toast.makeText(Chat.this, receiver_Name, Toast.LENGTH_LONG).show();

        FriendInfoIntial();

        userName.setText(receiver_Name);
        Picasso.get().load(receiver_Image).placeholder(R.drawable.icon_user).into(userImage);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                message_send();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        rootRef.child("Messages").child(sendID).child(receiver_ID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Messages messages = dataSnapshot.getValue(Messages.class);

                        messagesList.add(messages);

                        message_adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void message_send(){
        String Text = input_text.getText().toString();

        if (TextUtils.isEmpty(Text)){
            Toast.makeText(Chat.this, "message is empty !", Toast.LENGTH_SHORT).show();
        }
        else {
            String senderRef = "Messages/" + sendID + "/" + receiver_ID;
            String receiverRef = "Messages/" + receiver_ID + "/" + sendID;

            DatabaseReference messagekeyRef = rootRef.child("Messages")
                    .child(sendID).child(receiver_ID).push();

            String messagePushID = messagekeyRef.getKey();

            Map messageTextBody = new HashMap();
            messageTextBody.put("message", Text);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", sendID);

            Map textDetial = new HashMap();
            textDetial.put(senderRef + "/" + messagePushID , messageTextBody);
            textDetial.put(receiverRef + "/" + messagePushID , messageTextBody);

            rootRef.updateChildren(textDetial).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Chat.this, "message send successful !", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Chat.this, "message error !", Toast.LENGTH_SHORT).show();
                    }
                    input_text.setText("");
                }
            });
        }
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

        btn_send = (Button) findViewById(R.id.btn_send_message);
        input_text = (EditText) findViewById(R.id.input_message);

        message_adapter = new Message_adapter(messagesList);
        messageList = (RecyclerView) findViewById(R.id.message_recycler_view);
        linearLayoutManager = new LinearLayoutManager(this);
        messageList.setLayoutManager(linearLayoutManager);
        messageList.setAdapter(message_adapter);
    }
}
