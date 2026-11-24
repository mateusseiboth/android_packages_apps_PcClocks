// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.model

/** Which tone of the palette paints each of the four digits (hour tens first). */
class ToneScheme private constructor(private val tones: List<Int>) {

    fun toneOf(digitIndex: Int): Int = tones[digitIndex % tones.size]

    companion object {
        val SINGLE = ToneScheme(listOf(0, 0, 0, 0))
        val DIAGONAL = ToneScheme(listOf(0, 1, 1, 0))
        val FOUR_TONES = ToneScheme(listOf(0, 1, 2, 3))
    }
}
