// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.face

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.Typeface
import android.util.SizeF
import org.lineageos.pcclocks.model.ToneScheme

/** How a stacked face draws its digits. */
data class DigitStyle(
    val typeface: Typeface,
    val fontVariation: String? = null,
    val tones: ToneScheme = ToneScheme.SINGLE,
    /** Fraction of a digit each neighbour covers (horizontally and between the two rows). */
    val overlap: Float = 0f,
    /** Outline width as a fraction of the font size, 0 for none. */
    val outline: Float = 0f,
    /** Stencil bridges cut across every digit, as a fraction of the font size, 0 for none. */
    val stencilGap: Float = 0f,
)

/** Hours above minutes, each digit in its own cell, as the large digital clocks lay them out. */
class StackedDigitsPainter(private val digitStyle: DigitStyle) : ClockPainter {
    private val fill =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            typeface = digitStyle.typeface
            fontVariationSettings = digitStyle.fontVariation
            textAlign = Paint.Align.CENTER
        }
    private val outline = Paint(fill).apply { style = Paint.Style.STROKE }
    private val stencil = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }
    private val digitBounds = Rect()

    override fun measure(fontSizePx: Float): SizeF {
        val cell = cellOf(fontSizePx)
        return SizeF(cell.width + cell.advanceX, cell.height + cell.advanceY)
    }

    override fun draw(canvas: Canvas, state: FaceState) {
        val cell = cellOf(state.fontSizePx)
        val layer = canvas.saveLayer(null, null)
        state.time.digits.forEachIndexed { index, digit ->
            val left = (index % 2) * cell.advanceX
            val top = (index / 2) * cell.advanceY
            val color = state.palette.toneAt(digitStyle.tones.toneOf(index))
            drawDigit(canvas, digit, left + cell.width / 2, top + cell.height, color)
        }
        cutStencil(canvas, cell, state.fontSizePx)
        canvas.restoreToCount(layer)
    }

    private fun drawDigit(
        canvas: Canvas,
        digit: Char,
        centerX: Float,
        baseline: Float,
        color: Int,
    ) {
        fill.color = color
        canvas.drawText(digit.toString(), centerX, baseline, fill)
        if (digitStyle.outline <= 0f) return
        outline.color = color
        canvas.drawText(digit.toString(), centerX, baseline, outline)
    }

    /** Clears a thin band across each row, the bridges of a stencil. */
    private fun cutStencil(canvas: Canvas, cell: Cell, fontSizePx: Float) {
        if (digitStyle.stencilGap <= 0f) return
        val gap = fontSizePx * digitStyle.stencilGap
        listOf(0f, cell.advanceY).forEach { top ->
            val middle = top + cell.height * 0.5f
            canvas.drawRect(
                0f,
                middle - gap / 2,
                cell.width + cell.advanceX,
                middle + gap / 2,
                stencil,
            )
        }
    }

    private fun cellOf(fontSizePx: Float): Cell {
        fill.textSize = fontSizePx
        outline.textSize = fontSizePx
        outline.strokeWidth = fontSizePx * digitStyle.outline
        fill.getTextBounds(DIGITS, 0, DIGITS.length, digitBounds)
        val width = DIGITS.maxOf { fill.measureText(it.toString()) }
        val height = digitBounds.height().toFloat()
        return Cell(
            width,
            height,
            width * (1 - digitStyle.overlap),
            height * (1 - digitStyle.overlap),
        )
    }

    private data class Cell(
        val width: Float,
        val height: Float,
        val advanceX: Float,
        val advanceY: Float,
    )

    private companion object {
        const val DIGITS = "0123456789"
    }
}
