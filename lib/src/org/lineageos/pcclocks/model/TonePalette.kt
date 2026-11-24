// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

/** ARGB color arithmetic without Android classes. */
object ColorMath {

    /** Mixes [from] into [to] by [fraction] (0 keeps [from], 1 gives [to]), channel by channel. */
    fun blend(from: Int, to: Int, fraction: Float): Int =
        CHANNEL_SHIFTS.fold(0) { color, shift ->
            color or (mix(channel(from, shift), channel(to, shift), fraction) shl shift)
        }

    private val CHANNEL_SHIFTS = listOf(24, 16, 8, 0)

    private fun channel(color: Int, shift: Int) = (color ushr shift) and 0xFF

    private fun mix(from: Int, to: Int, fraction: Float) =
        Math.round(from + (to - from) * fraction.coerceIn(0f, 1f))
}

/** The tones a clock face paints with; the first one is the clock's main color. */
data class TonePalette(private val tones: List<Int>) {

    fun toneAt(index: Int): Int = tones[index % tones.size]

    /** Every tone moved towards [color] by [fraction], e.g. into the always-on display color. */
    fun towards(color: Int, fraction: Float) =
        TonePalette(tones.map { ColorMath.blend(it, color, fraction) })

    companion object {
        private const val WHITE = 0xFFFFFFFF.toInt()
        private const val BLACK = 0xFF000000.toInt()

        /** Four tones from one seed: the seed, then lighter (dark theme) or darker steps. */
        fun fromSeed(seed: Int, isDark: Boolean): TonePalette {
            val towards = CONTRAST.getValue(isDark)
            return TonePalette(STEPS.map { ColorMath.blend(seed, towards, it) })
        }

        private val STEPS = listOf(0f, 0.3f, 0.5f, 0.7f)

        /** Steps go lighter on a dark theme and darker on a light one. */
        private val CONTRAST = mapOf(true to WHITE, false to BLACK)
    }
}
