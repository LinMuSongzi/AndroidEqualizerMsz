package com.bullhead.androidequalizer.lifecycle

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
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
        strokeWidth = 5f
    }

    val path = Path()
    var xArray: FloatArray? = null
    var widthSum = 10
    var position = 0

    val lock = Object()

    private val runnableInvalidate = Runnable {
        invalidate()
    }


    var valuesQueue = LinkedBlockingQueue<Float>(100);

    var lastY = 0f;
    var lastX = 0f;
    fun upData(d: Int) {
        val sum = height / 120f
        val hValue = (120 - d) * sum
        Log.i(TAG, "upData: db = $d , height = $hValue")

        if (widthSum == 0) {
            widthSum = width / 100
        }

        if (position >= width) {
            position = 0

            return
        }
        if (position == 0) {
            path.reset()
            path.moveTo(width.toFloat(), hValue)
            lastX = width.toFloat()
            lastY = hValue
            positionPlus()

            return
        } else {
//            path.offset(position.toFloat(), hValue)
            positionPlus()
        }

        val lx = (width - position).toFloat()


        val lx2 = (lastX + lx) / 2
        val ly2 = (lastY + hValue) / 2 + (Math.random() * 5 + 10) * (if (lastY > hValue) -1 else 1)
//        path.cubicTo(lastX, lastY, lx2, ly2.toFloat(), lx, hValue)
        path.cubicTo(lastX, lastY, lx2, ly2.toFloat(), lx, hValue)

        lastX = lx
        lastY = hValue
//        synchronized(lock) {
//            post(runnableInvalidate)
//            lock.wait()
//        }
    }

    private fun positionPlus() {
        position += 25;
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        synchronized(lock) {

//            path.moveTo(0f, height / 2f)
//
//            path.quadTo(
//                0f,
//                height / 2f,
//                100f,
//                height / 2f + height/4f,
//            )
//            path.quadTo(
//                200f,
//                height / 2f - height/4f,
//                300f,
//                height / 2f + height/4f,
//            )
//            path.quadTo(
//                400f,
//                height / 2f,
//                500f,
//                height / 2f + height/4f,
//            )

            canvas.drawPath(path, paint)
            lock.notify()
        }
    }


    companion object {
        const val TAG = "VolumeView"
    }
}