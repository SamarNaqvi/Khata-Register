package com.example.khataregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    android.widget.Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.email);
        password=findViewById(R.id.password);
        submitButton = (android.widget.Button)findViewById(R.id.submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();
                if(userEmail.length()==0)
                {
                    email.setError("Email cannot be empty");
                }
                if(userPassword.length()==0)
                {
                    password.setError("Password cannot be empty");
                }

                User.getUser(LoginActivity.this, userEmail, userPassword);

            }
        });
    }


}