package com.example.spotify_app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform registration here
                if (registerUser()) {
                    // If registration successful, display a toast and redirect to LoginActivity
                    Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish(); // Close RegisterActivity
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean registerUser() {
        // Implement your registration logic here
        // For simplicity, just check if username and password are not empty
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password);
    }
}
