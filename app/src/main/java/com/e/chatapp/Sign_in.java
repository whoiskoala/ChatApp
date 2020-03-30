package com.e.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.e.chatapp.User_package.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class Sign_in extends AppCompatActivity {

    private TextInputLayout input_username;
    private TextInputLayout input_password;
    private Button btn_Sign_in;
    private Button btn_Sign_up;
    private FirebaseAuth firebaseAuth;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-zA-Z])" +
//                    "(?=.* [@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{8,}" +
                    "$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();
        input_username = (TextInputLayout) findViewById(R.id.Input_username);
        input_password = (TextInputLayout) findViewById(R.id.Input_password);
        btn_Sign_in = findViewById(R.id.sign_in);
        btn_Sign_up = findViewById(R.id.sign_up);

        btn_Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sign_in.this, Sign_up.class));
            }
        });
    }

    public void sign_in(View view) {

        final String useremail = input_username.getEditText().getText().toString();
        final String password = input_password.getEditText().getText().toString();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("Users");
        boolean validation = validateName() && validatePassword();

        if (validation) {
            users.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {                               //Customise validation
//                    if (dataSnapshot.child(currentuser).exists()) {
                        if (!useremail.isEmpty()) {
//                            if (login.getPassword().equals(password)) {
                                firebaseAuth.signInWithEmailAndPassword(useremail, password)            //firebase auto verification
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                    final User login = dataSnapshot.child(currentuser).getValue(User.class);         //store value by User.java class
                                                    String username = login.getUsername();
                                                    SharedPreferences sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putString("username", username);
                                                    String birthday = login.getBirthday();
                                                    editor.putString("birthday", birthday);
                                                    editor.putString("password", password);
                                                    editor.putString("email", useremail);
                                                    editor.commit();
                                                    startActivity(new Intent(Sign_in.this, Main_page.class));
                                                    Toast.makeText(Sign_in.this, "Sign in successful!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Sign_in.this, task.getException().getMessage(),
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
//                            }
//                        else {
//                                Toast.makeText(Sign_in.this, "Wrong password !", Toast.LENGTH_SHORT).show();
//                            }
                        }
                    }
//                    else
//                        Toast.makeText(Sign_in.this, "User doesn't exist !", Toast.LENGTH_SHORT).show();
//                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    throw databaseError.toException();
                }
            });
        }
    }

    private boolean validateName() {
        String nameInput = input_username.getEditText().getText().toString();

        if (nameInput.isEmpty()) {
            input_username.setError("Field can't be empty");
            return false;
        } else if (nameInput.length() > 20) {
            input_username.setError("Username to long");
            return false;
        } else {
            input_username.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = input_password.getEditText().getText().toString();

        if (passwordInput.isEmpty()) {
            input_password.setError("Field can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            input_password.setError("Password at least 8 character and no space");
            return false;
        } else {
            input_password.setError(null);
            return true;
        }
    }
}
