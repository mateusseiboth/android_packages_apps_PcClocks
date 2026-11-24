// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

import org.junit.Assert.assertEquals
import org.junit.Test

class ToneSchemeTest {

    @Test
    fun single_paintsEveryDigitWithThePrimaryTone() {
        assertEquals(List(4) { 0 }, (0..3).map(ToneScheme.SINGLE::toneOf))
    }

    @Test
    fun diagonal_pairsOppositeCorners() {
        // Hour tens and minute units share a tone, as do hour units and minute tens.
        assertEquals(listOf(0, 1, 1, 0), (0..3).map(ToneScheme.DIAGONAL::toneOf))
    }

    @Test
    fun fourTones_givesEveryDigitItsOwnTone() {
        assertEquals(listOf(0, 1, 2, 3), (0..3).map(ToneScheme.FOUR_TONES::toneOf))
    }
}
