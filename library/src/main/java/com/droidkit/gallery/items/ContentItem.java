package com.droidkit.gallery.items;

import android.content.Context;

import com.droidkit.gallery.R;
import com.droidkit.gallery.util.TimeHelper;

import java.io.File;

/**
 * Created by Jesus Christ. Amen.
 */
public class ContentItem extends ExplorerItem {


    public ContentItem(String path) {
        super(path);
    }

    public ContentItem(File file) {
        super(file);
    }

    public ContentItem(File file, boolean selected) {
        super(file, selected);
    }

    public ContentItem(File file, boolean selected, String fileType) {
        super(file, selected, fileType);
    }


    public ContentItem(File file, boolean selected, String fileType, int imageId) {
        super(file, selected, fileType, imageId, true);
    }




    public String getImage() {
        return null;
    }

    public String getThumb() {
        return null;
    }
}