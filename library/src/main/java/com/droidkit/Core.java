package com.droidkit;

import com.droidkit.images.cache.BitmapClasificator;
import com.droidkit.images.loading.ImageLoader;

/**
 * Created by Jesus Christ. Amen.
 */
public class Core {


    private static Core core;
    private final ImageLoader imageLoader;

    public static void init(AppContext appContext) {
        Core.core = new Core(appContext);
    }
    private Core(AppContext appContext){


        BitmapClasificator clasificator = new BitmapClasificator.Builder()
                .startExactSize(100, 100)
                .setFreeSize(2)
                .setLruSize(15)
                .endFilter()
                .startAny()
                .useSizeInBytes()
                .setLruSize(16 * 1024 * 1024)
                .setFreeSize(8 * 1024 * 1024)
                .endFilter()
                .build();

        this.imageLoader = new ImageLoader(clasificator, appContext);
        //this.imageLoader.getTaskResolver().register(ImagePreviewTask.class, ImagePreviewActor.class);
        //this.imageLoader.getTaskResolver().register(VideoPreviewTask.class, VideoPreviewActor.class);
        //this.imageLoader.getTaskResolver().register(VideoTask.class, VideoActor.class);
        //this.imageLoader.getTaskResolver().register(AvatarTask.class, AvatarActor.class);
        //this.imageLoader.getTaskResolver().register(FullAvatarTask.class, FullAvatarActor.class);
    }
    public static Core core(){
        return core;
    }
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
