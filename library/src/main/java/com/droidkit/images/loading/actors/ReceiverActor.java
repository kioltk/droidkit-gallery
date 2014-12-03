package com.droidkit.images.loading.actors;

import com.droidkit.actors.*;
import com.droidkit.actors.tasks.AskCallback;
import com.droidkit.actors.tasks.AskFuture;
import com.droidkit.images.cache.BitmapReference;
import com.droidkit.images.loading.ImageLoader;
import com.droidkit.images.loading.ImageReceiver;
import com.droidkit.images.loading.TaskResolver;
import com.droidkit.images.loading.actors.messages.*;

import com.droidkit.Logger;

/**
 * Created by ex3ndr on 20.08.14.
 */
public final class ReceiverActor extends Actor {

    public static Props<ReceiverActor> prop(final ImageReceiver receiver, final ImageLoader imageLoader) {
        return Props.create(ReceiverActor.class, new ActorCreator<ReceiverActor>() {
            @Override
            public ReceiverActor create() {
                return new ReceiverActor(receiver, imageLoader);
            }
        }).changeDispatcher("ui");
    }

    private int taskId = -1;
    private AskFuture future;
    private TaskResolver resolver;
    private ImageReceiver receiver;
    private ImageLoader loader;

    public ReceiverActor(ImageReceiver receiver, ImageLoader imageLoader) {
        this.loader = imageLoader;
        this.resolver = imageLoader.getTaskResolver();
        this.receiver = receiver;
    }

    @Override
    public void onReceive(Object message) {
        super.onReceive(message);

        final String path = self().getPath();

        if (message instanceof TaskRequest) {
            final TaskRequest taskRequest = (TaskRequest) message;
            taskId = taskRequest.getRequestId();
            final int currentId = taskId;

            Logger.d("ImageReceiver", path + "|RequestTask " + taskId);
            // Cancel current work
            cancel();

            BitmapReference reference = loader.getMemoryCache().findInCache(taskRequest.getRequest().getKey());
            if (reference != null) {
                Logger.d("ImageReceiver", path + "|Founded in cache");
                receiver.onImageLoaded(new ImageLoaded(currentId, reference));
                return;
            }

            try {
                final ActorSelection selection = resolver.resolveSelection(taskRequest.getRequest());
                future = ask(selection, new AskCallback<BitmapReference>() {
                    @Override
                    public void onResult(BitmapReference result) {
                        Logger.d("ImageReceiver", path + "|Work result");
                        receiver.onImageLoaded(new ImageLoaded(currentId, result.fork()));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Logger.d("ImageReceiver", path + "|Work error: " + throwable);
                        receiver.onImageError(new ImageError(currentId, throwable));
                    }
                });
                Logger.d("ImageReceiver", path + "|Requested new work @" + selection.getPath());
            } catch (Exception e) {
                Logger.d("ImageReceiver", path + "|Error during resolve");
                receiver.onImageError(new ImageError(currentId, e));
            }
        } else if (message instanceof TaskCancel) {
            TaskCancel taskCancel = (TaskCancel) message;
            if (taskId == taskCancel.getRequestId()) {
                cancel();
            }
        }
    }

    private void cancel() {
        if (future != null) {
            future.cancel();
            future = null;
        }
    }
}