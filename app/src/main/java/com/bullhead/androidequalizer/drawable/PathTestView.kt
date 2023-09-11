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

    var mumber = 20

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
        var sum = width.toFloat() / mumber
        val sunHeight = height.toFloat() / 2

//        while (true) {

        path.moveTo(0f, sunHeight / 2)
//        path.lineTo(width.toFloat() - sum,sunHeight-10)
        Log.i(TAG, "run: index * width.toFloat() / 50f = $sum , sunHeight = $sunHeight , yValue.size = ${yValue.size}")
        for (index in yValue.indices) {
            yValue[index] = (Math.random() * 80 - 50).toInt()
        }
//        path.quadTo(100f, sunHeight / 7, 200f, sunHeight / 2)
//        path.quadTo(250f, sunHeight / 2 + 100, 300f, sunHeight / 7)


//        path.quadTo(100f,sunHeight/7,200f,sunHeight/2)
//        path.quadTo(250f,sunHeight/2+100, 300f,sunHeight/7)

//
//            path.quadTo(width/2f,sunHeight+200,width/2f-50,sunHeight-50)


        var index = 1

//        var lastY = sunHeight / 2
        while (index < yValue.size) {

            val sx = (index * mumber).toFloat()
            val sy = yValue[index - 1].toFloat()
            if (index + 1 >= yValue.size) {
                break
            }
            val endx = ((index + 1) * mumber).toFloat()
            val endy = yValue[index].toFloat()
//                path.quadTo(sx-, sy, endx, endy)

            path.rQuadTo(sx, sy, endx, endy)

            index+=2

        }

//            path.cubicTo(width.toFloat(),sunHeight,width/2f,sunHeight+200,width/2f-50,sunHeight-50)

//            path.cubicTo(width/2f-50,sunHeight-50,width/2f-100,sunHeight+200,width/2f-150,sunHeight-50)

//            for (index in yValue.size downTo 1) {
//                if (index == 1) {
//                    path.lineTo(0f, yValue[0].toFloat())
//                } else {
//                    path.lineTo((index * mumber).toFloat(), yValue[index - 1].toFloat())
//                }
//                Log.i(TAG, "run: index * sum = ${index * mumber}, y = ${yValue[index - 1]}")
//            }

//            LineDa


        synchronized(lock) {
            post {
                invalidate()
            }
            lock.wait()
        }
        Thread.sleep(100)

    }

//    }


    companion object {
        const val TAG = "PathTestDrawable"
    }
}