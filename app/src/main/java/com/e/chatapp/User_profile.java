package com.e.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class User_profile extends AppCompatActivity {

    private String user_ID;

    private DatabaseReference RootRef;

    private de.hdodenhof.circleimageview.CircleImageView user_portrait;
    private TextView user_name;
    private TextView user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        RootRef = FirebaseDatabase.getInstance().getReference().child("Users");
        user_ID = getIntent().getExtras().get("user_ID").toString();

        user_portrait = (CircleImageView) findViewById(R.id.user_portrait);
        user_name = (TextView) findViewById(R.id.user_name);
        user_email = (TextView) findViewById(R.id.user_email);

        RetrievePhoto();
    }
        private void RetrievePhoto() {

        RootRef.child(user_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.exists()) && (dataSnapshot.hasChild("image"))) {
                    String Portrait = dataSnapshot.child("image").getValue().toString();
                    String name = dataSnapshot.child("username").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();

                    Picasso.get().load(Portrait).placeholder(R.drawable.icon_user).into(user_portrait);
                    user_name.setText(name);
                    user_email.setText(email);
                }
                else {
                    String name = dataSnapshot.child("username").getValue().toString();
                    String email = dataSnapshot.child("email").getValue().toString();
                    user_name.setText(name);
                    user_email.setText(email);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
