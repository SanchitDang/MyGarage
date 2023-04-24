package com.sanapplications.mygarage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private UserDatabaseHelper mUserDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            Intent intent = new Intent(MainActivity.this, DashBoardActivity.class);
            startActivity(intent);
//
//        mUserDatabaseHelper = new UserDatabaseHelper(this);
//
//        if (mUserDatabaseHelper.isUserLoggedIn()) {
//            // User is already logged in, redirect to dashboard
//            Intent intent = new Intent(MainActivity.this, DashBoardActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            // User is not logged in, redirect to login page
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
    }
}


