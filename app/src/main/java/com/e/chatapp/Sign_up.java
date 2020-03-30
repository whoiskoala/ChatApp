package com.e.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.e.chatapp.User_package.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Calendar;
import java.util.regex.Pattern;

public class Sign_up extends AppCompatActivity {

    private TextInputLayout input_username;
    private TextInputLayout input_email;
    private EditText choose_birthday;
    private TextInputLayout input_password;
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
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        input_username = (TextInputLayout) findViewById(R.id.Input_username);
        input_email = (TextInputLayout) findViewById(R.id.Input_email);
        choose_birthday = findViewById(R.id.birthday);
        input_password = (TextInputLayout) findViewById(R.id.Input_password);
        btn_Sign_up = findViewById(R.id.sign_up);


        choose_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog picker = new DatePickerDialog(Sign_up.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                choose_birthday.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
    }

    public void sign_up(View view) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference users = database.getReference("Users");
        boolean validation = validateName() && validateEmail() && validatePassword();
        if (validation) {
            firebaseAuth.createUserWithEmailAndPassword(input_email.getEditText().getText().toString().trim(),
                    input_password.getEditText().getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                final User user = new User(input_username.getEditText().getText().toString(),
                                        input_email.getEditText().getText().toString(),
                                        choose_birthday.getText().toString(),
                                        input_password.getEditText().getText().toString());
                                users.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.child(currentuser).exists())
                                            Toast.makeText(Sign_up.this, "Username already exist!", Toast.LENGTH_SHORT).show();
                                        else {
                                            users.child(currentuser).setValue(user);
                                            Toast.makeText(Sign_up.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Sign_up.this,Sign_in.class));
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        throw databaseError.toException();
                                    }
                                });
                            } else {
                                Toast.makeText(Sign_up.this, task.getException().getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
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

        private boolean validateEmail() {
        String emailInput = input_email.getEditText().getText().toString();

        if (emailInput.isEmpty()) {
            input_email.setError("Field can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            input_email.setError("Please enter a valid email address");
            return false;

        } else {
            input_email.setError(null);
            return true;
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
}
