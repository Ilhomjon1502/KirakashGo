package uz.ilhomjon.kirakashgo.presentation.screens.getsure

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.Toast
import kotlin.math.abs

class MyGestureListener(val context: Context, private val view: View, val targetX: Float) :
    GestureDetector.SimpleOnGestureListener() {


    private var initialX = 0f // Initial X position of the ImageView
    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val distanceX = e2.x - e1!!.x
        if (abs(distanceX) > 50 && abs(velocityX) > 100) {
            if (distanceX > 0) {
                // Right swipe detected
                // Handle right swipe action
                moveImageViewToRight(view, targetX)
                Toast.makeText(context, "Right Swipe", Toast.LENGTH_SHORT).show()
                Log.d("gesture_detector", "onFling: Right Swipe")
            } else {
                // Left swipe detected
                // Handle left swipe action
                moveImageViewToRight(view, targetX)
                Toast.makeText(context, "Left Swipe", Toast.LENGTH_SHORT).show()
                Log.d("gesture_detector", "onFling: Left Swipe")
            }
            return true
        }
        return false
    }


    private fun moveImageViewToRight(view: View, targetX: Float) {
        val animation = TranslateAnimation(0f, targetX - view.x, 0f, 0f)
        animation.duration = 1000 // Animation duration in milliseconds
        animation.fillAfter = true // Keeps the final position of the view after the animation

        view.startAnimation(animation)
    }
}