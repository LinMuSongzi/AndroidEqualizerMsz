package com.bullhead.androidequalizer.drawable

import android.animation.ValueAnimator
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


class PathTestView(context: Context?, attrs: AttributeSet?) : View(context, attrs), Runnable, LifecycleObserver {
    private var valuesYValueAnimator: ValueAnimator? = null
    private var resume = false
    private var path = Path()

    private val lock = Object()
    private lateinit var mRandom: java.util.Random

    /**
     * 上一次绘制的最后的y值
     */
    private lateinit var yValue: IntArray

    /**
     * 最终绘制的终点y值
     */
    private lateinit var yRealValue: IntArray

    /**
     * 当前绘制的y值
     */
    private lateinit var thisYValue: IntArray

    /**
     * 当时开始水纹位置
     */
    var startHeight = 0f

    /**
     * 上一次的分贝值
     */
    private var musicDbLastValue = 0

    /**
     * 横坐标的数组数量，可变
     */
    private var mumber = 40
    private val sleepTime = 50L
    private val animMoveAllTime = 300f
    val limiTime = 100L
    private var lastTime = 0L

    /**
     * 水纹的画笔
     */
    private var wavePaint = Paint().apply {
        color = Color.parseColor("#ffffff")
        style = Paint.Style.STROKE
        strokeWidth = 2f

        //防抖动
        isDither = true
        //抗锯齿，降低分辨率，提高绘制效率
        setAntiAlias(true)

    }
    private var paintBg = Paint().apply {
        color = Color.parseColor("#dddddd")
        alpha = (0.1f * 255).toInt()
        style = Paint.Style.FILL
        isDither = true
        //抗锯齿，降低分辨率，提高绘制效率
        isAntiAlias = true
    }

    private var paint2 = Paint().apply {
        color = Color.parseColor("#cccccc")
        style = Paint.Style.STROKE
        strokeWidth = 5f
        pathEffect = DashPathEffect(floatArrayOf(3f, 10f), 0f)
        isDither = true
        //抗锯齿，降低分辨率，提高绘制效率
        //抗锯齿，降低分辨率，提高绘制效率
        isAntiAlias = true
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

    var musicDb: Int = 0
        set(value) {
            val thisTime = System.currentTimeMillis()
            if (thisTime - lastTime <= limiTime) {
                return
            }

            lastTime = thisTime

            if (value >= 120) {
                field = 120
            } else if (value < 0) {
                field = 0
            }
            field = value
//            resetYvalue(field)
            changeMusicDB(field)
            Log.i(TAG, "音量 : musicDb = $field")
        }


    private fun resetYvalue(musicDb: Int, isCopyToo: Boolean = false) {
        if (!this::yValue.isInitialized) {
            return
        }
        synchronized(lock) {
            for (index in yValue.indices) {
                val sunHeight = if (startHeight == 0f) height * 0.75f else startHeight
                if (startHeight == 0f) {
                    startHeight = sunHeight
                }
                val i = (sunHeight - musicDb / 60f * height * 0.25f * Math.random() * 0.5f).toInt()
                if (isCopyToo) {
                    yValue[index] = i //(sunHeight - ((musicDb / 120f) * Math.random() * 0.5 * height / 3f)).toInt()
                }
                yRealValue[index] = i
            }
        }
    }


    override fun onDraw(canvas: Canvas) {
        if (!this::mRandom.isInitialized) {
            mRandom = java.util.Random()
            val size = (width * 1f / mumber).let {
                if (it > it.toInt()) {
                    (it + 2).toInt()
                } else {
                    (it + 1).toInt()
                }
            }
//            mumber = size
            yValue = IntArray(size)
            yRealValue = IntArray(size)
            thisYValue = IntArray(size)
            Log.i(TAG, "onDraw: mumber = $mumber , size = $size , size * mumber = ${size * mumber}")
            resetYvalue(musicDb, true)
            Thread(this).start()
        }
        canvas.drawPath(path, wavePaint)
        path.close()
        canvas.drawPath(path, paintBg)
        for (index in 1..10) {
            canvas.drawLine(100f * index, height - 10f, 100f * index, 0f, paint2)
        }
    }


    override fun run() {

        while (true) {
            path.reset()
            synchronized(lock) {
                for (index in yValue.indices) {
                    val newValue = getRealMixMusicDBYvalue(index)
                    thisYValue[index] = newValue
                }
                Log.i(TAG, "run: sumFiveMethod ")
                sumFiveMethod(thisYValue)
            }

            synchronized(lock) {
                if ((context as LifecycleOwner).lifecycle.currentState != Lifecycle.State.RESUMED) {
                    lock.wait()
                }
                post {
                    invalidate()
                }
            }
            Thread.sleep(sleepTime)
        }
    }

    /**
     * 绘制线
     */
    private fun sumFiveMethod(yValue: IntArray): IntArray {
        var index = 1
        var x1 = 0f
        var y1: Float = getRealYvalue(yValue, 0)
        path.moveTo(x1, y1)

        var x3: Float
        var y3: Float
        var x2: Float
        var y2: Float
        while (index < yValue.size) {
            x2 = (mumber * index).toFloat()
            y2 = getRealYvalue(yValue, index)


            val sumX1 = (mumber * (index - 1)).toFloat()
            val sumY1 = getRealYvalue(yValue, index - 1)

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
            y3 = getRealYvalue(yValue, index + 1)


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
        return yValue
    }

    /**
     * 音量改变
     */
    private fun changeMusicDB(field: Int) {
        if (musicDbLastValue == field) {
            return
        }

        if ((context as LifecycleOwner).lifecycle.currentState != Lifecycle.State.RESUMED) {
//            musicDbLastValue = field
            return
        }

        if (!::yRealValue.isInitialized) {
            return
        }

        synchronized(lock) {
            val _11 = yRealValue.size / 5f
            val _22 = _11 * 2
            val _33 = _11 * 3
            val _44 = _11 * 4
            val _55 = _11 * 5


            for (index in yRealValue.indices) {
                if (index >= _11 && index < _44 && Math.random() > 0.5) {
                    yRealValue[index] = (startHeight - field / 60f * height * (Math.random() * 0.3 + 0.5)).toInt()
                } else if (index >= _33 && index < _55 && Math.random() > 0.5) {
                    yRealValue[index] = (startHeight - field / 60f * height * 0.75f * (Math.random() * 0.2 + 0.2)).toInt()
                } else if (index <= _11) {
                    yRealValue[index] = (startHeight - field / 60f * height * (Math.random() * 0.2 + 0.1)).toInt()
                } else {
                    yRealValue[index] = (startHeight - field / 60f * height * 0.5f * (Math.random() * 0.2 + 0.2)).toInt()
                }
                yValue[index] = thisYValue[index]
            }
        }
    }

    /**
     * 这个值将会被写入path 的轨迹中
     */
    private fun getRealYvalue(yValue: IntArray, index: Int): Float {
        val yv = yValue[index]
        if (musicDbLastValue == 0) {
            return yv.toFloat()
        }
        return yv.toFloat()
    }

    private fun getRealMixMusicDBYvalue(index: Int): Int {
        var old = yValue[index]
        var new = yRealValue[index]
        val thisv = thisYValue[index]

        val checkValue = new - thisv

        if (thisv < 2 && thisv > -2) {
            return new
        }
        return (thisYValue[index] + checkValue / animMoveAllTime * sleepTime).toInt()
    }


    companion object {
        const val TAG = "PathTestDrawable"
    }
}