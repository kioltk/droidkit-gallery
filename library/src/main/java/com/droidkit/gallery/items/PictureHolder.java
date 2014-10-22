package com.droidkit.gallery.items;

import android.view.View;

import com.droidkit.gallery.R;

/**
 * Created by kiolt_000 on 22/10/2014.
 */
public class PictureHolder extends ExploreItemViewHolder {
    private final View videoView;

    public PictureHolder(View itemView) {
        super(itemView);
        videoView = itemView.findViewById(R.id.video_holder);
    }

    public void setVideo() {
        videoView.setVisibility(View.VISIBLE);
    }

    public void disableVideo() {
        videoView.setVisibility(View.GONE);
    }
}
