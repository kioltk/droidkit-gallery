package com.droidkit.gallery.items;

import android.content.Context;

import com.droidkit.gallery.R;
import com.droidkit.gallery.util.TimeHelper;

import java.io.File;

/**
 * Created by Jesus Christ. Amen.
 */
public class ContentItem extends ExplorerItem {


    protected File thumb = null;
    public int imageWidth;
    public int imageHeight;
    public int thumbWidth;
    public int thumbHeight;
    private int rotation;

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
        return getThumb();
    }

    public String getThumb() {
        if(thumb!=null)
            return thumb.getAbsolutePath();
        return file.getAbsolutePath();
    }

    public int getThumbWidth() {

        if(thumbWidth==0) return imageWidth;
        return thumbWidth;
    }

    public int getThumbHeight() {
        if(thumbHeight==0) return imageHeight;
        return thumbHeight;
    }

    public void setImageSize(int imageWidth, int imageHeight) {
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    public void setThumb(String thumb, int thumbWidth, int thumbHeight) {
        this.thumb = new File(thumb);
        this.thumbWidth = thumbWidth;
        this.thumbHeight = thumbHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setRotation(int rotation){
        this.rotation = rotation;
    }

    public int getRotation(){
        return this.rotation;
    }

}