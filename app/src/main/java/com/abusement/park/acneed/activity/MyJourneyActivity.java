package com.abusement.park.acneed.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.model.Image;
import com.abusement.park.acneed.model.User;
import com.abusement.park.acneed.model.Video;
import com.abusement.park.acneed.utils.CustomSequenceEncoder;
import com.abusement.park.acneed.utils.ImageCompressor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jcodec.scale.BitmapUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class MyJourneyActivity extends AppCompatActivity {

    private static final String TAG = "MY_JOURNEY";

    private User currentUser;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private ArrayAdapter<Image> adapter;
    private ListView imageListView;
    private ProgressBar progressBar;

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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
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
                    /* Create journey in a background thread */
                    progressBar.setVisibility(View.VISIBLE);
                    new CreateVideoTask().execute(CustomAdapter.imagesToInclude(currentUser));
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

    public void logout(View view) {
        finish();
        firebaseAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void goHome(View view) {
        finish();
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    private class CreateVideoTask extends AsyncTask<List<Image>, Integer, File> {

        @Override
        protected File doInBackground(List<Image>... params) {
            int completedImages = 0;
            File video = null;
            try {
                File root = new File(Environment.getExternalStorageDirectory(), File.separator + "Acneed" + File.separator);
                root.mkdirs();
                video = File.createTempFile("MyJourney", ".mp4", root);
                CustomSequenceEncoder sequenceEncoder = new CustomSequenceEncoder(video);
                for (Image image : params[0]) {
                    Log.d(TAG, "Trying to add image " + image.getUri());
                    Uri uri = Uri.parse(image.getUri());
                    ContentResolver cr = MyJourneyActivity.this.getContentResolver();
                    InputStream is = cr.openInputStream(uri);
                    Bitmap frame = ImageCompressor.compressImageToThumbnail(uri, cr, 400, 400);
                    is.close();
                    Log.d(TAG, "Encoding image");
                    sequenceEncoder.encodeNativeFrame(BitmapUtil.fromBitmap(frame));
                    publishProgress(++completedImages, params[0].size());
                }
                sequenceEncoder.finish();
                Log.d(TAG, "Sequence Encoder finished");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return video;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int numerator = values[0];
            int denom = values[1];
            progressBar.setProgress((int) (1.0 * numerator / denom * 100));
        }

        @Override
        protected void onPostExecute(File video) {
            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MyJourneyActivity.this.getApplicationContext(), "Video complete", Toast.LENGTH_LONG).show();
            currentUser.addVideo(new Video(Uri.fromFile(video).toString(), new Date(), video.getPath()));
            databaseReference.child("users").child(currentUser.getUid()).setValue(currentUser);
        }
    }
}
