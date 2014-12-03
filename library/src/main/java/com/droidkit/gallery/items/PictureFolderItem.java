package com.droidkit.gallery.items;

import android.content.Context;

import com.droidkit.gallery.holders.ExploreItemViewHolder;


/**
 * Created by Jesus Christ. Amen.
 */
public class PictureFolderItem extends ContentItem  {
    private final String bucketName;
    private final int bucketId;
    private String albumImage = null;
    private int imgCounter = 0;

    public PictureFolderItem(int bucketId, String bucketName) {
        super("");
        this.bucketId = bucketId;
        this.bucketName = bucketName;
    }

    @Override
    public String getTitle() {
        return bucketName;
    }

    @Override
    public String getSubtitle(Context context) {
        return ""+ imgCounter ;
    }

    @Override
    public String getPath() {
        return ""+ bucketId;
    }



    @Override
    public void bindData(ExploreItemViewHolder holder) {
        holder.setTitle(getTitle());
        holder.setSubtitle(getSubtitle(holder.getContext()));
        holder.setImage("" + albumImage);
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    public void putImage(String imgUri) {
        if(albumImage==null)
            albumImage=imgUri;
        imgCounter++;
    }

    @Override
    public String getImage() {
        return albumImage;
    }

    @Override
    public String getThumb(){
        return albumImage;
    }

}
