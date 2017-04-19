package com.abusement.park.acneed.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.model.Suggestion;
import com.abusement.park.acneed.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

public class PostSuggestionActivity extends AppCompatActivity {

    private static final String TAG = "POST_SUGGESTION";

    private EditText subjectEditText;
    private EditText descriptionEditText;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_suggestion);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            logout(null);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        initViews();
        retrieveUser();
    }

    private void retrieveUser() {
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentUser = dataSnapshot.getValue(User.class);
                        assert currentUser != null;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        throw new RuntimeException(databaseError.getMessage(), databaseError.toException());
                    }
                });
    }

    private void initViews() {
        subjectEditText = (EditText) findViewById(R.id.Post_suggestion_title_edit_text);
        descriptionEditText = (EditText) findViewById(R.id.Post_suggestion_description_edit_text);
    }

    private boolean validateInput() {
        EditText invalidEditText = StringUtils.isBlank(subjectEditText.getText()) ? subjectEditText :
                                StringUtils.isBlank(descriptionEditText.getText()) ? descriptionEditText : null;
        if (invalidEditText != null) {
            invalidEditText.setError("Cannot be empty!");
            invalidEditText.requestFocus();
            return false;
        }
        return true;
    }

    public void submitSuggestion(View view) {
        if (validateInput()) {
            String subject = subjectEditText.getText().toString().trim();
            String description = descriptionEditText.getText().toString().trim();
            Suggestion suggestion = new Suggestion(currentUser.getUid(), subject, description);
            Log.d(TAG, "Posting suggestion: " + subject);
            ProgressDialog modal = displayProgressDialog();
            DatabaseReference dbReference  = databaseReference.child("suggestions").push();
            suggestion.setId(dbReference.getKey());
            dbReference.setValue(suggestion);
            currentUser.addSuggestion(suggestion);
            databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(currentUser);
            modal.dismiss();
            clearTextFields();
        } else {
            Log.d(TAG, "The input was invalid! Did not post");
        }
    }

    private void clearTextFields() {
        subjectEditText.getText().clear();
        descriptionEditText.getText().clear();
    }

    @NonNull
    private ProgressDialog displayProgressDialog() {
        ProgressDialog modal = new ProgressDialog(PostSuggestionActivity.this);
        modal.setMessage("Posting your suggestion");
        modal.setTitle("Please Wait");
        modal.show();
        return modal;
    }

    public void logout(View view) {
        firebaseAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void goHome(View view) {
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }

    public void goToIdeas(View view) {
        startActivity(new Intent(this, SuggestionsTabbedActivity.class));
        finish();
    }

}
