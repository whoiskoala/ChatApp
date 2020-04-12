package com.e.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_profile extends AppCompatActivity {

    private String user_ID, state, currentuser;

    private DatabaseReference UserRef, RequestRef, ChatRef;
    private FirebaseAuth Auth;

    private de.hdodenhof.circleimageview.CircleImageView user_portrait;
    private TextView user_name;
    private TextView user_email;
    private Button btn_request, btn_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users");
        RequestRef = FirebaseDatabase.getInstance().getReference().child("Request");
        ChatRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        Auth = FirebaseAuth.getInstance();

        user_ID = getIntent().getExtras().get("user_ID").toString();
        currentuser = Auth.getCurrentUser().getUid();

        user_portrait = (CircleImageView) findViewById(R.id.user_portrait);
        user_name = (TextView) findViewById(R.id.user_name);
        user_email = (TextView) findViewById(R.id.user_email);
        btn_request = (Button) findViewById(R.id.send_requirement);
        btn_message = (Button) findViewById(R.id.send_message);

        state = "first";



        RetrievePhoto();
    }
        private void RetrievePhoto() {

        UserRef.child(user_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))) {
                    String Portrait = dataSnapshot.child("image").getValue().toString();
                    String name = dataSnapshot.child("username").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    Picasso.get().load(Portrait).placeholder(R.drawable.icon_user).into(user_portrait);
                    user_name.setText(name);
                    user_email.setText(email);

                    RequestManagement();
                }
                else {
                    String name = dataSnapshot.child("username").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    user_name.setText(name);
                    user_email.setText(email);

                    RequestManagement();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void RequestManagement(){

        RequestRef.child(currentuser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user_ID)){
                            String request = dataSnapshot.child(user_ID).child("request_type").getValue().toString();

                            if (request.equals("sent")){
                                state = "sent_request";
                                btn_request.setText("Cancel Request");
                            }
                            else if (request.equals("received")){
                                state = "receive_request";
                                btn_request.setText("Accept Request");

                                btn_message.setVisibility(View.VISIBLE);
                                btn_message.setEnabled(true);

                                btn_message.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        cancelRequest();
                                    }
                                });
                            }
                        }
                        else {
                            ChatRef.child(currentuser)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(user_ID)){
                                                state = "friend";
                                                btn_request.setText("Remove friend");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        if (!currentuser.equals(user_ID)){
            btn_request.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    btn_request.setEnabled(false);

                    if (state.equals("first")){
                        sendRequest();
                    }
                    if (state.equals("send_request")){
                        cancelRequest();
                    }
                    if (state.equals("receive_request")){
                        acceptRequest();
                    }
                    if (state.equals("friend")){
                        removeFriend();
                    }
                }
            });
        }
        else {
            btn_request.setVisibility(View.INVISIBLE);
        }
    }

    private void removeFriend(){
        ChatRef.child(currentuser).child(user_ID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            ChatRef.child(user_ID).child(currentuser)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                btn_request.setEnabled(true);
                                                state = "first";
                                                btn_request.setText("Send Request");

                                                btn_message.setVisibility(View.INVISIBLE);
                                                btn_message.setEnabled(false);

                                            }
                                        }
                                    });
                        }
                    }

                });
    }

    private void acceptRequest(){
        ChatRef.child(currentuser).child(user_ID)
                .child("Contacts").setValue("Friends")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            ChatRef.child(user_ID).child(currentuser)
                                    .child("Contacts").setValue("Friends")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                RequestRef.child(currentuser).child(user_ID)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    RequestRef.child(user_ID).child(currentuser)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    btn_request.setEnabled(true);
                                                                                    state = "friend";
                                                                                    btn_request.setText("Remove friend");

                                                                                    btn_message.setVisibility(View.INVISIBLE);
                                                                                    btn_message.setEnabled(false);
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    private void cancelRequest(){
        RequestRef.child(currentuser).child(user_ID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            RequestRef.child(user_ID).child(currentuser)
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                btn_request.setEnabled(true);
                                                state = "first";
                                                btn_request.setText("Send Request");

                                                btn_message.setVisibility(View.INVISIBLE);
                                                btn_message.setEnabled(false);

                                            }
                                        }
                                    });
                        }
                    }

                });
    }

    private void sendRequest(){
        RequestRef.child(currentuser).child(user_ID)
                .child("request_type").setValue("sent")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            RequestRef.child(user_ID).child(currentuser)
                                    .child("request_type").setValue("received")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                btn_request.setEnabled(true);
                                                state = "send_request";
                                                btn_request.setText("Cancel Request");
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
}
