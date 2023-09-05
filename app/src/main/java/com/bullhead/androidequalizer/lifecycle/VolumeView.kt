package com.bullhead.androidequalizer.lifecycle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.log10

class VolumeView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }

    var position = -1
    private val runnableInvalidate = Runnable {
        invalidate()
    }


    var valuesQueue = LinkedBlockingQueue<Float>(100);


    fun upData(d: Int) {
        val sum = height / 120f
        val hight = d * sum
        Log.i(TAG, "upData: db = $d , height = $hight")

        valuesQueue.offer(hight)
        post(runnableInvalidate)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (position == width) {
            position = -1
        }
        val y = valuesQueue.poll()
        if (y != null) {

            canvas.drawPoint((++position).toFloat(), y, paint)
        }
    }


    companion object {
        const val TAG = "VolumeView"
    }
}