package com.droidkit.gallery.core;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.droidkit.gallery.SuperPickerActivity;
import com.droidkit.gallery.items.ExplorerItem;

import java.util.ArrayList;

/**
 * Created by kiolt_000 on 24/09/2014.
 */
public class PictureViewerAdapter extends FragmentStatePagerAdapter {
    private final ArrayList<ExplorerItem> items;
    private SuperPickerActivity pickerActivity;

    public PictureViewerAdapter(SuperPickerActivity pickerActivity, ArrayList<ExplorerItem> items) {
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
        return PictureViewerItemFragment.getInstance(items.get(i).getPath());
    }

}