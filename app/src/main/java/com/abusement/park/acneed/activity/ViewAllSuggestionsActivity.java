package com.abusement.park.acneed.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.model.Suggestion;
import com.abusement.park.acneed.model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewAllSuggestionsActivity extends AppCompatActivity {

    private static final String TAG = "ViewAllSuggestions";

    private static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        TextView percentText;
        TextView totalVotesText;
        TextView subjectText;
        Suggestion suggestion;
        View.OnClickListener clickListener;

        public SuggestionViewHolder(View v) {
            super(v);
            percentText = (TextView) itemView.findViewById(R.id.Suggestions_percent_text);
            totalVotesText = (TextView) itemView.findViewById(R.id.Suggestions_votes_text);
            subjectText = (TextView) itemView.findViewById(R.id.Suggestions_subject_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (suggestion != null) {
                        Log.d(TAG, "CLICKED CHILD: " + suggestion.getId());
                    }
                }
            });
        }
    }

    private User user;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<Suggestion, SuggestionViewHolder> recyclerAdapter;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_suggestions);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            firebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerAdapter = new FirebaseRecyclerAdapter<Suggestion, SuggestionViewHolder>(Suggestion.class, R.layout
                .suggestions_list_item, SuggestionViewHolder.class, databaseReference.child("suggestions")) {
            @Override
            protected void populateViewHolder(SuggestionViewHolder viewHolder, final Suggestion suggestion, int position) {
                Log.d(TAG, "Adding suggestion: " + suggestion.getId());
                viewHolder.percentText.setText(suggestion.calculateUpVotePercent() + "% approve");
                viewHolder.totalVotesText.setText(suggestion.totalVotes() + " votes");
                viewHolder.subjectText.setText(suggestion.getTitle());
                viewHolder.suggestion = suggestion;
            }
        };
        recyclerView = (RecyclerView) findViewById(R.id.All_suggestions_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
    }

}
