package com.droidkit.gallery.picker;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droidkit.gallery.R;
import com.droidkit.gallery.items.PictureItem;
import com.droidkit.gallery.photoview.PhotoViewAttacher;
import com.droidkit.images.loading.view.ImageKitView;

/**
 * Created by kiolt_000 on 24/09/2014.
 */
public class PictureViewerItemFragment extends Fragment {
    private static final String ARGUMENT_PATH = "arg_path";
    private static final String ARGUMENT_WIDTH = "arg_width";
    private static final String ARGUMENT_HEIGHT = "arg_height";
    private GalleryPickerActivity pickerActivity;
    private View rootView;
    private String path;
    private PhotoViewAttacher attacher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.picker_fragment_picture_viewer_item, null);

        path = getArguments().getString(ARGUMENT_PATH);

        final ImageKitView holder = (ImageKitView) rootView.findViewById(R.id.preview);



        String uri = "" + path;
        holder.requestPhoto(uri);


        PhotoViewAttacher attacher = new PhotoViewAttacher(holder);

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

    public static PictureViewerItemFragment getInstance(PictureItem item){

        PictureViewerItemFragment fragment = new PictureViewerItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_PATH, item.getImage());
        bundle.putInt(ARGUMENT_WIDTH, item.getImageWidth());
        bundle.putInt(ARGUMENT_HEIGHT, item.getImageHeight());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onPause() {
        super.onPause();
        if(attacher !=null) {
            attacher.cleanup();

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.pickerActivity = (GalleryPickerActivity) activity;
    }
}
