package com.example.weedy.ui

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.mxn.soul.flowingdrawer_core.FlowingDrawer

/**
 * Custom view to handle dragging interactions and open a drawer when dragged beyond a threshold.
 */
class DragView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var flowingDrawer: FlowingDrawer? = null // Drawer to be opened
    private var initialTouchX: Float = 0f // Initial X coordinate of touch
    private var initialTranslationX: Float = 0f // Initial translation X position of the view

    /**
     * Sets the FlowingDrawer instance to be controlled by this view.
     */
    fun setFlowingDrawer(flowingDrawer: FlowingDrawer) {
        this.flowingDrawer = flowingDrawer
    }

    /**
     * Handles touch events for dragging the view and interacting with the drawer.
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // Record the initial touch position and view translation
                initialTouchX = event.rawX
                initialTranslationX = translationX
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                // Calculate the distance dragged and update the view's translation
                val dx = event.rawX - initialTouchX
                translationX = initialTranslationX + dx

                // If dragged past halfway of the view's width, open the drawer
                if (translationX > width * 0.5f) {
                    flowingDrawer?.openMenu()
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                // Animate the view back to its original position
                ObjectAnimator.ofFloat(this, "translationX", 0f).apply {
                    duration = 300 // Duration of the animation
                    start()
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
