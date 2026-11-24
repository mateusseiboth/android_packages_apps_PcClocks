// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

import java.time.LocalDate
import java.util.Locale
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class WeatherTextTest {

    @Test
    fun temperature_hasTheDegreeSign() {
        assertEquals("58°", WeatherText.temperature(58))
        assertEquals("-3°", WeatherText.temperature(-3))
    }

    @Test
    fun dateLine_isShortMonthAndDayInCapitals() {
        assertEquals("AUG 17", WeatherText.dateLine(LocalDate.of(2026, 8, 17), Locale.US))
    }

    @Test
    fun dateLine_dropsAbbreviationDots() {
        val line = WeatherText.dateLine(LocalDate.of(2026, 8, 17), Locale.forLanguageTag("pt-BR"))

        assertFalse(line, line.contains('.'))
        assertEquals(line, line.uppercase(Locale.forLanguageTag("pt-BR")))
    }

    @Test
    fun iconKind_coversEveryWeatherState() {
        (1..31).forEach { assertEquals(true, WeatherIconKind.of(it) != null) }
    }

    @Test
    fun iconKind_examples() {
        assertEquals(WeatherIconKind.SUN, WeatherIconKind.of(1))
        assertEquals(WeatherIconKind.MOON, WeatherIconKind.of(2))
        assertEquals(WeatherIconKind.CLOUD, WeatherIconKind.of(9))
        assertEquals(WeatherIconKind.FOG, WeatherIconKind.of(10))
        assertEquals(WeatherIconKind.RAIN, WeatherIconKind.of(12))
        assertEquals(WeatherIconKind.STORM, WeatherIconKind.of(18))
        assertEquals(WeatherIconKind.SNOW, WeatherIconKind.of(22))
        assertEquals(WeatherIconKind.WIND, WeatherIconKind.of(30))
    }

    @Test
    fun iconKind_unknownState_isNull() {
        assertNull(WeatherIconKind.of(0))
        assertNull(WeatherIconKind.of(99))
    }
}
