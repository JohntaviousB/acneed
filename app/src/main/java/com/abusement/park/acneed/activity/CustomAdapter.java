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
import com.abusement.park.acneed.model.User;
import com.abusement.park.acneed.utils.ImageCompressor;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.text.format.DateFormat.getMediumDateFormat;

public class CustomAdapter<T> extends ArrayAdapter<Image> {

    private static final String TAG = "CUSTOM_ADAPTER";
    private static LruCache<String, Bitmap> thumbnailCache;
    private static final int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024);
    private static final Map<Image, Boolean> checkedRows = new LinkedHashMap<>();

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

    public static List<Image> imagesToInclude(User user) {
        List<Image> result = new ArrayList<>();
        for (Map.Entry<Image, Boolean > entry : checkedRows.entrySet()) {
            if (entry.getValue()) {
                result.add(entry.getKey());
            }
        }

        /* There's a chance the listView did not include all the images b/c the
            user did not scroll, so we'll just add the rest of the images not already
            in the map
         */
        for (Image image : user.getImages()) {
            if (!checkedRows.containsKey(image)) {
                result.add(image);
            }
        }
        return result;
    }

    public CustomAdapter(Context context, int resource, List<Image> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(super.getContext());
        View customView = convertView == null ? inflater.inflate(R.layout.images_list_item, parent, false) :
                convertView;
        final Image image = super.getItem(position);

        ImageView imageView = (ImageView) customView.findViewById(R.id.images_list_image_view);
        TextView dateTextView = (TextView) customView.findViewById(R.id.images_list_date_text);
        final CheckBox includeCheckBox = (CheckBox) customView.findViewById(R.id.images_list_check_box);

        dateTextView.setText(getMediumDateFormat(super.getContext()).format(image.getUploadDate()));
        try {
            final String uri = image.getUri();
            if (!checkedRows.containsKey(image)) {
                checkedRows.put(image, true);
            } else {
                includeCheckBox.setChecked(checkedRows.get(image));
            }
            includeCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedRows.put(image, !checkedRows.get(image));
                }
            });
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
