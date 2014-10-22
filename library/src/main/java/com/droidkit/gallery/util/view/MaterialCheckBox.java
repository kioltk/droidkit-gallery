package com.droidkit.gallery.util.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

import com.droidkit.gallery.R;

/**
 * Created by kiolt_000 on 22/10/2014.
 */
    public class MaterialCheckBox extends View implements Checkable, View.OnClickListener {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = getResources().getColor(R.color.picker_main_color); // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;
    private boolean checked = false;
    private boolean invalidation = false;
    private int progress;
    private OnClickListener clickListener;

    public MaterialCheckBox(Context context) {
        super(context);
        init(null, 0);
    }

    public MaterialCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MaterialCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MaterialCheckBox, defStyle, 0);
        mExampleString = null;
        mExampleColor = a.getColor(
                R.styleable.MaterialCheckBox_lineColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.MaterialCheckBox_lineWidth,
                mExampleDimension);


        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        //invalidateTextPaintAndMeasurements();
        if (!isInEditMode()) {
            super.setOnClickListener(this);
        }
    }

    private void invalidateTextPaintAndMeasurements() {
        if (mExampleString == null)
            return;
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap bitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas tempCanvas = new Canvas(bitmap);

        if(isInEditMode()){
            invalidation = true;
            progress = 20;
        }else
            if (!invalidation)
                if (!checked)
                    return;


        int width = getWidth();
        int height = getHeight();

        Paint paint = new Paint();
        paint.setColor(mExampleColor);

        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(mExampleDimension);
        paint.setStyle(Paint.Style.STROKE);
        //canvas.drawLine(width * 0.025f, height * 0.45f, width * 0.375f, height * 0.8f, paint);
        //canvas.drawLine(width * 0.35f, height * 0.75f, width * 0.95f, height * 0.15f, paint);
        //canvas.drawPoints(new float[]{width * 0.025f, height * 0.45f, width * 0.375f, height * 0.8f}, paint);

        Path path = new Path();
        path.moveTo(width * 0.14f, height * 0.55f);
        path.lineTo(width * 0.375f, height * 0.78f);

        path.lineTo(width * 0.88f, height * 0.23f);
        path.moveTo(width * 0.88f, height * 0.23f);
        path.close();

        tempCanvas.drawPath(path, paint);

        if (invalidation) {
            float progressValue = progress / 20f;
            paint = new Paint();
            paint.setColor(0x00000000);
            paint.setAntiAlias(true);
            paint.setDither(true);
            //paint.setStrokeJoin(Paint.Join.ROUND);
            //paint.setStrokeCap(Paint.Cap.ROUND);


            //paint.setColor(0xCC000000);
            //temp.drawRect(0, 0, temp.getWidth(), temp.getHeight(), paint);

            paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(getHeight() * 1.5f);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

            if (!checked) {
                tempCanvas.drawLine(0, 0, width * progressValue, height * progressValue, paint);
            } else {
                tempCanvas.drawLine(width * progressValue, height * progressValue, width, height, paint);
            }
            if (progress == 20) {
                progress = 0;
                invalidation = false;
                canvas.drawBitmap(bitmap, 0, 0, new Paint());
                return;
            } else {
                postInvalidate();
                progress++;
            }
        }

        canvas.drawBitmap(bitmap, 0, 0, new Paint());
    }

    @Override
    public void invalidate() {
        super.invalidate();
    }

    @Override
    public void setChecked(boolean checked) {
        this.checked = checked;
        invalidate();
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
        invalidate();
        invalidation = true;
        progress = 0;
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        clickListener = l;
    }

    @Override
    public void onClick(View v) {
        toggle();
        if (clickListener != null) {
            clickListener.onClick(v);
        }
    }
}