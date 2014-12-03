package com.droidkit.gallery.picker;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.droidkit.gallery.R;
import com.droidkit.gallery.photoview.PhotoViewAttacher;
import com.droidkit.images.loading.view.PhotoPreview;

/**
 * Created by kiolt_000 on 24/09/2014.
 */
public class PictureViewerItemFragment extends Fragment {
    private GalleryPickerActivity pickerActivity;
    private View rootView;
    private String path;
    private PhotoViewAttacher mAttacher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.picker_fragment_picture_viewer_item, null);

        path = getArguments().getString("pathID");

        final PhotoPreview holder = (PhotoPreview) rootView.findViewById(R.id.preview);

        // Set the Drawable displayed
        // "file://"+pathID
        // todo actor image loading?
//        Bitmap bitmap = BitmapFactory.decodeFile(pathID);
        String uri = "" + path;
        holder.requestPhoto(uri);
        /*DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoader.getInstance().loadImage(uri,options,new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                holder.setImageBitmap(loadedImage);
                mAttacher = new PhotoViewAttacher(holder);
                holder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // pickerActivity.toggleSystemUi();
                    }
                });
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });*/

        final View selectedView = rootView.findViewById(R.id.selected);
        selectedView.setSelected(pickerActivity.isSelected(path));

        selectedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean selected = pickerActivity.selectItem(path);
                selectedView.setSelected(selected);
            }
        });

        return rootView;
    }

    public static PictureViewerItemFragment getInstance(String path){

        PictureViewerItemFragment fragment = new PictureViewerItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mAttacher!=null) {
            mAttacher.cleanup();

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.pickerActivity = (GalleryPickerActivity) activity;
    }
}
