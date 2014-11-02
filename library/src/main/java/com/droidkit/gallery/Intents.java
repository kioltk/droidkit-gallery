package com.droidkit.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.droidkit.gallery.picker.GalleryPickerActivity;
import com.droidkit.gallery.viewer.GalleryViewerActivity;

/**
 * Created by kioltk on 10/30/14.
 */
public class Intents {

    public static Intent getPickerActivity(Context context){
        Intent intent = new Intent(context, GalleryPickerActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);



        return intent;
    }

    public static Intent getViewerActivity(Context context, String folderPath) {
        Intent intent = new Intent(context, GalleryViewerActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);

        bundle.putString("pathID", folderPath);

        return intent;
    }
}
