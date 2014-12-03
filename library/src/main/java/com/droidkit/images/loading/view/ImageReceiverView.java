package com.droidkit.images.loading.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import com.droidkit.images.cache.BitmapReference;
import com.droidkit.images.loading.AbsTask;
import com.droidkit.images.loading.ImageLoader;
import com.droidkit.images.loading.ImageLoaderProvider;
import com.droidkit.images.loading.ImageReceiver;
import com.droidkit.images.loading.ReceiverCallback;

/**
 * Created by ex3ndr on 06.09.14.
 */
public class ImageReceiverView extends View implements ReceiverCallback {
    private ImageReceiver receiver;

    public ImageReceiverView(Context context) {
        super(context);
        init();
    }

    public ImageReceiverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageReceiverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (getContext().getApplicationContext() instanceof ImageLoaderProvider) {
            ImageLoader loader = ((ImageLoaderProvider) getContext().getApplicationContext()).getImageLoader();
            receiver = loader.createReceiver(this);
        } else {
            throw new RuntimeException("Application does not implement ImageLoaderProvider");
        }
    }

    public void request(AbsTask absTask) {
        receiver.request(absTask);
    }

    public void requestSwitch(AbsTask absTask) {
        receiver.request(absTask, false);
    }

    public void clear() {
        receiver.clear();
    }

    protected Bitmap getBitmap() {
        BitmapReference reference = receiver.getReference();
        if (reference != null && !reference.isReleased()) {
            return reference.getBitmap();
        } else {
            return null;
        }
    }

    @Override
    public void onImageLoaded(BitmapReference bitmap) {
        invalidate();
    }

    @Override
    public void onImageCleared() {
        invalidate();
    }

    @Override
    public void onImageError() {
        invalidate();
    }
}
