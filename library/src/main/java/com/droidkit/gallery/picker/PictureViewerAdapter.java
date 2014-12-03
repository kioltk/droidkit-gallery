package com.droidkit.gallery.picker;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.droidkit.gallery.items.ExplorerItem;
import com.droidkit.gallery.items.PictureItem;

import java.util.ArrayList;

/**
 * Created by kiolt_000 on 24/09/2014.
 */
public class PictureViewerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<PictureItem> items;
    private SuperPickerActivity pickerActivity;

    public PictureViewerAdapter(SuperPickerActivity pickerActivity, ArrayList<PictureItem> items) {
        super(pickerActivity.getFragmentManager());
        this.pickerActivity = pickerActivity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position + 1 + " of " + getCount();
    }


    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new PictureViewerItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pathID", items.get(i).getFull());
        fragment.setArguments(bundle);
        return fragment;
    }

}