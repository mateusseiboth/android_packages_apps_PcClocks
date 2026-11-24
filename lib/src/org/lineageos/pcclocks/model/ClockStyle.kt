// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

/**
 * A lock screen clock style. [packageName] and [clockId] are the ones SystemUI's ClockRegistry and
 * ThemePicker already know, so the style's plugin is found and loaded on demand.
 */
data class ClockStyle(
    val packageName: String,
    val clockId: String,
    val isAnalog: Boolean = false,
    val showsWeather: Boolean = false,
)

object ClockStyles {
    private const val PACKAGE_PREFIX = "com.android.systemui.clocks."

    val BIG_NUM = ClockStyle(PACKAGE_PREFIX + "bignum", "ANALOG_CLOCK_BIGNUM", isAnalog = true)
    val CALLIGRAPHY = ClockStyle(PACKAGE_PREFIX + "calligraphy", "DIGITAL_CLOCK_CALLIGRAPHY")
    val GROWTH = ClockStyle(PACKAGE_PREFIX + "growth", "DIGITAL_CLOCK_GROWTH")
    val INFLATE = ClockStyle(PACKAGE_PREFIX + "inflate", "DIGITAL_CLOCK_INFLATE")
    val METRO = ClockStyle(PACKAGE_PREFIX + "metro", "DIGITAL_CLOCK_METRO")
    val NUM_OVERLAP = ClockStyle(PACKAGE_PREFIX + "numoverlap", "DIGITAL_CLOCK_NUMBEROVERLAP")
    val WEATHER =
        ClockStyle(PACKAGE_PREFIX + "weather", "DIGITAL_CLOCK_WEATHER", showsWeather = true)

    val all = listOf(BIG_NUM, CALLIGRAPHY, GROWTH, INFLATE, METRO, NUM_OVERLAP, WEATHER)

    private val byId = all.associateBy { it.clockId }

    fun byClockId(clockId: String): ClockStyle? = byId[clockId]
}
