package com.example.myapplication

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.View
import java.util.*

class PlaneAnim(context: Context?) : View(context) {
    var background: Bitmap
    var plane: Bitmap
    var planeVelocity=20
    var planeX: Int
    var planeY: Int
    var dWidth: Int
    var dHeigth: Int
    var dest : Rect
    var planeFrame:Int=0
    var random:Random
    var planeWidth:Int = 0
    var planeHeight:Int = 0
    val UPDATE_MILLIS:Long=30
    lateinit var runnable: Runnable
    var score=0
    var paint:Paint

    var touchY:Float? =null
     var touchX:Float? =null

    init {
        background = BitmapFactory.decodeResource(resources, R.drawable.background)
        var original = BitmapFactory.decodeResource(resources, R.drawable.rocket)
        plane=Bitmap.createScaledBitmap(original,80,80,true)

        random = Random()
        var display: Display = (context as Activity).windowManager.defaultDisplay
        var size = Point()
        display.getSize(size)
        dWidth = size.x
        dHeigth = size.y
        planeX = dWidth+random.nextInt(200)
        planeY = random.nextInt(100)
        dest = Rect(0,0,dWidth,dHeigth)
        paint = Paint()
        paint.setColor(Color.rgb(8,5,3))
        runnable=Runnable{
             kotlin.run {
                 invalidate()
             }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            canvas.drawBitmap(background, null, dest, null)

            canvas.drawBitmap(plane, planeX.toFloat(),planeY.toFloat(),null)
//            canvas.drawBitmap(plane, planeX.toFloat(),planeY.toFloat(),null)
//            canvas.drawBitmap(plane, planeX.toFloat(),planeY.toFloat(),null)
            canvas.drawRect(0F,0F,100F,100F,paint)
        }
        postDelayed(runnable,UPDATE_MILLIS)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event!=null) {
            touchX = event.x
            touchY=event.y

            Log.d("touch : ", "x : $touchX, y: $touchY")
        }
        return true
    }

}