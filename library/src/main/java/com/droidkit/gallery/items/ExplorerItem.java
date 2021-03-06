package com.droidkit.gallery.items;

import android.content.Context;


import com.droidkit.gallery.holders.ExploreItemViewHolder;

import java.io.File;

/**
* Created by kiolt_000 on 14/09/2014.
*/
public class ExplorerItem {
    private String fileType = null;
    protected final File file;
    private boolean selected = false;
    private boolean enabled = true;
    private int imageId = 0;

    public ExplorerItem(File file) {
        this.file = file;
    }

    public ExplorerItem(File file, boolean selected) {
        this(file);
        this.selected = selected;
    }

    public ExplorerItem(File file, boolean selected, String fileType) {
        this(file, selected);
        this.fileType = fileType;
    }

    public ExplorerItem(String path) {
        this(new File(path), false);
    }

    public ExplorerItem(File file, boolean selected, String fileType, int imageId, boolean enabled) {
        this.file = file;
        this.selected = selected;
        this.fileType = fileType;
        this.imageId = imageId;
        this.enabled = enabled;
    }


    public String getTitle() {
        return file.getName();
    }

    public String getSubtitle(Context context) {
        return null;
    }


    public String getPath() {
        return file.getAbsolutePath();
    }

    public boolean isDirectory() {
        return file.isDirectory();
    }

    public File getFile() {
        return file;
    }

    public String getFileType() {
        return fileType;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Long getLastModified() {
        return file.lastModified();
    }

    public void bindData(ExploreItemViewHolder holder) {
        holder.setTitle(getTitle());
        holder.setSubtitle(getSubtitle(holder.getContext()));
        holder.setSelected(selected);
        //holder.enableDivider();
    }
}