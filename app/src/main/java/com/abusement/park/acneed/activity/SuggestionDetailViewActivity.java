package com.abusement.park.acneed.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.model.Suggestion;
import com.abusement.park.acneed.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

public class SuggestionDetailViewActivity extends AppCompatActivity {

    private static final String TAG = "SuggestionDetail";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private User currentUser;
    private String suggestionId;

    private ImageButton upVoteButton;
    private ImageButton downVoteButton;
    private ProgressBar voteBar;
    private TextView usernameTextView;
    private TextView subjectTextView;
    private TextView totalVotesTextView;
    private EditText detailsEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_detail_view);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            logout(null);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        suggestionId = getIntent().getStringExtra("suggestionId");
        retrieveUser();
        initViews();
        populateViewsFromSuggestion();

    }

    private void retrieveUser() {
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "User retrieval cancelled");
            }
        });
    }

    public void upVote(View view) {
        databaseReference.child("suggestions").child(suggestionId).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Suggestion suggestion = mutableData.getValue(Suggestion.class);
                if (suggestion == null) {
                    Log.d(TAG, "TRANSACTION RECEIVED NULL SUGGESTION");
                    return Transaction.success(mutableData);
                }
                boolean result = suggestion.addUpVoter(currentUser.getUid());
                if (!result) {
                    suggestion.removeUpVoter(currentUser.getUid());
                }
                Log.d(TAG, "USER ADDED TO UPVOTES: " + result);
                mutableData.setValue(suggestion);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "UPVOTE TRANSACTION COMPLETE " + databaseError);
            }
        });
    }

    public void downVote(View view) {
        databaseReference.child("suggestions").child(suggestionId).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Suggestion suggestion = mutableData.getValue(Suggestion.class);
                if (suggestion == null) {
                    Log.d(TAG, "TRANSACTION RECEIVED NULL SUGGESTION");
                    return Transaction.success(mutableData);
                }
                boolean result = suggestion.addDownVoter(currentUser.getUid());
                if (!result) {
                    suggestion.removeDownVoter(currentUser.getUid());
                }
                Log.d(TAG, "USER ADDED TO DOWNVOTES: " + result);
                mutableData.setValue(suggestion);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d(TAG, "DOWNVOTE TRANSACTION COMPLETE " + databaseError);
            }
        });
    }

    private void initViews() {
        upVoteButton = (ImageButton) findViewById(R.id.Suggestion_upvote_button);
        downVoteButton = (ImageButton) findViewById(R.id.Suggestion_downvote_button);
        voteBar = (ProgressBar) findViewById(R.id.Suggestion_details_bar);
        voteBar.setMax(100);
        usernameTextView = (TextView) findViewById(R.id.Suggestion_poster_username_text_view);
        subjectTextView = (TextView) findViewById(R.id.Suggestion_details_subject_text_view);
        totalVotesTextView = (TextView) findViewById(R.id.Suggestion_details_total_votes);
        detailsEditText = (EditText) findViewById(R.id.Suggestion_details_details);
        detailsEditText.setEnabled(false);
    }

    private void populateViewsFromSuggestion() {
        databaseReference.child("suggestions").child(suggestionId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Suggestion suggestion = dataSnapshot.getValue(Suggestion.class);
                if (suggestion != null) {
                    voteBar.setProgress(suggestion.calculateUpVotePercent());
                    totalVotesTextView.setText(suggestion.totalVotes() + " votes");
                    usernameTextView.setText(suggestion.getUser());
                    subjectTextView.setText(suggestion.getTitle());
                    detailsEditText.setText(suggestion.getDetails());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Suggestion retrieval cancelled");
            }
        });
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
