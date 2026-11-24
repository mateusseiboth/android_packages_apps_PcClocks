// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.face

import android.graphics.Canvas
import android.graphics.Paint
import android.util.SizeF
import org.lineageos.pcclocks.ClockTypefaces
import org.lineageos.pcclocks.model.WeatherText

/**
 * A narrow column: the time across the top, the date running up the left edge, the weather icon in
 * the middle and the temperature running up the right edge.
 */
class WeatherPainter : ClockPainter {
    private val text =
        Paint(Paint.ANTI_ALIAS_FLAG).apply { typeface = ClockTypefaces.googleSansFlex }
    private val icon = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun measure(fontSizePx: Float) = SizeF(fontSizePx * WIDTH, fontSizePx * HEIGHT)

    override fun draw(canvas: Canvas, state: FaceState) {
        val size = state.fontSizePx
        text.color = state.palette.toneAt(0)
        text.textSize = size * TIME_SIZE
        canvas.drawText(
            "${state.time.hourText} ${state.time.minuteText}",
            size * 0.42f,
            size * 0.42f,
            text,
        )
        text.textSize = size * SIDE_TEXT_SIZE
        drawUpwards(
            canvas,
            WeatherText.dateLine(state.date, state.locale),
            size * 0.36f,
            size * HEIGHT,
        )
        state.weather?.let { weather ->
            icon.color = state.palette.toneAt(2)
            WeatherIcons.draw(canvas, weather.icon, size * 0.95f, size * 0.95f, size * 0.42f, icon)
            drawUpwards(canvas, weather.temperature, size * 1.5f, size * HEIGHT)
        }
    }

    /** Text read from the bottom up, its baseline on the vertical line x, starting at bottom. */
    private fun drawUpwards(canvas: Canvas, value: String, x: Float, bottom: Float) {
        canvas.save()
        canvas.rotate(-90f, x, bottom)
        canvas.drawText(value, x, bottom, text)
        canvas.restore()
    }

    private companion object {
        const val WIDTH = 1.6f
        const val HEIGHT = 2f
        const val TIME_SIZE = 0.4f
        const val SIDE_TEXT_SIZE = 0.34f
    }
}
