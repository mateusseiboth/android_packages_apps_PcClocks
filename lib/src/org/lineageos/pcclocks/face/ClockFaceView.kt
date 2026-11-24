// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.face

import android.content.Context
import android.graphics.Canvas
import android.view.View
import kotlin.math.ceil

/** Measures and draws a face through its [painter]; [state] changes redraw it. */
class ClockFaceView(context: Context, private val painter: ClockPainter, initial: FaceState) :
    View(context) {

    var state: FaceState = initial
        set(value) {
            val resized = field.fontSizePx != value.fontSizePx
            field = value
            invalidate()
            if (resized) requestLayout()
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = painter.measure(state.fontSizePx)
        setMeasuredDimension(ceil(size.width).toInt(), ceil(size.height).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        painter.draw(canvas, state)
    }
}
