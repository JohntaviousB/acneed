package com.abusement.park.acneed.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toolbar;

import com.abusement.park.acneed.R;
import com.google.firebase.auth.FirebaseAuth;

public class SuggestionsTabbedActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_suggestions);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setActionBar((Toolbar)findViewById(R.id.Suggestions_toolbar));
            getActionBar().setTitle("Suggestions");
        }
        TabHost tabHost = getTabHost();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_home:
                startActivity(new Intent(this, WelcomeActivity.class));
                finish();
                return true;
            case R.id.action_journey:
                startActivity(new Intent(this, MyJourneyActivity.class));
                finish();
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;
            case R.id.action_suggestions:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
