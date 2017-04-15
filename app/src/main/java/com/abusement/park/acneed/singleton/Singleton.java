package com.abusement.park.acneed.singleton;

import com.abusement.park.acneed.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Singleton {

    private static Singleton instance;

    private static User currentUser;
    private static FirebaseAuth firebaseAuth;
    private static DatabaseReference databaseReference;

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
            firebaseAuth = FirebaseAuth.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return instance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean checkUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public void updateUser(User user) {
        databaseReference.child("users").child(user.getUid()).setValue(user);
    }

    }
