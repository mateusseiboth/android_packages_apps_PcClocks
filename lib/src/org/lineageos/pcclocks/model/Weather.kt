// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/** The icons the weather clock draws, one per family of weather states. */
enum class WeatherIconKind {
    SUN,
    MOON,
    CLOUD,
    FOG,
    RAIN,
    STORM,
    SNOW,
    WIND;

    companion object {
        // Ids of SystemUI's WeatherData.WeatherStateIcon.
        private val BY_STATE: Map<Int, WeatherIconKind> =
            mapOf(1 to SUN, 2 to MOON, 3 to SUN, 4 to MOON, 5 to SUN, 6 to MOON) +
                (7..9).associateWith { CLOUD } +
                mapOf(10 to FOG) +
                (11..15).associateWith { RAIN } +
                (16..18).associateWith { STORM } +
                (19..27).associateWith { SNOW } +
                mapOf(28 to WIND, 29 to STORM, 30 to WIND, 31 to SNOW)

        fun of(stateId: Int): WeatherIconKind? = BY_STATE[stateId]
    }
}

/** Texts of the weather clock. */
object WeatherText {

    fun temperature(degrees: Int): String = "$degrees°"

    /** Short month and day in capitals, without abbreviation dots (e.g. "AUG 17"). */
    fun dateLine(date: LocalDate, locale: Locale): String =
        DateTimeFormatter.ofPattern("MMM d", locale).format(date).replace(".", "").uppercase(locale)
}
