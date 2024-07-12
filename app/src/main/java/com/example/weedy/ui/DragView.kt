package com.example.weedy.ui

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.mxn.soul.flowingdrawer_core.FlowingDrawer

class DragView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var flowingDrawer: FlowingDrawer? = null
    private var initialX: Float = 0f
    private var initialY: Float = 0f

    fun setFlowingDrawer(flowingDrawer: FlowingDrawer) {
        this.flowingDrawer = flowingDrawer
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = x
                initialY = y
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - initialX
                val dy = event.y - initialY
                x = initialX + dx
                y = initialY + dy

                if (dx > 300f) {
                    flowingDrawer?.openMenu()
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                x = 0f
                y = initialY
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}