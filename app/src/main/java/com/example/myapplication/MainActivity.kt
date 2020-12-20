package com.example.myapplication
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


/**
 * This app demonstrates how to use a SurfaceView to render an image from a
 * separate thread.
 * In addition, it shows how you can use clipping for animation,
 * and implements a basic game loop. This is not intended as a production
 * quality app. Rather, it demonstrates the basics
 * of these techniques, so you can dive deeper on your own.
 *
 * Note that SurfaceView offers trade-offs you must consider:
 *
 * * Offers a lover level drawing surface with more control without the need
 * for learning OpenGL or the NDK.
 * * You can draw on it same as a canvas.
 * * Draw from a separate thread, not the UI thread.
 * * Does not have built-in hardware acceleration, e.g. for transformations.
 * Monitor performance carefully, especially if you are doing animations.
 *
 * Game play:
 *
 * The user is presented with a dark surface with a white circle.
 * This represents a wall shone at with a flashlight cone. Touching the
 * surface hides an android image. The light cone then follows continuous
 * motion. If the image of an android is discovered, the screen lights up and
 * the word "WIN!" appears. To restart lift finger and touch screen again.
 *
 * The following limitations are imposed to keep the code focused.
 *
 * * No startup screen or any other functionality other than game play.
 * * No saving of state, game, or user data.
 * * No acrobatics to handle edge cases.
 */
class MainActivity : AppCompatActivity() {
    private var mGameView: GameView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Lock orientation into landscape.
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // Create a GameView and bind it to this activity.
        // You don't need a ViewGroup to fill the screen, because the system
        // has a FrameLayout to which this will be added.
        mGameView = GameView(this)
        // Android 4.1 and higher simple way to request fullscreen.
        mGameView!!.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(mGameView)

    }

    /**
     * Pauses game when activity is paused.
     */
    override fun onPause() {
        super.onPause()
        mGameView!!.pause()
    }

    /**
     * Resumes game when activity is resumed.
     */
    override fun onResume() {
        super.onResume()
        mGameView!!.resume()
    }
}