package com.droidkit.gallery.picker;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;


import com.droidkit.gallery.R;
import com.droidkit.gallery.items.ExploreItemViewHolder;
import com.droidkit.gallery.items.ExplorerItem;
import com.droidkit.gallery.items.PictureFolderItem;
import com.droidkit.gallery.items.PictureItem;
import com.droidkit.gallery.util.MaterialInterpolator;

import java.io.File;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PicturePickerFragment extends Fragment implements AdapterView.OnItemClickListener, SelectionListener {

    public static final String ARG_PATH_ID = "pathID";
    protected View rootView;
    protected String pathID;
    protected ArrayList<ExplorerItem> items;
    protected GalleryPickerActivity pickerActivity;
    protected String pathName = "Select pictures";
    private GridView gridView;
    private int columnsNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void loadDirectories() {

        long startTime = System.currentTimeMillis();
        final String[] columns =
                {
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
                };
        final String orderBy = MediaStore.Images.Media.DEFAULT_SORT_ORDER;

        Cursor imagecursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                null, null, orderBy);
        if (imagecursor != null) {
            int imgUriColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int bucketIdColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int bucketNameColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int insertedImagesCounter = 0;
            int lastBucketId = -1;
            PictureFolderItem folder = null;
            if (imagecursor.moveToFirst())
                do {
                    int bucketId = imagecursor.getInt(bucketIdColumnIndex);

                    String bucketName = imagecursor.getString(bucketNameColumnIndex);
                    if (bucketId != lastBucketId) {

                        folder = new PictureFolderItem(bucketId, bucketName);
                        items.add(folder);
                    }
                    String imgUri = imagecursor.getString(imgUriColumnIndex);
                    folder.putImage(imgUri);


                    lastBucketId = bucketId;
                } while (imagecursor.moveToNext());
            imagecursor.close();
        }
        Log.w("Pictures loader", "Loaded " + items.size() + " directories in " + (System.currentTimeMillis() - startTime));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        //todo: animate it here
        items = new ArrayList<ExplorerItem>();
        if (bundle == null) {
            rootView = inflater.inflate(R.layout.picker_fragment_picture_picker, container, false);
            loadDirectories();
            if (items.isEmpty()) {

                ((TextView) rootView.findViewById(R.id.status)).setText(R.string.picker_pictures_empty);

            } else {
                columnsNum = getResources().getInteger(R.integer.num_columns_albums);
                gridView = (GridView) rootView.findViewById(R.id.grid);
                gridView.setNumColumns(columnsNum);
                gridView.setAdapter(new PictureAdapter(pickerActivity, items, columnsNum));
                gridView.setOnItemClickListener(pickerActivity);
            }
        } else {

            pathID = bundle.getString(ARG_PATH_ID);
            pathName = bundle.getString("path_name");
            loadDirectory();
            rootView = inflater.inflate(R.layout.picker_fragment_picture_picker, container, false);
            if (items.isEmpty()) {
                ((TextView) rootView.findViewById(R.id.status)).setText(R.string.picker_pictures_empty_folder);
            } else {
                gridView = (GridView) rootView.findViewById(R.id.grid);
                columnsNum = getResources().getInteger(R.integer.num_columns_pictures);
                gridView.setNumColumns(columnsNum);
                gridView.setAdapter(new PictureAdapter(pickerActivity, items, columnsNum, this));
                gridView.setOnItemClickListener(this);
            }
        }
        pickerActivity.updateCounter();
        pickerActivity.getActionBar().setTitle(pathName);
        return rootView;
    }

    protected void loadDirectory() {
        long startTime = System.currentTimeMillis();
        Log.w("Pictures loader", "Loading directory " + pathID);
        final String[] columns = {MediaStore.Images.Media.DATA};
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;

        Cursor imageCursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                MediaStore.Images.Media.BUCKET_ID + " = " + pathID, null, orderBy);
        if (imageCursor != null) {
            int imgUriColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            if (imageCursor.moveToFirst())
                do {
                    String imageUri = imageCursor.getString(imgUriColumnIndex);

                    items.add(new PictureItem(new File(imageUri), pickerActivity.isSelected(imageUri)));

                } while (imageCursor.moveToNext());
            imageCursor.close();
        }
        Log.w("Pictures loader", "Loaded " + items.size() + " items in directory in " + (System.currentTimeMillis() - startTime));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.picker_picture, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.pickerActivity = (GalleryPickerActivity) activity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View itemView, int position, long id) {
        ExplorerItem item = (ExplorerItem) parent.getItemAtPosition(position);
        if (item.isDirectory())
            pickerActivity.onItemClick(parent, itemView, position, id);
        else
            pickerActivity.openFull(pathID, item.getFile())
                    ;
    }

    @Override
    public void selectItem(ExplorerItem item, View itemView) {
        pickerActivity.selectItem(item, itemView);
        int firstVisible = gridView.getFirstVisiblePosition();
        int lastVisible = gridView.getLastVisiblePosition();
        for (int i = 0; i < items.size(); i++) {
            ExplorerItem tempItem = items.get(i);
            String tempPath = tempItem.getPath();
            if (pickerActivity.isSelected(tempPath)) {
                if (i >= firstVisible && i <= lastVisible) {
                    ExploreItemViewHolder holder = (ExploreItemViewHolder) gridView.getChildAt(i - firstVisible).getTag();
                    holder.setSelected(pickerActivity.getSelectedIndex(tempItem));
                }

            }
        }

    }

    public static Fragment getInstance(String folderPath) {

        PicturePickerFragment fragment = new PicturePickerFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);

        

        return fragment;
    }
}
