package com.example.todoapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.example.todoapp.Models.User;
import com.example.todoapp.R;
import com.example.todoapp.Repositories.UserRepository;

public class SignUpActivity extends BasedActivity {

    private EditText sUsername, sPassword;
    private UserRepository userRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sUsername = findViewById(R.id.sUsername);
        sPassword = findViewById(R.id.sPassword);
        Button btnRegister = findViewById(R.id.btnRegister);
        userRepository = new UserRepository(this);
        ImageButton btnBack = findViewById(R.id.btnBack);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void registerUser() {

        String username = sUsername.getText().toString().trim();
        String password = sPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Username and password cannot be empty", Toast.LENGTH_SHORT).show();
        }

        if (userRepository.addUser(new User(username, password))) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
        }

    }
}
