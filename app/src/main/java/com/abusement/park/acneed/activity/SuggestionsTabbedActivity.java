package com.abusement.park.acneed.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.abusement.park.acneed.R;

public class SuggestionsTabbedActivity extends TabActivity {

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_suggestions);
        tabHost = getTabHost();

        tabHost.setup();

        TabHost.TabSpec spec = tabHost.newTabSpec("Tab One");
        spec.setContent(new Intent(this, ViewAllSuggestionsActivity.class));
        spec.setIndicator("View All");
        tabHost.addTab(spec);

        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab Two");
        spec2.setContent(new Intent(this, ViewMySuggestionsActivity.class));
        spec2.setIndicator("View Mine");
        tabHost.addTab(spec2);

    }
}
