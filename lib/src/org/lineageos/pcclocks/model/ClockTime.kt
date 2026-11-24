// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

/** The time a clock face shows, already split the way the faces draw it. */
data class ClockTime(val hourOfDay: Int, val minute: Int, val second: Int, val is24Hour: Boolean) {

    /** The hour on the face's dial: 0 to 23, or 1 to 12 on a twelve hour clock. */
    val dialHour: Int
        get() = if24Hour(hourOfDay) ?: ((hourOfDay + 11) % 12 + 1)

    val hourText: String
        get() = twoDigits(dialHour)

    val minuteText: String
        get() = twoDigits(minute)

    /** Hour tens, hour units, minute tens, minute units. */
    val digits: List<Char>
        get() = (hourText + minuteText).toList()

    val singleLine: String
        get() = "$hourText:$minuteText"

    private fun if24Hour(hour: Int): Int? = hour.takeIf { is24Hour }

    private fun twoDigits(value: Int) = value.toString().padStart(2, '0')
}
