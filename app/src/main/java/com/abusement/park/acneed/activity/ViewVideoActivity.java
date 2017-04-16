package com.abusement.park.acneed.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static android.text.format.DateFormat.getMediumDateFormat;

public class ViewVideoActivity extends AppCompatActivity {

    private User user;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_dialog);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                String userString = extras.getString("user");
                ObjectMapper mapper = new ObjectMapper();
                user = mapper.readValue(userString, User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int index = getIntent().getIntExtra("selected_index", 0);
            mediaController = new MediaController(this);
            displayVideoDialog(index);
        }
    }

    private void displayVideoDialog(int index) {
        final VideoView videoView = (VideoView) findViewById(R.id.full_video_view);
        videoView.setVideoURI(Uri.parse(user.getVideos().get(index).getUri()));
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        final ImageButton leftButton = (ImageButton) findViewById(R.id.full_video_left_button);
        final ImageButton rightButton = (ImageButton) findViewById(R.id.full_video_right_button);
        final TextView dateText = (TextView) findViewById(R.id.full_video_date_text);
        dateText.setText(getMediumDateFormat(getApplicationContext()).format(user.getVideos().get(index).getUploadDate()));
        WelcomeActivity.enableButtonsBasedOnIndex(user.getVideos().size(), index, leftButton, rightButton);
        final IntegerWrapper wrappedIndex = new IntegerWrapper(index);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setVideoURI(Uri.parse(user.getVideos().get(--wrappedIndex.value).getUri()));
                dateText.setText(getMediumDateFormat(getApplicationContext()).format(user.getVideos().get
                        (wrappedIndex.value).getUploadDate()));
                WelcomeActivity.enableButtonsBasedOnIndex(user.getVideos().size(), wrappedIndex.value, leftButton,
                        rightButton);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setVideoURI(Uri.parse(user.getVideos().get(++wrappedIndex.value).getUri()));
                dateText.setText(getMediumDateFormat(getApplicationContext()).format(user.getVideos().get
                        (wrappedIndex.value).getUploadDate()));
                WelcomeActivity.enableButtonsBasedOnIndex(user.getVideos().size(), wrappedIndex.value, leftButton,
                        rightButton);
            }
        });

        Button closeButton = (Button)findViewById(R.id.full_video_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.setVideoURI(null);
                startActivity(new Intent(ViewVideoActivity.this, WelcomeActivity.class));
            }
        });
    }

    private static class IntegerWrapper {
        public int value;

        public IntegerWrapper(int value) {
            this.value = value;
        }
    }
}
