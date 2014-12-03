package com.droidkit.images.loading.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;

import com.droidkit.Screen;
import com.droidkit.images.loading.tasks.RawFileTask;

/**
 * Created by Jesus Christ. Amen.
 */
public class PhotoPreview extends ImageReceiverView {
    private Bitmap src;

    public PhotoPreview(Context context) {
        super(context);
    }

    public PhotoPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoPreview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void requestPhoto(String fileName) {
        requestSwitch(new RawFileTask(fileName));
    }

    /*public void requestVideo(String fileName) {
        requestSwitch(new VideoTask(fileName));
    }

    public void requestVideo(int type, int id, ConversationMessage message) {
        requestSwitch(new VideoPreviewTask(type, id, message));
    }

    public void requestPhoto(int type, int id, ConversationMessage message) {
        requestSwitch(new ImagePreviewTask(type, id, message));
    }*/

    public void noRequest() {
        clear();
    }

    public void setSrc(Bitmap src) {
        this.src = src;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = getBitmap();
        if (bitmap == null) {
            bitmap = src;
        }

        if (bitmap != null) {
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();
            matrix.postScale(getWidth() / (float) bitmap.getWidth(), getHeight() / (float) bitmap.getHeight());
            shader.setLocalMatrix(matrix);
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setShader(shader);
            canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), Screen.dp(2), Screen.dp(2), p);
        }
    }
}
