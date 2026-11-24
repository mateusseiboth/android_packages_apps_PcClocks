// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

/** Angles of an analog clock's hands, clockwise from twelve o'clock. */
data class AnalogHands(val hourDegrees: Float, val minuteDegrees: Float, val secondDegrees: Float) {
    companion object {
        /** The numerals a big number dial shows, with their angle. */
        val DIAL_NUMERALS = listOf(12 to 0f, 3 to 90f, 6 to 180f, 9 to 270f)

        fun of(time: ClockTime): AnalogHands {
            val seconds = time.second.toFloat()
            val minutes = time.minute + seconds / 60f
            val hours = time.hourOfDay % 12 + minutes / 60f
            return AnalogHands(
                hourDegrees = hours * 30f,
                minuteDegrees = minutes * 6f,
                secondDegrees = seconds * 6f,
            )
        }
    }
}
