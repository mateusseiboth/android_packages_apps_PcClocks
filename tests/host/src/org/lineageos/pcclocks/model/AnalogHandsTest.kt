// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

import org.junit.Assert.assertEquals
import org.junit.Test

class AnalogHandsTest {

    @Test
    fun threeOClock() {
        val hands = AnalogHands.of(ClockTime(15, 0, 0, is24Hour = true))

        assertEquals(90f, hands.hourDegrees, 1e-4f)
        assertEquals(0f, hands.minuteDegrees, 1e-4f)
        assertEquals(0f, hands.secondDegrees, 1e-4f)
    }

    @Test
    fun hourHandMovesWithTheMinutes() {
        assertEquals(15f, AnalogHands.of(ClockTime(0, 30, 0, is24Hour = true)).hourDegrees, 1e-4f)
    }

    @Test
    fun minuteHandMovesWithTheSeconds() {
        val hands = AnalogHands.of(ClockTime(0, 10, 30, is24Hour = true))

        assertEquals(63f, hands.minuteDegrees, 1e-4f)
        assertEquals(180f, hands.secondDegrees, 1e-4f)
    }

    @Test
    fun dialNumeralsSitAtTheQuarters() {
        assertEquals(listOf(12 to 0f, 3 to 90f, 6 to 180f, 9 to 270f), AnalogHands.DIAL_NUMERALS)
    }
}
