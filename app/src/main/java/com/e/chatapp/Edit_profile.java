package com.e.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.e.chatapp.User_package.User;
import com.e.chatapp.ui.dashboard.DashboardFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class Edit_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        final SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        final String name = sharedPreferences.getString("name", null);
        EditText edit_username = (EditText) findViewById(R.id.edit_username);
        SpannableString s_name = new SpannableString(name);
        edit_username.setHint(s_name);

        final String email = sharedPreferences.getString("email", null);
        EditText edit_email = (EditText) findViewById(R.id.edit_email);
        SpannableString s_email = new SpannableString(email);
        edit_email.setHint(s_email);

        final String password = sharedPreferences.getString("password", null);
        EditText edit_password = (EditText) findViewById(R.id.edit_password);
        SpannableString s_password = new SpannableString(password);
        edit_password.setHint(s_password);

    }

    public void Save_edit(View view) {

        SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        EditText input_username = (EditText) findViewById(R.id.edit_username);
        String s_input_username = input_username.getText().toString();

        EditText input_email = (EditText) findViewById(R.id.edit_email);
        String s_input_email = input_email.getText().toString();

        EditText input_password = (EditText) findViewById(R.id.edit_password);
        String s_input_password = input_password.getText().toString();

        if(s_input_username.isEmpty()){
            s_input_username = sharedPreferences.getString("username", null);
        }
        String old_email = sharedPreferences.getString("email", null);
        if(s_input_email.isEmpty()){
            s_input_email = sharedPreferences.getString("email", null);
        }
        String old_password = sharedPreferences.getString("password", null);
        if(s_input_password.isEmpty()){
            s_input_password = sharedPreferences.getString("password", null);
        }

        String s_username = sharedPreferences.getString("username", null);

        final String birthday = sharedPreferences.getString("birthday", null);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        FirebaseUser userA = FirebaseAuth.getInstance().getCurrentUser();
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider
                .getCredential(old_email, old_password); // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        final String finalS_input_email = s_input_email;
        final String finalS_input_password = s_input_password;
        userA.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Edit_profile.this, "User re-authenticated.", Toast.LENGTH_SHORT).show();
                        //Now change your email address \\
                        //----------------Code for Changing Email Address----------\\
                        FirebaseUser userE = FirebaseAuth.getInstance().getCurrentUser();
                        userE.updateEmail(finalS_input_email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Edit_profile.this, "User email address updated.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        //----------------------------------------------------------\\
                        //----------------Code for Changing Password----------\\
                        FirebaseUser userP = FirebaseAuth.getInstance().getCurrentUser();
                        userP.updateEmail(finalS_input_password)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Edit_profile.this, "User password updated.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        //----------------------------------------------------------\\
                    }
                });

        final DatabaseReference users = database.getReference("Users");
        final User user = new User(s_input_username, s_input_email, birthday, s_input_password);
        final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.child(currentuser).setValue(user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
        editor.putString("username", s_input_username);
        editor.putString("email", s_input_email);
        editor.putString("password", s_input_password);
        editor.commit();


        Intent intent = new Intent(this, Main_page.class);
        startActivity(intent);
    }
}
