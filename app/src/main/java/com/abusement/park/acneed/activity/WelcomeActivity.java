package com.abusement.park.acneed.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.utils.ImageCompressor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WELCOME";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private EditText frequencyEditText;
    private TextView editReminderSettingsTextView;
    private TextView saveReminderSettingsTextView;
    private TextView usernameText;
    private LinearLayout thumbnailsLayout;

    private String currentReminderFrequency;

    private static final int CHOOSE_IMAGE_REQUEST_CODE = 1;
    private static final int THUMBNAIL_HEIGHT = 100;
    private static final int THUMBNAIL_WIDTH = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (firebaseAuth.getCurrentUser() == null) {
            logout(null);
        }
        initializeViews();
        currentReminderFrequency = frequencyEditText.getText().toString();
        String email = firebaseAuth.getCurrentUser().getEmail();
        String defaultUsername = email.substring(0, email.indexOf('@'));
        usernameText.setText(StringUtils.isBlank(firebaseAuth.getCurrentUser().getDisplayName())
                ? defaultUsername : firebaseAuth.getCurrentUser().getDisplayName());
    }

    private void initializeViews() {
        usernameText = (TextView) findViewById(R.id.Home_username);
        frequencyEditText = (EditText) findViewById(R.id.Home_frequency_edit_text);
        editReminderSettingsTextView = (TextView) findViewById(R.id.Home_edit_reminder_edit_text);
        saveReminderSettingsTextView = (TextView) findViewById(R.id.Home_save_reminder_changes);
        thumbnailsLayout = (LinearLayout) findViewById(R.id.Home_scroll_view_layout);
    }

    public void displayEditProfileDialog(View view) {
        // TODO: 3/25/2017
    }

    public void addImageToMyJourney(View view) {
        Intent chooseImage = new Intent().setType("image/*").setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(chooseImage, "Select Image to Add to " +
                "Journey"), CHOOSE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "Choose Image Request completed with data " + data.getData());
            ImageView newThumbnail = new ImageView(this);
            Log.d(TAG, "Attempting to compress Image");
            try {
                newThumbnail.setImageBitmap(ImageCompressor.compressImageToThumbnail(data.getData(),
                        getContentResolver(), THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT));
            } catch (FileNotFoundException e) {
                Log.d(TAG, "Error compressing image", e);
                Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_LONG).show();
            }
            Log.d(TAG, "Image compression succeeded. Setting layout and adding to layout");
            newThumbnail.setLayoutParams(new LinearLayout.LayoutParams(THUMBNAIL_WIDTH,
                    THUMBNAIL_HEIGHT));
            thumbnailsLayout.addView(newThumbnail);
        }
    }

    public void editReminderSettings(View view) {
        // toggle the EditText b/t editable and uneditable for each click
        frequencyEditText.setEnabled(!frequencyEditText.isEnabled());

        // hide or unhide the Save button for each click and reset to the defaults if needed
        // also remove the error if 'cancel' was clicked
        if (frequencyEditText.isEnabled()) {
            saveReminderSettingsTextView.setVisibility(View.VISIBLE);
        } else {
            saveReminderSettingsTextView.setVisibility(View.GONE);
            frequencyEditText.setText(currentReminderFrequency);
            frequencyEditText.setError(null);
        }

        // toggle the TextView's value between 'cancel' and 'edit' for each click
        editReminderSettingsTextView.setText("cancel".equals(editReminderSettingsTextView.getText())
                ? "edit" : "cancel");
    }

    public void saveReminderSettings(View view) {
        String newFrequency = frequencyEditText.getText().toString();
        if (StringUtils.isBlank(newFrequency)) {
            frequencyEditText.setError("Alert frequency cannot be blank");
            frequencyEditText.requestFocus();
        } else {
            saveReminderSettingsTextView.setVisibility(View.GONE);
            frequencyEditText.setEnabled(false);
            editReminderSettingsTextView.setText("edit");
            // todo need to update the database with new settings
            // probably use an AsyncTask
        }
    }

    public void logout(View view) {
        finish();
        firebaseAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void goHome(View view) {
        //no need to do anything since we're already home
    }

}
