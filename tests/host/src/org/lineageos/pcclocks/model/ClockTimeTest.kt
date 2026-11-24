// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ClockTimeTest {

    @Test
    fun fullDay_keepsTheHourWithLeadingZero() {
        val time = ClockTime(hourOfDay = 9, minute = 5, second = 0, is24Hour = true)

        assertEquals("09", time.hourText)
        assertEquals("05", time.minuteText)
    }

    @Test
    fun fullDay_afternoon() {
        assertEquals("21", ClockTime(21, 30, 0, is24Hour = true).hourText)
    }

    @Test
    fun halfDay_afternoonHourIsOnTheTwelveHourDial() {
        assertEquals("09", ClockTime(21, 30, 0, is24Hour = false).hourText)
    }

    @Test
    fun halfDay_midnightAndNoonAreTwelve() {
        assertEquals("12", ClockTime(0, 0, 0, is24Hour = false).hourText)
        assertEquals("12", ClockTime(12, 0, 0, is24Hour = false).hourText)
    }

    @Test
    fun digits_areHourThenMinute() {
        assertEquals(listOf('1', '0', '3', '8'), ClockTime(10, 38, 0, is24Hour = true).digits)
    }

    @Test
    fun singleLine_joinsHourAndMinute() {
        assertEquals("10:38", ClockTime(10, 38, 0, is24Hour = true).singleLine)
    }
}
