// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.face

import android.graphics.Canvas
import android.graphics.Paint
import android.util.SizeF

/** "10:38" on one line in the style's font: the small clock above the notifications. */
class SingleLinePainter(style: DigitStyle) : ClockPainter {
    private val paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = style.typeface
            fontVariationSettings = style.fontVariation
        }

    override fun measure(fontSizePx: Float): SizeF {
        paint.textSize = fontSizePx
        val metrics = paint.fontMetrics
        return SizeF(paint.measureText(SAMPLE), metrics.descent - metrics.ascent)
    }

    override fun draw(canvas: Canvas, state: FaceState) {
        paint.textSize = state.fontSizePx
        paint.color = state.palette.toneAt(0)
        canvas.drawText(state.time.singleLine, 0f, -paint.fontMetrics.ascent, paint)
    }

    private companion object {
        // The widest time, so the small clock does not change width every minute.
        const val SAMPLE = "00:00"
    }
}
