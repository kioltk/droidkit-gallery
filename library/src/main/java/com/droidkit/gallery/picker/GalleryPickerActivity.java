package com.droidkit.gallery.picker;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;


import com.droidkit.gallery.R;
import com.droidkit.gallery.items.ExplorerItem;
import com.droidkit.gallery.util.SystemUiHider;

import java.io.File;

public class GalleryPickerActivity extends SuperPickerActivity {


    private SystemUiHider mSystemUiHider;
    private static final int HIDER_FLAGS = 0;//SystemUiHider.FLAG_FULLSCREEN;
    private boolean showing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setIcon(R.drawable.bar_picker_icon);

        View container = findViewById(R.id.container);
        final View controlsView = findViewById(R.id.controllers);

        mSystemUiHider = SystemUiHider.getInstance(this, container, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
                    // Cached values.
                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(final boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                            // If the ViewPropertyAnimator API is available
                            // (Honeycomb MR2 and later), use it to animate the
                            // in-layout UI controls at the bottom of the
                            // screen..

                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {
                            // If the ViewPropertyAnimator APIs aren't
                            // available, simply show or hide the in-layout UI
                            // controls.
                        }
                        controlsView.post(new Runnable() {
                            @Override
                            public void run() {
                                controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                            }
                        });
                        if(!showing && visible){
                            //delayedHide(3000);
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            /*case R.id.camera:
                Toast.makeText(this, "Do we need it here?", Toast.LENGTH_SHORT).show();
                break;
            */
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(currentFragment!=null){
            currentFragment.onCreateOptionsMenu(menu,getMenuInflater());
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected Fragment getWelcomeFragment() {
        return new PicturePickerFragment();
    }

    @Override
    protected void save() {

       // DatabaseConnector.save(this, selectedItems);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {

        ExplorerItem item = (ExplorerItem) parent.getItemAtPosition(position);

        if (item.isDirectory()) {
            String path = item.getPath();

            Fragment fragment = PicturePickerFragment.getInstance(item.getPath(),item.getTitle());
            getFragmentManager().beginTransaction()
                    // todo animate out
                    .setCustomAnimations(R.animator.picker_fragment_explorer_enter, R.animator.picker_fragment_explorer_out,
                            R.animator.picker_fragment_explorer_return, R.animator.picker_fragment_explorer_exit)
                    .replace(R.id.container, fragment)
                    .addToBackStack(path)
                    .commit();
            currentFragment = fragment;
        } else {

            String path = item.getPath();
            Bundle bundle = new Bundle();
            bundle.putString("pathID", path);

            Fragment fragment = new PictureViewerFragment();
            fragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.animator.picker_fragment_explorer_enter, R.animator.picker_fragment_explorer_out,
                            R.animator.picker_fragment_explorer_return, R.animator.picker_fragment_explorer_exit)
                    .replace(R.id.container, fragment)
                    .addToBackStack(path)
                    .commit();
            currentFragment = fragment;
        }
        invalidateOptionsMenu();
    }

    public void openFull(String path, File file) {



        Bundle bundle = new Bundle();
        bundle.putString("pathID", path);
        bundle.putString("selectedItem", file.getPath());
        Fragment fragment = new PictureViewerFragment();
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction()
                // todo animate out
                .setCustomAnimations(R.animator.picker_fragment_explorer_enter, R.animator.picker_fragment_explorer_out,
                    R.animator.picker_fragment_explorer_return, R.animator.picker_fragment_explorer_exit)
                .replace(R.id.container, fragment)
                .addToBackStack(path+"_full")
                .commit();
        currentFragment = fragment;
        invalidateOptionsMenu();
    }
}
