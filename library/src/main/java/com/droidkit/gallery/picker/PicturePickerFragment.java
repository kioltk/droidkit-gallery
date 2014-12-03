package com.droidkit.gallery.picker;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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
import com.droidkit.gallery.holders.ExploreItemViewHolder;
import com.droidkit.gallery.items.ExplorerItem;
import com.droidkit.gallery.items.PictureFolderItem;
import com.droidkit.gallery.items.PictureItem;
import com.droidkit.gallery.util.MaterialInterpolator;

import java.io.File;
import java.util.ArrayList;

public class PicturePickerFragment extends Fragment implements AdapterView.OnItemClickListener, SelectionListener {

    public static final String ARG_PATH_ID = "pathID";
    protected View rootView;
    protected String pathID;
    protected ArrayList<ExplorerItem> items;
    protected GalleryPickerActivity pickerActivity;
    protected String pathName = "Select pictures";
    private GridView gridView;
    private int columnsNum;
    private long animationLengthMultiplier = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void loadDirectories() {

        long startTime = System.currentTimeMillis();
        final String[] columns =
                {
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.BUCKET_DISPLAY_NAME
                };
        final String orderBy = MediaStore.Images.Media.DEFAULT_SORT_ORDER + ", " + MediaStore.Images.Media.DATE_MODIFIED + " DESC";

        Cursor imageCursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                null, null, orderBy);
        if (imageCursor != null) {
            int imgIdColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
            int imgUriColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int bucketIdColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int bucketNameColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int insertedImagesCounter = 0;
            int lastBucketId = -1;
            PictureFolderItem folder = null;
            if (imageCursor.moveToFirst())
                do {
                    int bucketId = imageCursor.getInt(bucketIdColumnIndex);

                    String bucketName = imageCursor.getString(bucketNameColumnIndex);
                    if (bucketId != lastBucketId) {

                        folder = new PictureFolderItem(bucketId, bucketName);
                        items.add(folder);
                    }
                    String imgUri = imageCursor.getString(imgUriColumnIndex);
                    long imageId = imageCursor.getLong(imgIdColumnIndex);
                    Cursor thumbCursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
                            pickerActivity.getContentResolver(), imageId,
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            new String[]{ MediaStore.Images.Thumbnails.DATA });
                    int thumbUriIndex = thumbCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                    if(thumbCursor.moveToFirst()) {
                        imgUri = thumbCursor.getString(thumbUriIndex);
                    }
                    thumbCursor.close();
                    folder.putImage(imgUri);


                    lastBucketId = bucketId;
                } while (imageCursor.moveToNext());
            imageCursor.close();
        }
        Log.w("Pictures loader", "Loaded " + items.size() + " directories in " + (System.currentTimeMillis() - startTime));
    }

    protected void loadDirectory() {
        long startTime = System.currentTimeMillis();
        Log.w("Pictures loader", "Loading directory " + pathID);
        final String[] columns = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
        final String orderBy = MediaStore.Images.Media.DATE_MODIFIED + " DESC";

        Cursor imageCursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                MediaStore.Images.Media.BUCKET_ID + " = " + pathID, null, orderBy);
        if (imageCursor != null) {
            int imgUriColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            int imgIdColumnIndex = imageCursor.getColumnIndex(MediaStore.Images.Media._ID);
            if (imageCursor.moveToFirst())
                do {
                    String imageUri =  imageCursor.getString(imgUriColumnIndex);
                    long imageId = imageCursor.getLong(imgIdColumnIndex);
                    Cursor thumbCursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(
                            pickerActivity.getContentResolver(), imageId,
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            new String[]{ MediaStore.Images.Thumbnails.DATA });
                    int thumbUriIndex = thumbCursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
                    String thumb = null;
                    if(thumbCursor.moveToFirst()) {
                        thumb = thumbCursor.getString(thumbUriIndex);
                    }
                    thumbCursor.close();

                    items.add(new PictureItem(new File(imageUri), pickerActivity.isSelected(imageUri), thumb==null? null: new File(thumb)));
                } while (imageCursor.moveToNext());
            imageCursor.close();
        }
        Log.w("Pictures loader", "Loaded " + items.size() + " items in directory in " + (System.currentTimeMillis() - startTime));
    }


    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        long animationLength = 0;
        int animatorId = nextAnim;
        int offsetIncreaseOffset = 0;
        final int defaultAnimationOffsetPerItem = (50);

        if (gridView != null)

            if (nextAnim == R.animator.picker_fragment_explorer_enter || nextAnim == R.animator.picker_fragment_explorer_return) {
                animatorId = R.animator.picker_animation_fake;
                gridView.setAlpha(0);
                gridView.post(new Runnable() {
                    @Override
                    public void run() {
                        gridView.setAlpha(1);
                        int offsetIncreaseOffset = 0;
                        for (int i = 0; i < gridView.getChildCount(); i++) {
                            View galleryItemView = gridView.getChildAt(i);

                            AnimationSet dropDownAnimation = new AnimationSet(true);
                            dropDownAnimation.setInterpolator(new MaterialInterpolator());
                            dropDownAnimation.setDuration(180 * animationLengthMultiplier);

                            if (i % columnsNum == 0) {
                                offsetIncreaseOffset = i * defaultAnimationOffsetPerItem - 25;
                            }

                            dropDownAnimation.setStartOffset((i * defaultAnimationOffsetPerItem - offsetIncreaseOffset) * animationLengthMultiplier);
                            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                            dropDownAnimation.addAnimation(alphaAnimation);
                            ScaleAnimation scaleAnimation = new ScaleAnimation(
                                    0f, 1f,
                                    0f, 1f,
                                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                            dropDownAnimation.addAnimation(scaleAnimation);
                            galleryItemView.startAnimation(dropDownAnimation);
                        }
                    }
                });

                animationLength = gridView.getChildCount() * 100 + 50;
                Log.d("Explorer animation", "CreateAnimator: enter");
            } else if (nextAnim == R.animator.picker_fragment_explorer_exit || nextAnim == R.animator.picker_fragment_explorer_out) {
                animatorId = R.animator.picker_animation_fake;

                for (int i = 0; i < gridView.getChildCount(); i++) {
                    final View galleryItemView = gridView.getChildAt(i);
                    {

                        AnimationSet dropDownAnimation = new AnimationSet(true);
                        dropDownAnimation.setInterpolator(new MaterialInterpolator());
                        dropDownAnimation.setDuration(180 * animationLengthMultiplier);

                        if (i % columnsNum == 0) {
                            offsetIncreaseOffset = i * defaultAnimationOffsetPerItem - 25;
                        }

                        dropDownAnimation.setStartOffset((i * defaultAnimationOffsetPerItem - offsetIncreaseOffset) * animationLengthMultiplier);
                        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
                        dropDownAnimation.addAnimation(alphaAnimation);
                        ScaleAnimation scaleAnimation = new ScaleAnimation(
                                1f, 0f,
                                1f, 0f,
                                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                        dropDownAnimation.addAnimation(scaleAnimation);
                        dropDownAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                galleryItemView.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                galleryItemView.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        galleryItemView.startAnimation(dropDownAnimation);
                    }


                }
                animationLength = gridView.getChildCount() * 100 + defaultAnimationOffsetPerItem;
                Log.d("Explorer animation", "CreateAnimator: exit");

            } else {
                return null;
            }


        AnimatorSet animator = (AnimatorSet) AnimatorInflater.loadAnimator(pickerActivity,
                animatorId);

        animator.setDuration(animationLength * animationLengthMultiplier);

        return animator;
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
            pickerActivity.openFull(pathID, item.getFile());
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
        Bundle bundle = new Bundle();

        PicturePickerFragment fragment = new PicturePickerFragment();
        fragment.setArguments(bundle);


        return fragment;
    }

    public static Fragment getInstance(String path, String title) {

        Bundle bundle = new Bundle();
        bundle.putString(ARG_PATH_ID, path);
        bundle.putString("path_name", title);
        PicturePickerFragment fragment = new PicturePickerFragment();
        fragment.setArguments(bundle);


        return fragment;
    }
}