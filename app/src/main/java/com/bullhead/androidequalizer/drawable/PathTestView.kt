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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference


class PathTestView(context: Context?, attrs: AttributeSet?) : View(context, attrs), Runnable, LifecycleObserver {

    private var resume = false
    var path = Path()

    val lock = Object()
    lateinit var mRandom: java.util.Random
    lateinit var yValue: IntArray
    lateinit var yRealValue: IntArray
    var startHeight = 0f

    var paint = Paint().apply {
        color = Color.parseColor("#ffffff")
        style = Paint.Style.STROKE
        strokeWidth = 5f

        //防抖动
        setDither(true)
        //抗锯齿，降低分辨率，提高绘制效率
        setAntiAlias(true)

    }
    var paintBg = Paint().apply {
        color = Color.parseColor("#dddddd")
        alpha = (0.1f * 255).toInt()
        style = Paint.Style.FILL
        setDither(true)
        //抗锯齿，降低分辨率，提高绘制效率
        setAntiAlias(true)
    }

    var paint2 = Paint().apply {
        color = Color.parseColor("#cccccc")
        style = Paint.Style.STROKE
        strokeWidth = 5f
        pathEffect = DashPathEffect(floatArrayOf(3f, 10f), 0f)
        setDither(true)
        //抗锯齿，降低分辨率，提高绘制效率
        //抗锯齿，降低分辨率，提高绘制效率
        setAntiAlias(true)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onActivityResume() {
        synchronized(lock) {
            resume = true
//            valuesYValueAnimator.start()
            lock.notify()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onActivityStop() {
        valuesYValueAnimator?.pause()
        resume = false
    }

    init {
        (context as LifecycleOwner).apply {
            lifecycle.addObserver(this@PathTestView)
        }
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

//    var readySume = 0;

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
                changeMusicDB(field)
            }
            Log.i(TAG, "音量 : musicDb = $field")
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
            yValue[index] = sunHeight.toInt()//(sunHeight - ((musicDb / 120f) * Math.random() * 0.5 * height / 3f)).toInt()
        }
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
        path.close()
        canvas.drawPath(path, paintBg)
        for (index in 1..10) {
            canvas.drawLine(100f * index, height - 10f, 100f * index, 0f, paint2)
        }

        synchronized(lock) {
//            path.reset()
            lock.notify()
        }
    }

    var valuesYValueAnimator: ValueAnimator? = null
    override fun run() {
//        sumOneMethod()
//        sumTowMethod()
//        sumthreeMethod()
//        sumFourMethod()
//        while (true) {

        val max: Float = (1 * 0.7f - 0.35f) * height / 20f
//        synchronized(lock) {
//            valuesYValueAnimator = ValueAnimator.ofInt(-5, -5,-5).apply {
//                repeatCount = -1
//                duration = 200
//                addUpdateListener {
//                    synchronized(lock) {
//                        for (index in yValue.indices) {
//                            val newValue = yValue[index] + it.animatedValue as Int
//                            yValue[index] = newValue
//                        }
//                    }
//                }
//            }
//        }
//        valuesYValueAnimator.s
        while (true) {
            path.reset()
            val intArray = IntArray(yValue.size)
            synchronized(lock) {
                for (index in yValue.indices) {
                    val newValue = getRealMixMusicDBYvalue(index)
                    intArray[index] = newValue
                }
            }

            sumFiveMethod(yValue)

            synchronized(lock) {
                if (resume) {

                    post {
                        if (valuesYValueAnimator?.isPaused == true || valuesYValueAnimator?.isStarted != true) {
                            valuesYValueAnimator?.start()
                        }
                        invalidate()
                    }
                }
//                valuesYValueAnimator.pause()
                lock.wait()
            }
            Thread.sleep(75)
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
        var y1: Float = getRealYvalue(yValue, 0)//yValue[0].toFloat()
        path.moveTo(x1, y1)

        var x3: Float
        var y3: Float
        var x2: Float
        var y2: Float
        while (index < yValue.size) {
            x2 = (mumber * index).toFloat()
            y2 = getRealYvalue(yValue, index)//yValue[index].toFloat()


            val sumX1 = (mumber * (index - 1)).toFloat()
            val sumY1 = getRealYvalue(yValue, index - 1)//yValue[index - 1].toFloat()

            val cX = (x1 + x2) / 2
            val cY = (y1 + y2) / 2

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
            y3 = getRealYvalue(yValue, index + 1)//yValue[index + 1].toFloat()


            val aX = (x2 + x3) / 2
            val aY = (y2 + y3) / 2f

            path.quadTo(x2, y2, aX, aY)
            Log.i(TAG, "sumFiveMethod: 4 path.quadTo($x2, $y2, $aX, $aY)")

            if (index + 2 < yValue.size) {
                if ((y3 < y2 && getRealYvalue(yValue, index + 2) < y3) || (y3 > y2 && getRealYvalue(yValue, index + 2) > y3)) {
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

    var musicDbLastValue = 0
    private lateinit var musicDbLastValueAnim: ValueAnimator
    private fun changeMusicDB(field: Int) {
        if (musicDbLastValue == field) {
            return
        }

        if ((context as LifecycleOwner).lifecycle.currentState != Lifecycle.State.RESUMED) {
            musicDbLastValue = field
            return
        }
//        val thisW = WeakReference(this)
        if (this::musicDbLastValueAnim.isInitialized) {
            musicDbLastValueAnim.cancel()
        }
        musicDbLastValueAnim = ValueAnimator.ofArgb(musicDbLastValue, field).apply {
            duration = 750
            addUpdateListener {
                val value = it.animatedValue as Int
                musicDbLastValue = value
                Log.i(TAG, "changeMusicDB: $value")
            }
        }
        musicDbLastValueAnim.start()
    }

    private fun getRealYvalue(yValue: IntArray, index: Int): Float {
        val yv = yValue[index]
        if (musicDbLastValue == 0) {
            return yv.toFloat()
        }
        val size_3 = yValue.size / 3
        val _music120 = musicDbLastValue / 120f
        val returnValue = (if (size_3 > index) {
            yv - _music120 * height * 0.5f
        } else if (size_3 * 2 > index) {
            yv - _music120 * height * 0.3f
        } else {
            yv - _music120 * height * 0.15f
        })

        return returnValue
    }

    var realOr = 1
    val REAL_V = 2
    var readAnimValue = -REAL_V

    private fun getRealMixMusicDBYvalue(index: Int): Int {
        val returnValue = yValue[index]
//        val returnValue = (yValue[index] + if (realOr == 1) ++readAnimValue else --readAnimValue).apply {
//            if (realOr == 1 && readAnimValue >= REAL_V + 1) {
//                realOr = 0
//            } else if (realOr == 0 && readAnimValue <= -REAL_V - 1) {
//                realOr = 1
//            }
//        }


        Log.i(TAG, "getRealMixMusicDBYvalue: musicDbLastValue = $musicDbLastValue , musicDb = $musicDb , returnValue = $returnValue , index = $index")
        return returnValue
    }


    companion object {
        const val TAG = "PathTestDrawable"
    }
}