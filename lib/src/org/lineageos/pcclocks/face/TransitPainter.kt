// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.face

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.SizeF
import org.lineageos.pcclocks.model.TransitGlyphs
import org.lineageos.pcclocks.model.TransitStroke

/**
 * Hours above minutes, each digit a rail drawn as nested lines of three tones, like the lines of a
 * transit map running side by side.
 */
class TransitPainter : ClockPainter {
    private val rail =
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
        }

    override fun measure(fontSizePx: Float): SizeF {
        val unit = unitOf(fontSizePx)
        return SizeF(
            (TransitGlyphs.WIDTH * 2 + GAP_X) * unit + padding(unit) * 2,
            (TransitGlyphs.HEIGHT * 2 + GAP_Y) * unit + padding(unit) * 2,
        )
    }

    override fun draw(canvas: Canvas, state: FaceState) {
        val unit = unitOf(state.fontSizePx)
        state.time.digits.forEachIndexed { index, digit ->
            val left = padding(unit) + (index % 2) * (TransitGlyphs.WIDTH + GAP_X) * unit
            val top = padding(unit) + (index / 2) * (TransitGlyphs.HEIGHT + GAP_Y) * unit
            val path = pathOf(digit, left, top, unit)
            LANES.forEach { lane -> drawLane(canvas, path, lane, unit, state) }
        }
    }

    private fun drawLane(canvas: Canvas, path: Path, lane: Lane, unit: Float, state: FaceState) {
        rail.strokeWidth = unit * lane.width
        rail.color = state.palette.toneAt(lane.tone)
        canvas.drawPath(path, rail)
    }

    private fun pathOf(digit: Char, left: Float, top: Float, unit: Float): Path =
        Path().apply { TransitGlyphs.of(digit).forEach { stroke -> add(stroke, left, top, unit) } }

    private fun Path.add(stroke: TransitStroke, left: Float, top: Float, unit: Float) {
        when (stroke) {
            is TransitStroke.Line -> {
                moveTo(left + stroke.x1 * unit, top + stroke.y1 * unit)
                lineTo(left + stroke.x2 * unit, top + stroke.y2 * unit)
            }
            is TransitStroke.Arc -> {
                val oval =
                    RectF(
                        left + (stroke.cx - stroke.radius) * unit,
                        top + (stroke.cy - stroke.radius) * unit,
                        left + (stroke.cx + stroke.radius) * unit,
                        top + (stroke.cy + stroke.radius) * unit,
                    )
                arcTo(oval, stroke.startDegrees, stroke.sweepDegrees, true)
            }
        }
    }

    /** Half the widest lane, so round caps are not cut at the edges. */
    private fun padding(unit: Float) = unit * LANES.maxOf { it.width } / 2

    private fun unitOf(fontSizePx: Float) = fontSizePx / TransitGlyphs.HEIGHT

    private data class Lane(val width: Float, val tone: Int)

    private companion object {
        const val GAP_X = 0.9f
        const val GAP_Y = 1.1f

        // Widest first: each narrower lane runs inside the previous one.
        val LANES = listOf(Lane(0.95f, 1), Lane(0.6f, 2), Lane(0.22f, 0))
    }
}
