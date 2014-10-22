package com.droidkit.gallery.core;

import android.view.View;

import com.droidkit.gallery.items.ExplorerItem;

/**
 * Created by kiolt_000 on 22/10/2014.
 */
public interface SelectionListener {
    void selectItem(ExplorerItem item, View itemView);
}
