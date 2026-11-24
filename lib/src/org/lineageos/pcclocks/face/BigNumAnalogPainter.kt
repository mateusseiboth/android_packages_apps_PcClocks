// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.face

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.SizeF
import org.lineageos.pcclocks.ClockTypefaces
import org.lineageos.pcclocks.model.AnalogHands

/** An analog dial with large heavy numerals at twelve, three, six and nine. */
class BigNumAnalogPainter(private val diameterPerFontSize: Float) : ClockPainter {
    private val numerals =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = ClockTypefaces.robotoFlex
            fontVariationSettings = ClockTypefaces.robotoFlexAxes(weight = 900, width = 100)
            textAlign = Paint.Align.CENTER
        }
    private val hand =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
        }
    private val numeralBounds = Rect()

    override fun measure(fontSizePx: Float): SizeF {
        val diameter = fontSizePx * diameterPerFontSize
        return SizeF(diameter, diameter)
    }

    override fun draw(canvas: Canvas, state: FaceState) {
        val diameter = state.fontSizePx * diameterPerFontSize
        val center = diameter / 2
        drawNumerals(canvas, diameter, state.palette.toneAt(2))
        val hands = AnalogHands.of(state.time)
        drawHand(canvas, center, hands.hourDegrees, HOUR, state.palette.toneAt(1), diameter)
        drawHand(canvas, center, hands.minuteDegrees, MINUTE, state.palette.toneAt(0), diameter)
    }

    private fun drawNumerals(canvas: Canvas, diameter: Float, color: Int) {
        numerals.textSize = diameter * NUMERAL_SIZE
        numerals.color = color
        val center = diameter / 2
        val reach = center - diameter * NUMERAL_SIZE * 0.4f
        AnalogHands.DIAL_NUMERALS.forEach { (numeral, degrees) ->
            val text = numeral.toString()
            numerals.getTextBounds(text, 0, text.length, numeralBounds)
            val radians = Math.toRadians(degrees.toDouble())
            val x = center + reach * Math.sin(radians).toFloat()
            val y = center - reach * Math.cos(radians).toFloat() + numeralBounds.height() / 2f
            canvas.drawText(text, x, y, numerals)
        }
    }

    private fun drawHand(
        canvas: Canvas,
        center: Float,
        degrees: Float,
        spec: HandSpec,
        color: Int,
        diameter: Float,
    ) {
        hand.color = color
        hand.strokeWidth = diameter * spec.width
        canvas.save()
        canvas.rotate(degrees, center, center)
        canvas.drawLine(center, center, center, center - diameter * spec.length, hand)
        canvas.restore()
    }

    private data class HandSpec(val width: Float, val length: Float)

    private companion object {
        const val NUMERAL_SIZE = 0.34f
        val HOUR = HandSpec(width = 0.07f, length = 0.24f)
        val MINUTE = HandSpec(width = 0.022f, length = 0.44f)
    }
}
