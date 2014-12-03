package com.droidkit;

import android.app.Application;

import com.droidkit.images.loading.ImageLoader;
import com.droidkit.images.loading.ImageLoaderProvider;

/**
 * Created by Jesus Christ. Amen.
 */
public class AppContext extends Application implements ImageLoaderProvider {
    private static AppContext context;

    public static AppContext getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Core.init(this);
    }

    @Override
    public ImageLoader getImageLoader() {
        return Core.core().getImageLoader();
    }
}
