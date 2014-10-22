package com.droidkit.gallery.items;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidkit.gallery.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;


/**
 * Created by kiolt_000 on 16/09/2014.
 */
public class PictureFolderItem extends FolderItem {
    private final String bucketName;
    private final int bucketId;
    String albumImage = null;
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
    public void bindData(View itemView) {
        super.bindData(itemView);

        TextView subTitleView = (TextView) itemView.findViewById(R.id.subtitle);

        subTitleView.setVisibility(View.VISIBLE);


    }

    @Override
    public void bindData(ExploreItemViewHolder holder) {
        holder.setTitle(getTitle());
        holder.setSubtitle(getSubtitle(holder.getContext()));
        holder.setImage("file://"+ albumImage);
    }

    @Override
    public void bindImage(View itemView) {

        final ImageView holder = (ImageView) itemView.findViewById(R.id.image);
        if (holder != null) {

            //
            // todo actor image loading?
            holder.setImageResource(R.drawable.loading);




        }
         // super.bindImage(itemView);
    }

    public void putImage(String imgUri) {
        if(albumImage==null)
            albumImage=imgUri;
        imgCounter++;
    }
}
