package com.abusement.park.acneed.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.model.Image;
import com.abusement.park.acneed.model.User;
import com.abusement.park.acneed.utils.ImageCompressor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.util.Date;

import static android.text.format.DateFormat.getMediumDateFormat;

public class WelcomeActivity extends AppCompatActivity {

    private static final String TAG = "WELCOME";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private User user;

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
        retrieveUser();
        currentReminderFrequency = frequencyEditText.getText().toString();
        String email = firebaseAuth.getCurrentUser().getEmail();
        String defaultUsername = email.substring(0, email.indexOf('@'));
        usernameText.setText(StringUtils.isBlank(firebaseAuth.getCurrentUser().getDisplayName())
                ? defaultUsername : firebaseAuth.getCurrentUser().getDisplayName());
    }

    private void retrieveUser() {
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {

                    /* Beware this is called asynchronously */
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            clearThumbnails();
                            int index = 0;
                            for (Image image : user.getImages()) {
                                addThumbnailToScrollView(Uri.parse(image.getUri()), index++);
                            }
                        }
                        try {
                            Log.d(TAG, "Retrieved user: " + new ObjectMapper().writerWithDefaultPrettyPrinter()
                                    .writeValueAsString(user));
                        } catch (JsonProcessingException e) {
                            Log.d(TAG, "Could not write the json for the user :(", e);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "Failed to read data", databaseError.toException());
                    }
                });
    }

    private void initializeViews() {
        usernameText = (TextView) findViewById(R.id.Home_username);
        frequencyEditText = (EditText) findViewById(R.id.Home_frequency_edit_text);
        editReminderSettingsTextView = (TextView) findViewById(R.id.Home_edit_reminder_edit_text);
        saveReminderSettingsTextView = (TextView) findViewById(R.id.Home_save_reminder_changes);
        thumbnailsLayout = (LinearLayout) findViewById(R.id.Home_scroll_view_layout);
    }

    private void clearThumbnails() {
        thumbnailsLayout.removeAllViews();
    }

    public void displayEditProfileDialog(View view) {
        // TODO: 3/25/2017
    }

    public void addImageToMyJourney(View view) {
        Intent chooseImage;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            chooseImage = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            chooseImage.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        } else {
            chooseImage = new Intent(Intent.ACTION_GET_CONTENT);
        }
        chooseImage.setType("image/*").addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(chooseImage, "Select Image to Add to Journey"),
                CHOOSE_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "Choose Image Request completed with data " + data.getData());
            int takeFlags = data.getFlags();
            takeFlags &= Intent.FLAG_GRANT_READ_URI_PERMISSION;
            getContentResolver().takePersistableUriPermission(data.getData(), takeFlags);
            user.addImage(new Image(data.getData().toString(), new Date()));

            //update the database to account for the new image
            databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(user);
        }
    }

    private void addThumbnailToScrollView(Uri imageUri, final int index) {
        ImageView newThumbnail = new ImageView(this);
        Log.d(TAG, "Attempting to compress Image " + imageUri);
        try {
            newThumbnail.setImageBitmap(ImageCompressor.compressImageToThumbnail(imageUri, getContentResolver(),
                    THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT));
        } catch (FileNotFoundException e) {
            Log.d(TAG, "Error compressing image", e);
            Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_LONG).show();
        }
        Log.d(TAG, "Image compression succeeded. Setting layout and adding to layout");
        newThumbnail.setLayoutParams(new LinearLayout.LayoutParams(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT));
        newThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayDialog(v, index);
            }
        });
        thumbnailsLayout.addView(newThumbnail);
    }

    private void displayDialog(View v, int index) {
        final Dialog imageDialog = new Dialog(this);
        imageDialog.setContentView(R.layout.full_image_dialog);
        imageDialog.show();

        final ImageView imgView = (ImageView) imageDialog.findViewById(R.id.Full_image_image_view);
        imgView.setImageURI(Uri.parse(user.getImages().get(index).getUri()));
        final ImageButton leftButton = (ImageButton) imageDialog.findViewById(R.id.Full_image_left_button);
        final ImageButton rightButton = (ImageButton) imageDialog.findViewById(R.id.Full_image_right_button);
        final TextView dateText = (TextView) imageDialog.findViewById(R.id.Full_image_date_text);
        dateText.setText(getMediumDateFormat(getApplicationContext()).format(user.getImages().get(index).getUploadDate()));

        enableButtonsBasedOnIndex(user.getImages().size(), index, leftButton, rightButton);
        final IntegerWrapper wrappedIndex = new IntegerWrapper(index); // workaround the "effectively final" requirement

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgView.setImageURI(Uri.parse(user.getImages().get(--wrappedIndex.value).getUri()));
                dateText.setText(getMediumDateFormat(getApplicationContext()).format(user.getImages().get
                        (wrappedIndex.value).getUploadDate()));
                enableButtonsBasedOnIndex(user.getImages().size(), wrappedIndex.value, leftButton, rightButton);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgView.setImageURI(Uri.parse(user.getImages().get(++wrappedIndex.value).getUri()));
                dateText.setText(getMediumDateFormat(getApplicationContext()).format(user.getImages().get
                        (wrappedIndex.value).getUploadDate()));
                enableButtonsBasedOnIndex(user.getImages().size(), wrappedIndex.value, leftButton, rightButton);
            }
        });

        Button closeButton = (Button) imageDialog.findViewById(R.id.Full_image_close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgView.setImageURI(null);
                imageDialog.dismiss();
            }
        });

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

    private void enableButtonsBasedOnIndex(int imagesSize, int index, ImageButton left, ImageButton right) {
        if (index == 0) {
            left.setEnabled(false);
        }
        if (index == imagesSize - 1) {
            right.setEnabled(false);
        }
        if (index >= 0 && index < imagesSize - 1) {
            right.setEnabled(true);
        }
        if (index > 0 && index <= imagesSize - 1) {
            left.setEnabled(true);
        }
    }

    private static class IntegerWrapper {
        public int value;

        public IntegerWrapper(int value) {
            this.value = value;
        }
    }

}
