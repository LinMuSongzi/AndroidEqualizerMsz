package com.bullhead.androidequalizer.lifecycle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;
import org.jtransforms.fft.DoubleFFT_1D;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FFtView extends View {
    private List fftDatas = new ArrayList();

    public static final String TAG = "FFtView";

    @NonNull
    private Paint paint = new Paint();

    public FFtView(Context context) {
        super(context);
        initPaint();
    }

    public FFtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    public void update(@NotNull short[] buffer, int bytesRead) {
// 2. 使用FFT转换为频谱数据
        DoubleFFT_1D fft = new DoubleFFT_1D(bytesRead);
        double[] fftData = new double[bytesRead * 2];
        for (int i = 0; i < bytesRead; i++) {
            fftData[i] = (double) buffer[i];
        }
        fft.realForward(fftData);
        fftDatas.add(reduceFFTData(fftData, 100));
        post(() -> invalidate());

    }


    private float lineWidth = 0f;
    private float heightScale = 0f;

    public double[] reduceFFTData(double[] fftData, int downsampleRate) {
        int targetSize = fftData.length / downsampleRate;
        double[] reducedData = new double[targetSize];
        for (int i = 0; i < targetSize; i++) {
            double sum = 0;
            for (int j = 0; j < downsampleRate; j++) {
                sum += fftData[i * downsampleRate + j];
            }
            reducedData[i] = sum / downsampleRate;
        }
        return reducedData;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!fftDatas.isEmpty()) {

//            if (heightScale == 0) {
//                heightScale = getHeight() / 120f;
//            }
//
//
//            double[] fftData = (double[]) fftDatas.get(0);
//
//            lineWidth = getWidth() * 1f / fftData.length;
//
//            for (int i = 0; i < fftData.length; i++) {
//                float x = i * lineWidth;
//                float y = (float) (fftData[i] * heightScale)* 10;
//                Log.i(TAG, "onDraw: x = " + x + " , y = " + y);
//                canvas.drawLine(x, getHeight()/2, x, (getHeight()/2) - y, paint);
//            }
        }


    }
}
