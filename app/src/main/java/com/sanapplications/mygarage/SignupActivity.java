package com.sanapplications.mygarage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    private EditText mEmailEditText;
    private EditText mPasswordEditText;
    private Button mSignupButton;

    private UserDatabaseHelper mUserDatabaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mUserDatabaseHelper = new UserDatabaseHelper(this);

        mEmailEditText = findViewById(R.id.email_edit_text);
        mPasswordEditText = findViewById(R.id.password_edit_text);
        mSignupButton = findViewById(R.id.signup_button);

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                if (mUserDatabaseHelper.addUser(email, password)) {
                    Toast.makeText(SignupActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

