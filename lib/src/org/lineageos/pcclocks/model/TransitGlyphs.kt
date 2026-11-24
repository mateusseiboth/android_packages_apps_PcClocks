// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/** A box in glyph units. */
data class GlyphBox(val left: Float, val top: Float, val right: Float, val bottom: Float)

/** One stroke of a transit glyph, in glyph units (y grows downwards). */
sealed interface TransitStroke {
    fun bounds(): GlyphBox

    data class Line(val x1: Float, val y1: Float, val x2: Float, val y2: Float) : TransitStroke {
        override fun bounds() = GlyphBox(min(x1, x2), min(y1, y2), max(x1, x2), max(y1, y2))
    }

    /** An arc around a center; angles in degrees, 0 at three o'clock, clockwise. */
    data class Arc(
        val cx: Float,
        val cy: Float,
        val radius: Float,
        val startDegrees: Float,
        val sweepDegrees: Float,
    ) : TransitStroke {
        override fun bounds(): GlyphBox {
            val points = (0..SAMPLES).map { pointAt(startDegrees + sweepDegrees * it / SAMPLES) }
            return GlyphBox(
                points.minOf { it.first },
                points.minOf { it.second },
                points.maxOf { it.first },
                points.maxOf { it.second },
            )
        }

        private fun pointAt(degrees: Float): Pair<Float, Float> {
            val radians = Math.toRadians(degrees.toDouble())
            return (cx + radius * cos(radians).toFloat()) to (cy + radius * sin(radians).toFloat())
        }

        private companion object {
            const val SAMPLES = 72
        }
    }
}

/**
 * Digits drawn as rounded rails, like the lines of a transit map. Each digit is a single path of
 * strokes inside a [WIDTH] by [HEIGHT] box; the face draws it as nested lines of several tones.
 */
object TransitGlyphs {
    const val WIDTH = 4f
    const val HEIGHT = 6f

    private val GLYPHS: Map<Char, List<TransitStroke>> =
        mapOf(
            '0' to
                listOf(
                    arc(2f, 2f, 2f, 180f, 180f),
                    line(4f, 2f, 4f, 4f),
                    arc(2f, 4f, 2f, 0f, 180f),
                    line(0f, 4f, 0f, 2f),
                ),
            '1' to listOf(line(0.8f, 1.2f, 2.4f, 0f), line(2.4f, 0f, 2.4f, 6f)),
            '2' to listOf(arc(2f, 2f, 2f, 180f, 180f), line(4f, 2f, 0f, 6f), line(0f, 6f, 4f, 6f)),
            '3' to listOf(arc(2f, 1.5f, 1.5f, 180f, 270f), arc(2f, 4.5f, 1.5f, 270f, 270f)),
            '4' to listOf(line(3f, 6f, 3f, 0f), line(3f, 0f, 0f, 4f), line(0f, 4f, 4f, 4f)),
            '5' to
                listOf(
                    line(4f, 0f, 0.6f, 0f),
                    line(0.6f, 0f, 0.6f, 2.6f),
                    arc(2f, 4f, 2f, 225f, 270f),
                ),
            '6' to listOf(line(3.2f, 0f, 0.12f, 3.32f), arc(2f, 4f, 2f, 0f, 360f)),
            '7' to listOf(line(0f, 0f, 4f, 0f), line(4f, 0f, 1.5f, 6f)),
            '8' to listOf(arc(2f, 1.5f, 1.5f, 0f, 360f), arc(2f, 4.3f, 1.7f, 0f, 360f)),
            '9' to listOf(arc(2f, 2f, 2f, 0f, 360f), line(3.88f, 2.68f, 0.8f, 6f)),
        )

    fun of(digit: Char): List<TransitStroke> = GLYPHS[digit].orEmpty()

    private fun line(x1: Float, y1: Float, x2: Float, y2: Float) =
        TransitStroke.Line(x1, y1, x2, y2)

    private fun arc(cx: Float, cy: Float, radius: Float, start: Float, sweep: Float) =
        TransitStroke.Arc(cx, cy, radius, start, sweep)
}
