// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ClockStylesTest {

    // SystemUI's ClockRegistry (KNOWN_PLUGINS) and ThemePicker's plugin allowlist name these
    // packages and clock ids; a clock is only picked up lazily when both match.
    private val systemUiContract =
        mapOf(
            "com.android.systemui.clocks.bignum" to "ANALOG_CLOCK_BIGNUM",
            "com.android.systemui.clocks.calligraphy" to "DIGITAL_CLOCK_CALLIGRAPHY",
            "com.android.systemui.clocks.growth" to "DIGITAL_CLOCK_GROWTH",
            "com.android.systemui.clocks.inflate" to "DIGITAL_CLOCK_INFLATE",
            "com.android.systemui.clocks.metro" to "DIGITAL_CLOCK_METRO",
            "com.android.systemui.clocks.numoverlap" to "DIGITAL_CLOCK_NUMBEROVERLAP",
            "com.android.systemui.clocks.weather" to "DIGITAL_CLOCK_WEATHER",
        )

    @Test
    fun everyStyleMatchesSystemUiPackageAndClockId() {
        assertEquals(systemUiContract, ClockStyles.all.associate { it.packageName to it.clockId })
    }

    @Test
    fun styleIsFoundByClockId() {
        ClockStyles.all.forEach { assertEquals(it, ClockStyles.byClockId(it.clockId)) }
    }

    @Test
    fun unknownClockIdHasNoStyle() {
        assertEquals(null, ClockStyles.byClockId("DEFAULT"))
    }

    @Test
    fun onlyBigNumIsAnalog() {
        assertEquals(listOf(ClockStyles.BIG_NUM), ClockStyles.all.filter { it.isAnalog })
    }

    @Test
    fun onlyWeatherShowsTheWeather() {
        assertTrue(ClockStyles.WEATHER.showsWeather)
        assertEquals(listOf(ClockStyles.WEATHER), ClockStyles.all.filter { it.showsWeather })
    }
}
