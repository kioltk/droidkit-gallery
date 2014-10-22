package com.droidkit.gallery.items;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.droidkit.gallery.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;

/**
 * Created by kiolt_000 on 09/10/2014.
 */
public class ExploreItemViewHolder {

    private final TextView titleView;
    private final TextView subTitleView;
    private final ImageView imageView;
    private final View selectedView;
    private final View divider;
    private final TextView typeView;
    private final View itemView;
    private Context context;
    private final int itemSize;

    public ExploreItemViewHolder(View itemView, int itemSize) {
        this.itemSize = (int) (itemSize);
        context = itemView.getContext();
        this.itemView = itemView;
        titleView = (TextView) itemView.findViewById(R.id.title);
        subTitleView = (TextView) itemView.findViewById(R.id.subtitle);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        selectedView = itemView.findViewById(R.id.selected);
        divider = itemView.findViewById(R.id.divider);
        typeView = (TextView) itemView.findViewById(R.id.type);
    }

    public void setTitle(String title) {
        titleView.setVisibility(View.VISIBLE);
        titleView.setText(title);
    }
    public void setSubtitle(String subtitle) {
        subTitleView.setVisibility(View.VISIBLE);
        subTitleView.setText(subtitle);
    }


    public Context getContext() {
        return context;
    }

    public void setSelected(boolean selected) {
        if(selectedView!=null) {
            selectedView.setSelected(selected);
            if (selectedView instanceof Checkable) {
                ((Checkable) selectedView).setChecked(selected);
            }
        }
    }

    public void disableSubtitle() {
        subTitleView.setVisibility(View.GONE);
    }

    public void disableDivider() {
        divider.setVisibility(View.GONE);
    }

    public void enableDivider() {
        divider.setVisibility(View.VISIBLE);
    }

    public void setIcon(int imageId) {
        imageView.setImageResource(imageId);
    }

    public void setImage(String uri){
        imageView.setImageResource(R.drawable.loading);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(false)
                        .cacheOnDisk(false)
                        .build();

        /*ImageLoader.getInstance().loadImage(uri, new ImageSize(itemSize, itemSize), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                imageView.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });*/
    }

    public void setType(String fileType) {
        typeView.setText(fileType);
    }

    public void setSelectListener(View.OnClickListener onClickListener) {

        if (selectedView != null) {
            selectedView.setOnClickListener(onClickListener);
        }
    }

    public void setSelected(int selectedIndex) {
        selectedIndex++;
        if(selectedView!=null) {
            ((TextView) selectedView).setText("" + (selectedIndex > 0 ? selectedIndex : ""));
            selectedView.setSelected(selectedIndex > 0);
        }
    }
}
