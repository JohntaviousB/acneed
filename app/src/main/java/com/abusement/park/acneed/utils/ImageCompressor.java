package com.abusement.park.acneed.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;

import java.io.FileNotFoundException;

public class ImageCompressor {

    /**
     * Needed in order to avoid OutOfMemoryExceptions when instantiating multiple thumbnails
     * @param filepath
     * @param contentResolver
     * @param width
     * @param height
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap compressImageToThumbnail(Uri filepath, ContentResolver contentResolver, int width, int height)
            throws FileNotFoundException {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inTempStorage = new byte[24 * 1024];
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(filepath), null, options);
        return ThumbnailUtils.extractThumbnail(bitmap, width, height);
    }
}
