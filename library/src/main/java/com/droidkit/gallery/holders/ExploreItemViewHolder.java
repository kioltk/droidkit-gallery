package com.droidkit.gallery.holders;

import android.content.Context;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droidkit.gallery.R;
import com.droidkit.images.loading.view.PhotoPreview;

/**
 * Created by kiolt_000 on 09/10/2014.
 */
public class ExploreItemViewHolder {

    private final TextView titleView;
    private final TextView subTitleView;
    private final PhotoPreview imageView;
    private final View selectedView;
    //private final View divider;
    //private final TextView typeView;
    private final View itemView;
    private Context context;
    private final int itemSize;

    public ExploreItemViewHolder(View itemView, int itemSize) {
        this.itemSize = (int) (itemSize);
        context = itemView.getContext();
        this.itemView = itemView;
        titleView = (TextView) itemView.findViewById(R.id.title);
        subTitleView = (TextView) itemView.findViewById(R.id.subtitle);
        imageView = (PhotoPreview) itemView.findViewById(R.id.preview);
        selectedView = itemView.findViewById(R.id.selected);
        //divider = itemView.findViewById(R.id.divider);
        //typeView = (TextView) itemView.findViewById(R.id.type);
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


    public void setIcon(int imageId) {
        //imageView.setImageResource(imageId);
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

    public void setImage(String uri, int thumbWidth, int thumbHeight) {
        imageView.setSrc(null);
        imageView.clear();
        imageView.requestPreview(uri, itemSize, itemSize);

        int deltaSize = thumbHeight > thumbWidth ? thumbWidth : thumbHeight;

        double scale = ((double)  itemSize/ (double) deltaSize);
        int width = (int) (scale * thumbWidth);
        int height = (int) (scale * thumbHeight);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(width, height));

    }
}
