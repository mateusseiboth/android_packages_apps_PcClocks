// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

import org.junit.Assert.assertEquals
import org.junit.Test

class TonePaletteTest {

    @Test
    fun blend_atTheEnds_isEachColor() {
        assertEquals(
            0xFF102030.toInt(),
            ColorMath.blend(0xFF102030.toInt(), 0xFFFFFFFF.toInt(), 0f),
        )
        assertEquals(
            0xFFFFFFFF.toInt(),
            ColorMath.blend(0xFF102030.toInt(), 0xFFFFFFFF.toInt(), 1f),
        )
    }

    @Test
    fun blend_halfway_mixesEveryChannel() {
        assertEquals(0x80808080.toInt(), ColorMath.blend(0x00000000, 0xFFFFFFFF.toInt(), 0.5f))
    }

    @Test
    fun fromSeed_keepsTheSeedFirst() {
        val seed = 0xFF3366CC.toInt()

        assertEquals(seed, TonePalette.fromSeed(seed, isDark = true).toneAt(0))
    }

    @Test
    fun fromSeed_hasFourDistinctTones() {
        val palette = TonePalette.fromSeed(0xFF3366CC.toInt(), isDark = false)

        assertEquals(4, (0..3).map(palette::toneAt).toSet().size)
    }

    @Test
    fun toneAt_wrapsAround() {
        val palette = TonePalette(listOf(1, 2, 3, 4))

        assertEquals(1, palette.toneAt(4))
        assertEquals(2, palette.toneAt(5))
    }

    @Test
    fun dozing_turnsEveryToneIntoTheAodColor() {
        val palette = TonePalette(listOf(1, 2, 3, 4)).towards(0xFFFFFFFF.toInt(), 1f)

        assertEquals(List(4) { 0xFFFFFFFF.toInt() }, (0..3).map(palette::toneAt))
    }
}
