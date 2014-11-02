package com.droidkit.gallery.picker;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

import com.droidkit.gallery.R;
import com.droidkit.gallery.items.ExplorerItem;
import com.droidkit.gallery.util.MaterialInterpolator;

import java.io.File;
import java.util.ArrayList;


public class PictureViewerFragment extends PicturePickerFragment {

    private File file;
    private ViewPager pager;
    private PictureViewerAdapter adapter;

    // todo more interactive
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.picker_fragment_picture_viewer, null);
        pager = (ViewPager) rootView.findViewById(R.id.pager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getActivity().getActionBar().setTitle(adapter.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        items = new ArrayList<ExplorerItem>();
        //todo: animate it here
        // todo: actors

        Bundle bundle = getArguments();


        pathID = bundle.getString("pathID");
        String selectedItem = bundle.getString("selectedItem");
        loadDirectory();
        adapter = new PictureViewerAdapter(pickerActivity, items);
        pager.setAdapter(adapter);

        for (int i = 0; i < items.size(); i++) {
            ExplorerItem item = items.get(i);
            if (item.getPath().equals(selectedItem)) {
                pager.setCurrentItem(i, false);
                getActivity().getActionBar().setTitle(adapter.getPageTitle(i));
                break;
            }
        }



        return rootView;

    }

    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Badapuf
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.pickerActivity = (GalleryPickerActivity) activity;
    }
}
