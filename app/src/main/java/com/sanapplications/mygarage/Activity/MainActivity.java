package com.sanapplications.mygarage.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;

import com.sanapplications.mygarage.R;
import com.sanapplications.mygarage.Helper.UserDatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private UserDatabaseHelper mUserDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserDatabaseHelper = new UserDatabaseHelper(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


        if (mUserDatabaseHelper.isUserLoggedIn()) {
            // User is already logged in, redirect to dashboard
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        } else {
            // User is not logged in, redirect to login page
           // Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
            startActivity(intent);
            finish();
        }
            }
        },1500);



    }
}


