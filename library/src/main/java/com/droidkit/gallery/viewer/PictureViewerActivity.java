package com.droidkit.gallery.viewer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.droidkit.gallery.R;
import com.droidkit.gallery.items.ContentItem;
import com.droidkit.gallery.photoview.PhotoViewAttacher;
import com.droidkit.gallery.picker.GalleryPickerActivity;
import com.droidkit.images.loading.view.ImageKitView;

/**
 * Created by Jesus Christ. Amen.
 */
public class PictureViewerActivity extends ActionBarActivity implements View.OnSystemUiVisibilityChangeListener {
    private static final String ARG_PATH = "arg_absolute_path";
    private static final String ARG_OWNER = "arg_owner";
    private static final String ARG_TIMER = "arg_timer";
    private PhotoViewAttacher attacher;
    private Uri path;
    private boolean uiIsHidden = false;
    private View infoHolder;
    private TextView ownerView;
    private TextView timeView;
    private ImageKitView holder;
    private View decorView;
    private Toolbar toolbar;

    public static Intent openFull(GalleryPickerActivity pickerActivity, ContentItem item) {
        Intent intent = new Intent(pickerActivity, PictureViewerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARG_PATH, item.getFile().getAbsolutePath());
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.picker_activity_picture);
        Bundle extras = getIntent().getExtras();
        path = (Uri) extras.get(ARG_PATH);
        String time = extras.getString(ARG_TIMER);
        final String owner = extras.getString(ARG_OWNER);

        infoHolder = findViewById(R.id.info);
        ownerView = (TextView) findViewById(R.id.owner);
        timeView = (TextView) findViewById(R.id.time);
        holder = (ImageKitView) findViewById(R.id.preview);
        decorView = getWindow().getDecorView();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        int navbarHeight = getNavbarHeight();
        int statbarHeight = getStatbarHeight();
        int actionbarHeight = getAbarHeight();

        toolbar.setPadding(0, statbarHeight,0,0);

        Log.d("heights", "ab: " + actionbarHeight + " navbar: " +  navbarHeight + " statbar: " + statbarHeight);

        String uri = "" + path;
        holder.setImageURI(path);

        ownerView.setText(owner);
        timeView.setText(time);

        decorView.setOnSystemUiVisibilityChangeListener(this);
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        syncUiState();

        attacher = new PhotoViewAttacher(holder);

        attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                decorView.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        });


    }

    private int getAbarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    private int getStatbarHeight() {

        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    private void syncUiState() {
        if (uiIsHidden) {
            toolbar.animate()
                    .y(-toolbar.getHeight())
                    .alpha(0)
                    .setDuration(300)
                    .start();
            infoHolder.animate()
                    .alpha(0)
                    .setDuration(300)
                    .start();
        } else {
            toolbar.animate()
                    .y(0)
                    .alpha(1)
                    .setDuration(300)
                    .start();
            infoHolder.animate()
                    .alpha(1)
                    .setDuration(300)
                    .start();

            /*decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);*/

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.viewer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (i == R.id.action_share) {
            Toast.makeText(this, R.string.picture_share, Toast.LENGTH_SHORT).show();
            return true;
        } else if (i == R.id.action) {
            Toast.makeText(this, "Action", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (attacher != null) {
            attacher.cleanup();

        }
    }

    public static Intent open(Uri path, Context context) {
        return new Intent(context, PictureViewerActivity.class)
                .putExtra(ARG_OWNER, "owner")
                .putExtra(ARG_TIMER, "time")
                .putExtra(ARG_PATH, path);
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) {
            uiIsHidden = true;
        } else {
            uiIsHidden = false;
        }
        syncUiState();
    }

    public int getNavbarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }
}