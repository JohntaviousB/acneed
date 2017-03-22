package com.abusement.park.acneed.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.abusement.park.acneed.R;

import org.apache.commons.lang3.StringUtils;

public class WelcomeActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        String username = getIntent().getExtras().getString("user");
        if (StringUtils.isNotBlank(username)) {
            TextView usernameText = (TextView) findViewById(R.id
                    .Welcome_username);
            usernameText.setText("Welcome, " + username);
        }
    }

}
