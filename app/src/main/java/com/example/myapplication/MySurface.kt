package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import kotlin.random.Random

class MySurface(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    var mThread: MyThread
    var mThread2: MyThread

    var backgroundThread: BackgroundThread
    lateinit var mHolder: SurfaceHolder
    var mContext = context

    var touchX: Float = 0.0f
    var touchY: Float = 0.0f

    var dWidth = 0
    var dHeight: Int = 0

    var planeArray = arrayListOf<MyThread>()

    //    var plane = arrayListOf<MyPlane>()
    var score = 0

    init {
        holder.addCallback(this)
        backgroundThread = BackgroundThread(holder, context)
        mThread = MyThread(holder, context)
        mThread2 = MyThread(holder, context)
        InitApp()

    }

    private fun InitApp() {
        var display: Display = (context as Activity).windowManager.defaultDisplay
        dWidth = display.width
        dHeight = display.height
    }

    override fun surfaceCreated(p0: SurfaceHolder) {

//        mThread2.start()
//        backgroundThread.start()
        mThread.start() //Thread시작
        Log.d("Thread", "start")

    }

    override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
//        if (planeArray.size < 5) {
//            var newThread = MyThread(holder, context)
//            planeArray.add(newThread)
//
//            newThread.start()
//
//        }
        mThread.plane.move()
    }

    override fun surfaceDestroyed(p0: SurfaceHolder) {

    }


    inner class MyThread(holder: SurfaceHolder, context: Context) : Thread() {
        var plane: MyPlane

        init {
            mHolder = holder
            mContext = context
            var random = Random
            var X = random.nextInt(300)
            var Y = dWidth + random.nextInt(600)
            plane = MyPlane(X, Y, 30)
//            makeQuestion()
//            plane.add(MyPlane(200,300,30))
        }

        var background: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.background)
        var original = BitmapFactory.decodeResource(resources, R.drawable.rocket)
        val planeImg = Bitmap.createScaledBitmap(original, 100, 100, true)

        fun drawEverything(canvas: Canvas) {

//            plane.add(MyPlane(X, Y, 30))
//                canvas.drawBitmap(planeImg, X.toFloat(), Y.toFloat(), null)

            canvas.drawBitmap(background, null, Rect(0,0,dWidth,dHeight) , null)
//            canvas.drawBitmap(background, null, Rect(0,0,200,100) , null)
            var paint = Paint()
            paint.setColor(Color.RED)
            paint.textSize = 20F
            canvas.drawText(score.toString(), 30F, 50F, paint)

//            var iter = plane.iterator()
//            while (iter.hasNext()) {
//                var aplin = iter.next()
            canvas.drawBitmap(planeImg, plane.planeX.toFloat(), plane.planeY.toFloat(), null)
//            }

//
//            canvas.drawBitmap(plane, planeX.toFloat(),planeY.toFloat(),null)
////            canvas.drawBitmap(plane, planeX.toFloat(),planeY.toFloat(),null)
////            canvas.drawBitmap(plane, planeX.toFloat(),planeY.toFloat(),null)
//            canvas.drawRect(0F,0F,100F,100F,paint)
            movePlane()
        }

        fun movePlane() {
            var iter = planeArray.iterator()
//            while (iter.hasNext()) {
//                var aplin = iter.next()
//                aplin.move()
//                if (aplin.planeY > dHeight) iter.remove()
//            }
            while (iter.hasNext()) {
                val thread = iter.next()
                thread.plane.move()
                if (thread.plane.planeY > dHeight) {
                    thread.plane.planeY=Random.nextInt(dHeight)
                    invalidate()
                }

            }
        }

        override fun run() {
            var canvas: Canvas
            while (true) {
                canvas = mHolder.lockCanvas()
                try {
                    synchronized(mHolder) {
                        drawEverything(canvas)
                    }
                } finally {
                    if (canvas != null) mHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val y = event?.y
        val x = event?.x
        var returnValue = true
        var iter = planeArray.iterator()
        while (iter.hasNext()) {
            var aplin = iter.next()
            Log.d("onTouchEvent", "${aplin.plane.planeY} , ${aplin.plane.planeX}")
            if (x != null && y != null) {
                // 이미지 클릭하면
                if ((x - 20 <= aplin.plane.planeX && x + 20 >= aplin.plane.planeX && y - 20 <= aplin.plane.planeY && y + 20 >= aplin.plane.planeY)) {
                    aplin.plane.planeY = dHeight
                    Log.v("onTouchEvent", "x : ${event.x}, y : ${event.y}")
                    score++
                    Log.v("onTouchEvent", "score : $score")
                    iter.remove()
                    Thread.currentThread().interrupt()
                    invalidate()
                }
            }
        }


        return true
    }

    inner class BackgroundThread(holder: SurfaceHolder, context: Context) : Thread() {
        init {
            mHolder = holder
            mContext = context

        }

        var background: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.background)

        fun drawEverything(canvas: Canvas) {
            canvas.drawBitmap(background, null, Rect(0, 0, dWidth, dHeight), null)
        }


        override fun run() {
            var canvas: Canvas
            while (true) {
                canvas = mHolder.lockCanvas()
                try {
                    synchronized(mHolder) {
                        drawEverything(canvas)
                    }
                } finally {
                    if (canvas != null) mHolder.unlockCanvasAndPost(canvas)
                }
            }
        }
    }

}