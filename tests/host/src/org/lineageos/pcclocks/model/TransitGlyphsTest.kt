// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

import org.junit.Assert.assertTrue
import org.junit.Test

class TransitGlyphsTest {

    @Test
    fun everyDigitHasStrokes() {
        ('0'..'9').forEach { assertTrue("$it", TransitGlyphs.of(it).isNotEmpty()) }
    }

    @Test
    fun everyStrokeStaysInsideTheGlyphBox() {
        ('0'..'9').forEach { digit ->
            TransitGlyphs.of(digit).forEach { stroke ->
                val box = stroke.bounds()
                assertTrue("$digit $stroke", box.left >= -EPSILON && box.top >= -EPSILON)
                assertTrue(
                    "$digit $stroke",
                    box.right <= TransitGlyphs.WIDTH + EPSILON &&
                        box.bottom <= TransitGlyphs.HEIGHT + EPSILON,
                )
            }
        }
    }

    @Test
    fun unknownCharacterHasNoStrokes() {
        assertTrue(TransitGlyphs.of(':').isEmpty())
    }

    private companion object {
        const val EPSILON = 0.01f
    }
}
