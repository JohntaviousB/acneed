package com.abusement.park.acneed.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.abusement.park.acneed.R;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.lang3.StringUtils;

public class WelcomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private EditText frequencyEditText;
    private Spinner timeIntervalSpinner;
    private TextView editReminderSettingsTextView;
    private TextView saveReminderSettingsTextView;
    private TextView usernameText;

    private String currentReminderFrequency;
    private int currentReminderInterval;

    private static final String[] TIME_INTERVALS = {"days", "weeks", "months"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        initializeViews();
        currentReminderFrequency = frequencyEditText.getText().toString();
        currentReminderInterval = timeIntervalSpinner.getSelectedItemPosition();
        String email = firebaseAuth.getCurrentUser().getEmail();
        String defaultUsername = email.substring(0, email.indexOf('@'));
        usernameText.setText(StringUtils.isBlank(firebaseAuth.getCurrentUser().getDisplayName())
                ? defaultUsername : firebaseAuth.getCurrentUser().getDisplayName());
    }

    private void initializeViews() {
        usernameText = (TextView) findViewById(R.id.Home_username);
        frequencyEditText = (EditText) findViewById(R.id.Home_frequency_edit_text);
        timeIntervalSpinner = (Spinner) findViewById(R.id.Home_interval_spinner);
        editReminderSettingsTextView = (TextView) findViewById(R.id.Home_edit_reminder_edit_text);
        saveReminderSettingsTextView = (TextView) findViewById(R.id.Home_save_reminder_changes);

        timeIntervalSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout
                .simple_spinner_item, TIME_INTERVALS));
        timeIntervalSpinner.setEnabled(false);
    }

    public void displayEditProfileDialog(View view) {
        // TODO: 3/25/2017
    }

    public void addImageToMyJourney(View view) {

    }

    public void editReminderSettings(View view) {
        // toggle the EditText b/t editable and uneditable for each click
        frequencyEditText.setEnabled(!frequencyEditText.isEnabled());
        // do the same for the Spinner
        timeIntervalSpinner.setEnabled(!timeIntervalSpinner.isEnabled());

        // hide or unhide the Save button for each click and reset to the defaults if needed
        // also remove the error if 'cancel' was clicked
        if (frequencyEditText.isEnabled()) {
            saveReminderSettingsTextView.setVisibility(View.VISIBLE);
        } else {
            saveReminderSettingsTextView.setVisibility(View.GONE);
            frequencyEditText.setText(currentReminderFrequency);
            frequencyEditText.setError(null);
            timeIntervalSpinner.setSelection(currentReminderInterval);
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
            timeIntervalSpinner.setEnabled(false);
            editReminderSettingsTextView.setText("edit");
            // todo need to update the database with new settings
            // probably use an AsyncTask

        }
    }

}
