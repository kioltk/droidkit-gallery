package com.droidkit.gallery.viewer;

import com.droidkit.gallery.R;
import com.droidkit.gallery.items.PictureItem;
import com.droidkit.gallery.picker.GalleryPickerActivity;
import com.droidkit.gallery.picker.PicturePickerFragment;
import com.droidkit.gallery.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class GalleryViewerActivity extends GalleryPickerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }


    @Override
    protected Fragment getWelcomeFragment() {

        Bundle bundle = getIntent().getExtras();
        String folderPath = null;
        if(bundle!=null){
            folderPath = bundle.getString("pathID");
        }else{

        }
        return PicturePickerFragment.getInstance(folderPath);
    }
}
