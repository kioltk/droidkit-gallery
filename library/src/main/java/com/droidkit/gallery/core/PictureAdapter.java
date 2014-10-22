package com.droidkit.gallery.core;

import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.droidkit.gallery.R;
import com.droidkit.gallery.SuperPickerActivity;
import com.droidkit.gallery.items.ExploreItemViewHolder;
import com.droidkit.gallery.items.ExplorerItem;
import com.droidkit.gallery.items.PictureFolderHolder;
import com.droidkit.gallery.items.PictureFolderItem;
import com.droidkit.gallery.items.PictureHolder;
import com.droidkit.gallery.util.Timer;

import java.util.ArrayList;

/**
 * Created by kiolt_000 on 15/09/2014.
 */
public class PictureAdapter extends BaseAdapter {

    private final ArrayList<ExplorerItem> items;
    protected final SuperPickerActivity pickerActivity;
    private final int itemSize;
    private final int spacing;
    private final int columnsNum;
    private SelectionListener selectionListener;
    private int itemWidth;

    public PictureAdapter(SuperPickerActivity activity, ArrayList<ExplorerItem> items, int columnsNum) {
        this.pickerActivity = activity;
        this.items = items;
        this.columnsNum = columnsNum;
        DisplayMetrics metrics = pickerActivity.getResources().getDisplayMetrics();
        this.spacing = pickerActivity.getResources().getDimensionPixelSize(R.dimen.picker_picture_spacing);


        // PWNED!
        this.itemSize = (int) ((metrics.widthPixels / columnsNum)) - spacing;
    }
    public PictureAdapter(SuperPickerActivity activity, ArrayList<ExplorerItem> items, int columnsNum, SelectionListener selectionListener) {
        this(activity,items,columnsNum);
        this.selectionListener = selectionListener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    }

    @Override
    public ExplorerItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Timer.start();
        final ExplorerItem item = getItem(position);
        final View itemView;


        ExploreItemViewHolder holder;
        if (convertView != null) {
            itemView = convertView;
            holder = (ExploreItemViewHolder) itemView.getTag();
        } else {
            if (item instanceof PictureFolderItem) {
                itemView = View.inflate(pickerActivity, R.layout.picker_item_picture_folder, null);
                holder = new PictureFolderHolder(itemView, itemSize);
            } else {
                itemView = View.inflate(pickerActivity, R.layout.picker_item_picture, null);
                holder = (new PictureHolder(itemView, itemSize));
            }
        }

        itemView.setTag(holder);
        ViewGroup.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemSize);
        itemView.setLayoutParams(params);

        item.bindData(holder);
        holder.setSelected(pickerActivity.getSelectedIndex(item));
        holder.setSelectListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectionListener!=null)
                    selectionListener.selectItem(item, itemView);
            }
        });

        Timer.stop("Item created");
        return itemView;
    }
}