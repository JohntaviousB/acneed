package com.abusement.park.acneed.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;

public class LoginActivity extends AppCompatActivity {

    private String TAG = "LOGIN";

    private EditText userNameEditText;
    private EditText passwordEditText;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        if (firebaseAuth.getCurrentUser() != null) {
            Log.i(TAG, "User is still logged in from previous session.");
            startActivity(new Intent(this, WelcomeActivity.class));
        }
        userNameEditText = (EditText) findViewById(R.id.Login_email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.Login_password_edit_text);
        progressDialog = new ProgressDialog(this);
    }

    public void login(View loginButton) {
        Log.d(TAG, "Login clicked");
        if (validateTextFields()) {
            String email = userNameEditText.getText().toString().trim();
            String password = userNameEditText.getText().toString().trim();
            progressDialog.setMessage("Logging in...");
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid login credentials!", Toast
                                .LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void register(View registerButton) {
        Log.d(TAG, "Registration clicked");
        if (validateTextFields()) {
            final String email = userNameEditText.getText().toString().trim();
            final String password = userNameEditText.getText().toString().trim();
            progressDialog.setMessage("Registering...");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new
                                                                          OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(Task<AuthResult> task) {
                   progressDialog.dismiss();
                   if (task.isSuccessful()) {
                       Log.i(TAG, "Registration successful. Creating new user.");
                       progressDialog.setMessage("Uploading credentials...");
                       progressDialog.show();
                       User newUser = new User(email, password, task.getResult().getUser().getUid());
                       databaseReference.child("users").child(task.getResult().getUser().getUid()).setValue(newUser);
                       progressDialog.dismiss();
                       finish();
                       startActivity(new Intent(LoginActivity.this, WelcomeActivity.class));
                   } else {
                       Log.i(TAG, "Registration unsuccessful.");
                       if (task.getException() != null) {
                           Log.d(TAG, "Registration Exception: " + task.getException().getMessage());
                       }
                       Toast.makeText(LoginActivity.this, "Could not register with these " +
                               "credentials", Toast
                               .LENGTH_LONG).show();
                   }
               }
           });
        }
    }

    private boolean validateTextFields() {
        Log.i(TAG, "Validating text fields");
        for (EditText editText : new EditText[]{userNameEditText, passwordEditText}) {
            if (StringUtils.isBlank(editText.getText().toString())) {
                editText.setError("Field is required");
                editText.requestFocus();
                return false;
            }
        }
        return true;
    }
}
