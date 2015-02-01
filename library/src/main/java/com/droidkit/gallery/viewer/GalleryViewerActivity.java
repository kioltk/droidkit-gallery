package com.droidkit.gallery.viewer;

import com.droidkit.gallery.picker.GalleryPickerActivity;
import com.droidkit.gallery.picker.PicturePickerFragment;

import android.app.Fragment;
import android.os.Bundle;


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
