package com.bullhead.androidequalizer.waveview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class Wave33View extends View {
    private Paint grayPaint; // 灰色波浪画笔
    private Paint greenPaint; // 绿色波浪画笔
    private int waveHeight = 80; // 波浪高度
    private int waveWidth = 360; // 波浪宽度
    private int offset = 0; // 波浪偏移量
    private Path grayPath = new Path(); // 灰色波浪路径
    private Path greenPath = new Path(); // 绿色波浪路径
    private int centerX; // 中心x坐标
    private int centerY; // 中心y坐标

    public Wave33View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // 初始化灰色波浪画笔
        grayPaint = new Paint();
        grayPaint.setColor(Color.parseColor("#e3e3e3"));
        grayPaint.setStyle(Paint.Style.FILL);
        grayPaint.setAntiAlias(true);

        // 初始化绿色波浪画笔
        greenPaint = new Paint();
        greenPaint.setColor(Color.parseColor("#20be85"));
        greenPaint.setStyle(Paint.Style.FILL);
        greenPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        centerX = MeasureSpec.getSize(widthMeasureSpec) / 2;
        centerY = MeasureSpec.getSize(heightMeasureSpec) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制灰色波浪路径
        grayPath.reset();
        grayPath.moveTo(-waveWidth + offset, centerY);
        for (int i = -waveWidth; i <= getWidth() + waveWidth; i += waveWidth) {
            grayPath.rQuadTo(waveWidth / 4, -waveHeight, waveWidth / 2, 0);
            grayPath.rQuadTo(waveWidth / 4, waveHeight, waveWidth / 2, 0);
        }
        grayPath.lineTo(getWidth(), getHeight());
        grayPath.lineTo(0, getHeight());
        grayPath.close();
        canvas.drawPath(grayPath, grayPaint);

        // 绘制绿色波浪路径
        greenPath.reset();
        greenPath.moveTo(-waveWidth + offset, centerY);
        for (int i = -waveWidth; i <= getWidth() + waveWidth; i += waveWidth) {
            greenPath.rQuadTo(waveWidth / 4, -waveHeight, waveWidth / 2, 0);
            greenPath.rQuadTo(waveWidth / 4, waveHeight, waveWidth / 2, 0);
        }
        greenPath.lineTo(getWidth(), getHeight());
        greenPath.lineTo(0, getHeight());
        greenPath.close();
        canvas.drawPath(greenPath, greenPaint);
    }

    public void setOffset(int offset) {
        this.offset = offset;
        invalidate();
    }
}