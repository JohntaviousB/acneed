package com.abusement.park.acneed.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.model.Suggestion;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.abusement.park.acneed.activity.ViewAllSuggestionsActivity.*;

public class ViewMySuggestionsActivity extends AppCompatActivity {

    private static final String TAG = "My_Suggestions";

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Suggestion, SuggestionViewHolder> recyclerAdapter;
    private RecyclerView recyclerView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_suggestions);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            logout(null);
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) findViewById(R.id.My_suggestions_recycler_view);
        recyclerAdapter = new FirebaseRecyclerAdapter<Suggestion, SuggestionViewHolder>(Suggestion.class, R.layout
                .suggestions_list_item, SuggestionViewHolder.class, databaseReference.child("users").child(firebaseAuth
                .getCurrentUser().getUid()).child("suggestions")) {
            @Override
            protected void populateViewHolder(SuggestionViewHolder viewHolder, final Suggestion suggestion, int position) {
                Log.d(TAG, "Adding suggestion: " + suggestion.getId());
                viewHolder.percentText.setText(suggestion.calculateUpVotePercent() + "% approve");
                viewHolder.totalVotesText.setText(suggestion.totalVotes() + " votes");
                viewHolder.subjectText.setText(suggestion.getTitle());
                viewHolder.suggestion = suggestion;
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void goToIdeas(View view) {
        startActivity(new Intent(this, SuggestionsTabbedActivity.class));
        finish();
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
}
