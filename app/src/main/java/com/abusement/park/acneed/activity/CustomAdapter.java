package com.abusement.park.acneed.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.abusement.park.acneed.R;
import com.abusement.park.acneed.model.Image;
import com.abusement.park.acneed.utils.ImageCompressor;

import java.io.FileNotFoundException;
import java.util.List;

import static android.text.format.DateFormat.getMediumDateFormat;

public class CustomAdapter<T> extends ArrayAdapter<Image> {

    private static final String TAG = "CUSTOM_ADAPTER";

    private static LruCache<String, Bitmap> thumbnailCache;

    private static final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024);

    static {
        if (thumbnailCache == null) {
            thumbnailCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount() / 1024;
                }
            };
        }
    }

    private static void addToCache(String key, Bitmap bitmap) {
        if (thumbnailCache.get(key) == null) {
            thumbnailCache.put(key, bitmap);
        }
    }

    public CustomAdapter(Context context, int resource, List<Image> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(super.getContext());
        View customView = convertView == null ? inflater.inflate(R.layout.images_list_item, parent, false) :
                convertView;
        Image image = super.getItem(position);

        ImageView imageView = (ImageView) customView.findViewById(R.id.images_list_image_view);
        TextView dateTextView = (TextView) customView.findViewById(R.id.images_list_date_text);

        dateTextView.setText(getMediumDateFormat(super.getContext()).format(image.getUploadDate()));
        try {
            String uri = image.getUri();
            Bitmap bitmap;
            if ((bitmap = thumbnailCache.get(uri)) == null) {
                bitmap = ImageCompressor.compressImageToThumbnail(
                        Uri.parse(image.getUri()),
                        getContext().getContentResolver(),
                        200,
                        200);
                addToCache(uri,bitmap);
            }
            imageView.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            Log.e(TAG, "Failed to add bitmap to list view");
            e.printStackTrace();
        }
        return  customView;
    }
}
