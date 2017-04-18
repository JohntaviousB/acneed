package com.abusement.park.acneed.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.model.Suggestion;

import java.util.List;

public class CustomSuggestionAdapter extends ArrayAdapter<Suggestion> {

    public CustomSuggestionAdapter(Context context, int resource, List<Suggestion> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View customView = convertView == null ? View.inflate(super.getContext(), R.layout.suggestions_list_item, parent)
                : convertView;
        Suggestion suggestion = super.getItem(position);
        TextView percentTextView = (TextView) customView.findViewById(R.id.Suggestions_percent_text);
        TextView totalVotesTextView = (TextView) customView.findViewById(R.id.Suggestions_votes_text);
        TextView subjectTextView = (TextView) customView.findViewById(R.id.Suggestions_subject_text);

        /* Todo: maybe use a bar instead of just a raw percentage */
        percentTextView.setText(suggestion.calculateUpVotePercent() + "% approve");
        totalVotesTextView.setText(suggestion.getTotalVotes() + " votes");
        subjectTextView.setText(suggestion.getTitle());

        return  customView;
    }
}
