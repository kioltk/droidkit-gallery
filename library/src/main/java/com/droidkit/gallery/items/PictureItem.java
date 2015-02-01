package com.droidkit.gallery.items;

import android.content.Context;

import com.droidkit.gallery.R;
import com.droidkit.gallery.holders.ExploreItemViewHolder;
import com.droidkit.gallery.holders.PictureHolder;
import com.droidkit.gallery.util.TimeHelper;

import java.io.File;

/**
 * Created by Jesus Christ. Amen.
 */
public class PictureItem extends ContentItem {



    public PictureItem(File file, boolean selected) {
        super(file, selected);
    }

    @Override
    public boolean isDirectory() {
        return false;
    }

    @Override
    public void bindData(ExploreItemViewHolder holder) {
        holder.setImage(getThumb(), getThumbWidth(), getThumbHeight());
        if (isVideo()) {
            ((PictureHolder) holder).setVideo();
        } else {
            ((PictureHolder) holder).disableVideo();
        }
    }

    @Override
    public String getSubtitle(Context context) {
        String convertedSize = null;
        long size = (int) file.length();
        if (size > 1024 * 1024 * 1024) {
            convertedSize = (size / (1024 * 1024 * 1024)) + "" + ((size % (1024 * 1024 * 1024)) / (100 * 1024 * 1024)) + " " + context.getString(R.string.picker_gbytes);
        }
        if (size > 1024 * 1024) {
            convertedSize = (size / (1024 * 1024)) + "" + ((size % (1024 * 1024)) / (100 * 1024)) + " " + context.getString(R.string.picker_mbytes);
        }
        if (convertedSize == null) {
            if (size / 1024 == 0) {
                convertedSize = context.getString(R.string.picker_bytes, size);
            } else
                convertedSize = (size / (1024)) + " " + context.getString(R.string.picker_kbytes);
        }

        long date = file.lastModified();
        String subtitle = convertedSize;
        if (date != 0) {
            subtitle += ", " + TimeHelper.getConvertedTime(date, context);
        }
        return subtitle;
    }



    public boolean isVideo() {
        return false;
    }



}