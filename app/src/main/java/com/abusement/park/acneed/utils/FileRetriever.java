package com.abusement.park.acneed.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileRetriever {

    private static final String TAG = "FILE_RETRIEVER";

    public static File getFileFromUri(Uri uri, Context context) {
        try {
            InputStream originalInputStream = context.getContentResolver().openInputStream(uri);
            File copy = new File(Environment.getExternalStorageDirectory(), File.separator +
                    "Acneed" + File.separator);
            copy = File.createTempFile("temp", ".jpg", copy);
            OutputStream copyOs = new FileOutputStream(copy);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while( (bytesRead = originalInputStream.read(buffer)) != -1) {
                copyOs.write(buffer, 0, bytesRead);
            }
            copyOs.flush();
            copyOs.close();
            originalInputStream.close();
            return copy;
        } catch (IOException e) {
            Log.d(TAG, "Unable to get the file path", e);
            throw new RuntimeException(e);
        }
    }
}
