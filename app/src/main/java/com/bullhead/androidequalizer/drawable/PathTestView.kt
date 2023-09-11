package com.bullhead.androidequalizer.drawable

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import java.nio.channels.FileLock
import kotlin.random.Random


class PathTestView(context: Context?, attrs: AttributeSet?) : View(context, attrs), Runnable {

    var path = Path()

    val lock = Object()
    lateinit var mRandom: java.util.Random
    lateinit var yValue: IntArray

    var startHeight = 0f

    var paint = Paint().apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 2.5f
    }

    var paint2 = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
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

    var musicDb: Int = 30
        set(value) {
            if (value >= 120) {
                field = 120
            } else if (value < 0) {
                field = 0
            }
            field = value
            synchronized(lock) {
                resetYvalue()
            }
            Log.i(TAG, "音量 : musicDb = $field")
        }

    override fun onDraw(canvas: Canvas) {
        if (!this::mRandom.isInitialized) {
            mRandom = java.util.Random()
            var size = width / mumber
            if (size * mumber <= width) {
                size++
            }
            yValue = IntArray(size)
            Log.i(TAG, "onDraw: mumber = $mumber , size = $size , size * mumber = ${size * mumber}")
            resetYvalue()
            Thread(this).start()
        }
        canvas.drawPath(path, paint)

        for (index in 1..10) {
            canvas.drawLine(100f * index, height - 10f, 100f * index, 0f, paint2)
        }

        synchronized(lock) {
//            path.reset()
            lock.notify()
        }
    }

    private fun resetYvalue() {
        if (!this::yValue.isInitialized) {
            return
        }
        for (index in yValue.indices) {
            val sunHeight = if (startHeight == 0f) height * 0.75f else startHeight
            if (startHeight == 0f) {
                startHeight = sunHeight
            }
            yValue[index] = (sunHeight - ((musicDb / 120f) * Math.random() * 0.5 * height / 3f)).toInt()
        }
    }

    override fun run() {
//        sumOneMethod()
//        sumTowMethod()
//        sumthreeMethod()
//        sumFourMethod()
//        while (true) {
        val max: Float = (1 * 0.7f - 0.35f) * height / 20f

        while (true) {
            path.reset()
            val intArray = IntArray(yValue.size)
            synchronized(lock) {
                for (index in yValue.indices) {

                    val newValue = (yValue[index] + (Math.random() - 0.35f) * height / 100f).toInt()

                    intArray[index] = newValue
                }
            }

            sumFiveMethod(intArray)

            synchronized(lock) {
                post {
                    invalidate()
                }
                lock.wait()
            }
            Thread.sleep(50)
            (context as? Activity)?.apply {
                if (isFinishing) {
                    return@run
                }
            }
//            musicDb = (Math.random() * 30 + 30).toInt()
        }
    }

    private fun sumFiveMethod(yValue: IntArray): IntArray {
//        var sum = width.toFloat() / mumber
//        val sunHeight = if (startHeight == 0f) height * 0.3f else startHeight
//        Log.i(TAG, "sumFiveMethod: sunHeight = $sunHeight")


        var index = 1
        var x1 = 0f
        var y1: Float = yValue[0].toFloat()
        path.moveTo(x1, yValue[0].toFloat())

        var x3 = 0f
        var y3 = 0f
        var x2 = 0f
        var y2: Float
        while (index < yValue.size) {
            x2 = (mumber * index).toFloat()
            y2 = yValue[index].toFloat()


            var sumX1 = (mumber * (index - 1)).toFloat()
            var sumY1 = yValue[index - 1].toFloat()

            var cX = (x1 + x2) / 2
            var cY = (y1 + y2) / 2

            if (x1 != 0f && x1 != sumX1) {
                path.quadTo(sumX1, sumY1, (sumX1 + x2) / 2, (sumY1 + y2) / 2)
                Log.i(TAG, "sumFiveMethod: 1 path.quadTo($sumX1, $sumY1, ${(sumX1 + x2) / 2}, ${(sumY1 + y2) / 2})")
//                path.lineTo(x2, y2)
            } else {
                path.lineTo(cX, cY)
                Log.i(TAG, "sumFiveMethod: 2 path.lineTo($cX, $cY)")
            }



            if (index + 1 >= yValue.size) {
                path.lineTo(x2, y2)
                Log.i(TAG, "sumFiveMethod: 3 path.lineTo($x2, $y2)")
                break
            }

            x3 = (mumber * (index + 1)).toFloat()
            y3 = yValue[index + 1].toFloat()


            var aX = (x2 + x3) / 2
            var aY = (y2 + y3) / 2f

            path.quadTo(x2, y2, aX, aY)
            Log.i(TAG, "sumFiveMethod: 4 path.quadTo($x2, $y2, $aX, $aY)")

            if (index + 2 < yValue.size) {
                if ((y3 < y2 && yValue[index + 2] < y3) || (y3 > y2 && yValue[index + 2] > y3)) {
                    path.lineTo(x3, y3)
                    Log.i(TAG, "sumFiveMethod: 5 path.lineTo($x3, $y3)")
                    x1 = x3
                    y1 = y3
                } else {
                    x1 = aX
                    y1 = aY
                }
            }

            index += 2
        }

//        if (x2 > x3) {
//            path.quadTo(x3 + mumber, y3 - mumber, width.toFloat(), height.toFloat() / 2)
//        } else {
//            path.quadTo(x3 + mumber, y3 + mumber, width.toFloat(), height.toFloat() / 2)
//        }
        return yValue
    }


    companion object {
        const val TAG = "PathTestDrawable"
    }
}