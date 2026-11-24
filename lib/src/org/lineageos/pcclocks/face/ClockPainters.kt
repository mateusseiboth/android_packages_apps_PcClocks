// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.face

import org.lineageos.pcclocks.ClockTypefaces
import org.lineageos.pcclocks.model.ClockStyle
import org.lineageos.pcclocks.model.ClockStyles
import org.lineageos.pcclocks.model.ToneScheme

/** The painters of a style: the large clock and the small one above the notifications. */
data class FacePainters(val large: () -> ClockPainter, val small: () -> ClockPainter)

object ClockPainters {

    private fun digitStyles(): Map<ClockStyle, DigitStyle> =
        mapOf(
            ClockStyles.CALLIGRAPHY to DigitStyle(ClockTypefaces.serifBoldItalic),
            ClockStyles.GROWTH to
                DigitStyle(
                    typeface = ClockTypefaces.robotoFlex,
                    fontVariation = ClockTypefaces.robotoFlexAxes(weight = 800, width = 25),
                    stencilGap = 0.035f,
                ),
            ClockStyles.INFLATE to
                DigitStyle(
                    typeface = ClockTypefaces.robotoFlex,
                    fontVariation = ClockTypefaces.robotoFlexAxes(weight = 1000, width = 151),
                    tones = ToneScheme.FOUR_TONES,
                    overlap = 0.18f,
                    outline = 0.025f,
                ),
            ClockStyles.NUM_OVERLAP to
                DigitStyle(
                    typeface = ClockTypefaces.robotoFlex,
                    fontVariation = ClockTypefaces.robotoFlexAxes(weight = 1000, width = 100),
                    tones = ToneScheme.DIAGONAL,
                    overlap = 0.22f,
                ),
            ClockStyles.METRO to
                DigitStyle(
                    typeface = ClockTypefaces.robotoFlex,
                    fontVariation = ClockTypefaces.robotoFlexAxes(weight = 500, width = 100),
                ),
            ClockStyles.WEATHER to DigitStyle(ClockTypefaces.googleSansFlex),
        )

    // Faces whose large clock is not stacked digits.
    private val SPECIAL_LARGE: Map<ClockStyle, () -> ClockPainter> =
        mapOf(
            ClockStyles.BIG_NUM to { BigNumAnalogPainter(diameterPerFontSize = LARGE_DIAL) },
            ClockStyles.METRO to { TransitPainter() },
            ClockStyles.WEATHER to { WeatherPainter() },
        )

    fun of(style: ClockStyle): FacePainters =
        SPECIAL_LARGE[style]?.let { FacePainters(large = it, small = smallOf(style)) }
            ?: stackedOf(style)

    private fun stackedOf(style: ClockStyle): FacePainters {
        val digits = digitStyles().getValue(style)
        return FacePainters(
            large = { StackedDigitsPainter(digits) },
            small = { SingleLinePainter(digits) },
        )
    }

    private fun smallOf(style: ClockStyle): () -> ClockPainter =
        digitStyles()[style]?.let { digits -> { SingleLinePainter(digits) } }
            ?: {
                BigNumAnalogPainter(diameterPerFontSize = SMALL_DIAL)
            }

    private const val LARGE_DIAL = 2.2f
    private const val SMALL_DIAL = 1.2f
}
