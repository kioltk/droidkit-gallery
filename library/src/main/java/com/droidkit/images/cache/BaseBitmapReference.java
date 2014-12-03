package com.droidkit.images.cache;

import android.graphics.Bitmap;

import java.util.HashSet;

/**
 * Created by ex3ndr on 26.08.14.
 */
 /* package */ class BaseBitmapReference {

    /* package */ String key;
    /* package */ Bitmap bitmap;

    private HashSet<BitmapReference> references = new HashSet<BitmapReference>();

    private boolean isReleased;
    private MemoryCache memoryCache;

    /* package */ BaseBitmapReference(MemoryCache memoryCache, String key, Bitmap bitmap) {
        this.memoryCache = memoryCache;
        this.bitmap = bitmap;
        this.key = key;
        this.isReleased = false;
    }

    public synchronized String getKey() {
        return key;
    }

    public synchronized Bitmap getBitmap() {
        return bitmap;
    }

    public synchronized boolean isReleased() {
        return isReleased;
    }

    public synchronized BitmapReference createReference() {
        BitmapReference ref = new BitmapReference(key, bitmap, this);
        references.add(ref);
        return ref;
    }

    synchronized void releaseReference(BitmapReference reference) {
        if (isReleased) {
            return;
        }

        references.remove(reference);
        if (references.size() == 0) {
            release();
        }
    }

    public synchronized void release() {
        if (isReleased) {
            return;
        }
        isReleased = true;
        for (BitmapReference reference : references.toArray(new BitmapReference[0])) {
            reference.release();
        }
        references.clear();
        memoryCache.onReferenceDie(this);
        bitmap = null;
        key = null;
    }
}
