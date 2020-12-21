package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_game.view.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*
import kotlin.random.Random


/**
 * This class demonstrates the following interactive game basics:
 *
 * * Manages a rendering thread that draws to a SurfaceView.
 * * Basic game loop that sleeps to conserve resources.
 * * Processes user input to update game state.
 * * Uses clipping as a means of animation.
 *
 * Note that these are basic versions of these techniques.
 * Non-fatal edge cases are not handled.
 * Error handling is minimal. No logging. App assumes and uses a single thread.
 * Additional thread management would otherwise be necessary. See code comments.
 */
class GameView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null
) :
    SurfaceView(mContext, attrs), Runnable {
    private var mRunning = false
    private var mGameThread: Thread? = null
    private var mPath: Path
    private var mFlashlightCone: FlashlightCone? = null
    private var mPaint: Paint
    private var mBitmap: Bitmap? = null
    private var mWinnerRect: RectF? = null
    private var mBitmapX = 0
    private var mBitmapY = 0
    private var mViewWidth = 0
    private var mViewHeight = 0
    private var mSurfaceHolder: SurfaceHolder
    private var backgourndBitmap: Bitmap? = null
    var whitePaint = Paint()
    var blackPaint = Paint()

    var touchX = 0F
    var touchY = 0F

    class Roket(var x: Int, var y: Int, var roketRect: RectF, var circleImg: Bitmap) {
        val time = System.currentTimeMillis()
    }

    var roketArray = mutableListOf<Roket>()
    var circleArray = mutableListOf<Bitmap>()

    lateinit var scoreBoard: Bitmap
    lateinit var retry: Bitmap
    lateinit var save: Bitmap
    lateinit var star: Bitmap

    private var score = 0

    var life = 3
    var starArr= arrayListOf<Bitmap>()

    var timer = Timer()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        mViewHeight = h
//        mFlashlightCone = FlashlightCone(mViewWidth, mViewHeight)

        // Set font size proportional to view size.
        mPaint.textSize = 50F
        var original = BitmapFactory.decodeResource(resources, R.drawable.goldring)
        mBitmap = Bitmap.createScaledBitmap(original, 200, 200, true)

        var originalBackground = BitmapFactory.decodeResource(resources, R.drawable.universe)
        backgourndBitmap = Bitmap.createScaledBitmap(
            originalBackground,
            mViewWidth,
            mViewHeight,
            true
        )

        var originSqare = BitmapFactory.decodeResource(resources, R.drawable.purplesquare)
        scoreBoard = Bitmap.createScaledBitmap(
            originSqare, mViewWidth * 3 / 5, mViewHeight * 3 / 5, true
        )

        val circle1 = BitmapFactory.decodeResource(resources, R.drawable.fancycircle)
        val circle2 = BitmapFactory.decodeResource(resources, R.drawable.bluecircle)
        circleArray.add(Bitmap.createScaledBitmap(circle1, 200, 200, true))
        circleArray.add(Bitmap.createScaledBitmap(circle2, 200, 200, true))

        val originRetry = BitmapFactory.decodeResource(resources, R.drawable.retry)
        val originSave = BitmapFactory.decodeResource(resources, R.drawable.save)
        val originStar = BitmapFactory.decodeResource(resources, R.drawable.lifestar)
        retry = Bitmap.createScaledBitmap(originRetry, 200, 200, true)
        save = Bitmap.createScaledBitmap(originSave, 200, 200, true)
        star = Bitmap.createScaledBitmap(originStar, 100, 100, true)

//        mBitmap = BitmapFactory.decodeResource(
//            mContext.resources, R.drawable.rocket
//        )
//        mBitmap.cr
//        setUpBitmap()
        roketArray.add(setUpBitmap())
        starArr.clear()
        starArr.add(star)
        starArr.add(star)
        starArr.add(star)
        starArr.add(star)
//        roketArray.add(setUpBitmap())
    }

    /**
     * Runs in a separate thread.
     * All drawing happens here.
     */
    override fun run() {
        var canvas: Canvas
        while (mRunning) {
            // If we can obtain a valid drawing surface...
            if (mSurfaceHolder.surface.isValid) {

                canvas = mSurfaceHolder.lockCanvas()

//                canvas.save()
                backgourndBitmap?.let { canvas.drawBitmap(it, 0F, 0F, null) }
                whitePaint.color = Color.WHITE
                whitePaint.textSize = 100F
                blackPaint.color = Color.BLACK
                blackPaint.textSize = 100F
                if (life <= 0) {
                    Log.d("life is under 0", "true")

//                    var whiteboard=RectF(mViewWidth/5F,mViewHeight/5F,mViewWidth*4/5F,mViewHeight*4/5F)
//                    canvas.drawRect(
//                        whiteboard,
//                        whitePaint
//                    )
                    canvas.drawBitmap(scoreBoard, mViewWidth / 5F, mViewHeight / 5F, null)
                    canvas.drawText(
                        "Score : $score", (mViewWidth * 2 / 5).toFloat(),
                        mViewHeight * 2 / 5F, whitePaint
                    )
                    canvas.drawBitmap(retry, mViewWidth*2 / 6F, mViewHeight*6 / 13F, null)
                    canvas.drawBitmap(save, mViewWidth*8 / 14F, mViewHeight*6 / 13F, null)

                    if (touchX>mViewWidth*2 / 6F && touchX<mViewWidth*2 / 6F + 200 && touchY>mViewHeight*6 / 13F && touchY<mViewHeight*6 / 13F+200){

                        Log.d("save","click save, ${mViewWidth*2 / 6F}, ${mViewHeight*6 / 13F}")
//                        Toast.makeText(rootView.context,"재시작",Toast.LENGTH_SHORT).show()
                    }else if(touchX>mViewWidth*2 / 6F && touchX<mViewWidth*2 / 6F + 200 && touchY>mViewHeight*6 / 13F && touchY<mViewHeight*6 / 13F+200){
                        Toast.makeText(this.context,"저장되었습니다.",Toast.LENGTH_SHORT).show()
                    }

                } else {
                    var iter = roketArray.listIterator()
                    while (iter.hasNext()) {
                        var bRoket = iter.next()
                        if (touchX > bRoket.roketRect.left && touchX < bRoket.roketRect.right && touchY > bRoket.roketRect.top && touchY < bRoket.roketRect.bottom) {
                            iter.set(setUpBitmap())
                            canvas.drawBitmap(
                                bRoket.circleImg,
                                bRoket.x.toFloat(),
                                bRoket.y.toFloat(),
                                mPaint
                            )
                            score++
                        } else {
                            if (System.currentTimeMillis() - bRoket.time > 3000) {
                                iter.set(setUpBitmap())
                                life--
                                starArr.removeAt(0)
                                Log.d("life", life.toString())
                            } else canvas.drawBitmap(
                                bRoket.circleImg,
                                bRoket.x.toFloat(),
                                bRoket.y.toFloat(),
                                mPaint
                            )
                        }
                    }

                    for(i in 0 until starArr.size-1){
                        canvas.drawBitmap(star,(mViewWidth-100F*(i+1)),0F,null)
                    }

                    canvas.drawText(
                        score.toString(), 50F, 100F, whitePaint
                    )
                }

                // Clear the path data structure.
//                mPath.rewind()
                // Restore the previously saved (default) clip and matrix state.
//                canvas.restore()
                // Release the lock on the canvas and show the surface's
                // contents on the screen.
                mSurfaceHolder.unlockCanvasAndPost(canvas)
            }
        }


    }

    /**
     * Updates the game data.
     * Sets new coordinates for the flashlight cone.
     *
     * @param newX New x position of touch event.
     * @param newY New y position of touch event.
     */
//    private fun updateFrame(newX: Int, newY: Int) {
//        mFlashlightCone?.update(newX, newY)
//    }

    /**
     * Calculates a randomized location for the bitmap
     * and the winning bounding rectangle.
     */
    private fun setUpBitmap(): Roket {
        var w = Math.random() * (mViewWidth - mBitmap!!.width)
        var h = Math.random() * (mViewHeight - mBitmap!!.height)
        var roket = Roket(
            Math.floor(w).toInt(),
            Math.floor(h).toInt(),
            RectF(
                w.toFloat(), h.toFloat(),
                (w + mBitmap!!.width).toFloat(),
                (h + mBitmap!!.height).toFloat()
            ),
            circleArray[Random.nextInt(2)]
        )

        return roket
    }

    /**
     * Called by MainActivity.onPause() to stop the thread.
     */
    fun pause() {
        mRunning = false
        try {
            // Stop the thread == rejoin the main thread.
            mGameThread!!.join()
            timer.cancel()
        } catch (e: InterruptedException) {
        }
    }

    /**
     * Called by MainActivity.onResume() to start a thread.
     */
    fun resume() {
        mRunning = true
        mGameThread = Thread(this)
        mGameThread!!.start()
        timer.schedule(CustomTimer(), 5000, 10000)
    }

    fun reset() {
        score = 0
        life = 3
        roketArray.clear()
        roketArray.add(setUpBitmap())
        starArr.clear()
        starArr.add(star)
        starArr.add(star)
        starArr.add(star)
        starArr.add(star)
    }

    fun shake() {
        val animShake = AnimationUtils.loadAnimation(rootView.context, R.anim.shake)
        mySurface.startAnimation(animShake)
    }

//    fun time

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var x = event.x
        var y = event.y
        Log.d("Touch", "${event.action}")
        var iter = roketArray.listIterator()
        if (event.action == MotionEvent.ACTION_UP) {

            if (life<0){
                Log.d(
                    "Touch position",
                    "$x, $y, ${mViewWidth*2 / 6F}, ${mViewWidth*2 / 6F + 200}, ${mViewHeight*6 / 13F}, ${mViewHeight*6 / 13F+200}"
                )
                if ( x>mViewWidth*2 / 6F && x<(mViewWidth*2 / 6F + 200) && y>mViewHeight*6 / 13F && y<(mViewHeight*6 / 13F+200)){
                Log.d("click save", "$life")
                reset()
                invalidate()
                touchX = x
                touchX = y
            }
            }else {

                while (iter.hasNext()) {
                    var bRoket = iter.next()
                    Log.d(
                        "Touch position",
                        "$x, $y, ${bRoket.roketRect.left}, ${bRoket.roketRect.right}, ${bRoket.roketRect.top}, ${bRoket.roketRect.bottom}"
                    )
                    if (x > bRoket.roketRect.left && x < bRoket.roketRect.right && y > bRoket.roketRect.top && y < bRoket.roketRect.bottom) {
                        score++
                        Log.d("Score", score.toString())
                        iter.set(setUpBitmap())
                        touchX = x
                        touchX = y
                        invalidate()
                        break
                    }
                }
            }

        }

        return true
    }

    inner class CustomTimer : TimerTask() {
        override fun run() {
            roketArray.add(setUpBitmap())
        }

    }

    init {
        mSurfaceHolder = holder
        mPaint = Paint()
        mPaint.color = Color.DKGRAY
        mPath = Path()

    }
}