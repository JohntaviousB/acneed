package com.abusement.park.acneed.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.model.Image;
import com.abusement.park.acneed.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyJourney extends AppCompatActivity {

    private static final String TAG = "MY_JOURNEY";

    private User currentUser;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ArrayAdapter<Image> adapter;
    private ListView imageListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_journey);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (firebaseAuth.getCurrentUser() == null) {
            logout(null);
        }
        imageListView = (ListView) findViewById(R.id.My_journey_listview);
        retrieveUser();
    }

    private void retrieveUser() {
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentUser = dataSnapshot.getValue(User.class);
                        if (currentUser != null && adapter == null) {
                            adapter = new CustomAdapter<>(getApplicationContext(), R.layout.images_list_item,
                                    currentUser.getImages());
                            imageListView.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d(TAG, "Failed to read data", databaseError.toException());
                    }
                });
    }

    public void displayConfirmVideoDialog(View view) {
        new AlertDialog.Builder(view.getContext())
                .setTitle("Create Your Journey?")
                .setMessage("The selected ictures will be included in your journey.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createVideo();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void createVideo() {
    }

    public void logout(View view) {
        finish();
        firebaseAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void goHome(View view) {
        finish();
        startActivity(new Intent(this, WelcomeActivity.class));
    }
}
