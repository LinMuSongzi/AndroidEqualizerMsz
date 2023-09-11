package com.bullhead.androidequalizer.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View


class PathTestView(context: Context?, attrs: AttributeSet?) : View(context, attrs), Runnable {

    var path = Path()

    val lock = Object()
    lateinit var mRandom: java.util.Random

    lateinit var yValue: IntArray

    var paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 2.5f
    }

    var paint2 = Paint().apply {
        color = Color.parseColor("#a9c8b1")
        style = Paint.Style.STROKE
        strokeWidth = 2f
        pathEffect = DashPathEffect(floatArrayOf(3f, 10f), 0f)
    }

    init {

    }

//    override fun onResume(owner: LifecycleOwner) {
//        if (!join) {
//            mRandom = Random((view.measuredHeight * 0.75).toInt())
//            yValue = IntArray(view.width / 2)
//            Log.i(TAG, "onResume: view.measuredHeight * 0.75 = ${view.measuredHeight * 0.75}")
//            Log.i(TAG, "run: view.width / 2 = ${view.measuredWidth / 2}")
//            Thread(this).start()
//            join = true
//        }
//    }

    var mumber = 40

    override fun onDraw(canvas: Canvas) {
        if (!this::mRandom.isInitialized) {
            mRandom = java.util.Random()
            yValue = IntArray(width / mumber)
            Thread(this).start()
        }
        canvas.drawPath(path, paint)

        for (index in 1..10) {
            canvas.drawLine(100f * index, height - 10f, 100f * index, 0f, paint2)
        }

        synchronized(lock) {
            path.reset()
            lock.notify()
        }
    }

    override fun run() {
//        sumOneMethod()
//        sumTowMethod()
        sumthreeMethod()
        synchronized(lock) {
            post {
                invalidate()
            }
            lock.wait()
        }
        Thread.sleep(100)
    }

    private fun sumthreeMethod() {
        var sum = width.toFloat() / mumber
        val sunHeight = height.toFloat() / 2


        for (index in yValue.indices) {
            yValue[index] = (sunHeight + 50 * if (index % 2 == 0) -1 else 1).toInt()
            Log.i(TAG, "sumTowMethod: yValue[$index] = ${yValue[index]}")
        }
        path.moveTo(0f, yValue[0].toFloat())
        var index = 0
//        var x1:Float= (-mumber).toFloat()
        var y1: Float = yValue[0].toFloat()
        while (index < yValue.size) {

            if (index + 1 >= yValue.size) {
                break
            }

            val x2 = mumber * (index + 1) * 1.0f
            val y2 = yValue[index + 1].toFloat()

            if (index + 2 >= yValue.size) {
                path.lineTo(x2, y2)
                break
            }
            val x3 = mumber * (index + 2) * 1.0f
            val y3 = yValue[index + 2].toFloat()

            var x4 = 0f
            var y4 = 0f
            if (index + 3 < yValue.size) {
//                path.lineTo(x2, y2)
                x4 = mumber * (index + 3) * 1.0f
                y4 = yValue[index + 3].toFloat()
            }


//            if (y4 != 0f && (y4 >= y2 && y4 < y3) || (y4 > y3 && y4 <= y2)) {
//                path.cubicTo(x2, y2, x3, y3, x4, y4)
//                y1 = y4
//                index += 3
//            } else
                if ((y2 < y1 && y3 < y1) || ((y2 > y1 && y3 > y1))) {
                path.lineTo(x2, y2)
                y1 = y2
                index++
            } else {
                path.quadTo(x2, y2, x3, y3)
                y1 = y3
                index += 2
            }
        }

    }

    private fun sumTowMethod() {
        var sum = width.toFloat() / mumber
        val sunHeight = height.toFloat() / 2
        path.moveTo((-mumber).toFloat(), sunHeight)

        for (index in yValue.indices) {
            yValue[index] = (sunHeight + 50 * if (index % 2 == 0) -1 else 1).toInt()
            Log.i(TAG, "sumTowMethod: yValue[$index] = ${yValue[index]}")
        }
        var index = 0
        while (index < yValue.size) {
            val sx = mumber * index * 1.0f
            val sy = yValue[index].toFloat()
//
            val endx = mumber * index * 1.0f
            val endy = yValue[index + 1].toFloat()

            path.quadTo(sx, sy, endx, endy)
            index += 2
        }
    }

    private fun sumOneMethod() {
        var sum = width.toFloat() / mumber
        val sunHeight = height.toFloat() / 2

//        while (true) {

        path.moveTo(0f, sunHeight)
//        path.lineTo(width.toFloat() - sum,sunHeight-10)
        Log.i(TAG, "run: index * width.toFloat() / 50f = $sum , sunHeight = $sunHeight , yValue.size = ${yValue.size}")
        for (index in yValue.indices) {
            yValue[index] = (sunHeight + ((Math.random() * 2 - 1) * sunHeight / 10f)).toInt()
        }

        var index = 0

        while (index < yValue.size) {

            val sx = ((index + 1) * mumber).toFloat()
            val sy = yValue[index].toFloat()
            if (index + 1 >= yValue.size) {
                break
            }
            val endx = ((index + 1) * mumber).toFloat()
            val endy = yValue[index].toFloat()
            Log.i(TAG, "run: path.quadTo(sx, sy, endx, endy) = path.quadTo($sx, $sy, $endx, $endy)")
            path.quadTo(sx, sy, endx, endy)

            index += 2

        }
        path.quadTo(width.toFloat(), height.toFloat() / 2, (width + 10).toFloat(), height.toFloat() / 2)


    }

//    }


    companion object {
        const val TAG = "PathTestDrawable"
    }
}