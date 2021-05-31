package com.example.toeicapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class LoadingIndicator extends AppCompatImageView {
    private final int PURPLE = 17170457;
    private int[] colors = new int[] {Color.BLUE, Color.GREEN, Color.YELLOW, Color.RED};
    private int[] colors2 = new int[] {0xFF0D1333, 0xFF61688B, 0xFFA0A5BD};
    private Paint paint;
    private int stroke = 10;

    public LoadingIndicator(@NonNull Context context) {
        super(context);
    }
    public LoadingIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStrokeWidth(stroke);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Shader shader = new SweepGradient(getWidth() / 2, getHeight() / 2, colors2, null);
        paint.setShader(shader);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - stroke, paint);
    }
}
