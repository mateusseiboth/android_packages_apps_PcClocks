// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks

import android.graphics.Typeface

/**
 * Fonts the faces use. Only families the system already ships: Roboto Flex and Noto Serif from
 * AOSP, Google Sans Flex from LineageOS (Roboto when a build leaves it out).
 */
object ClockTypefaces {
    val robotoFlex: Typeface by lazy { Typeface.create("roboto-flex", Typeface.NORMAL) }
    val serifBoldItalic: Typeface by lazy { Typeface.create("serif", Typeface.BOLD_ITALIC) }
    val googleSansFlex: Typeface by lazy { Typeface.create("google-sans-flex", Typeface.NORMAL) }

    /** Roboto Flex axes: weight up to 1000, width from 25 (narrow) to 151 (wide). */
    fun robotoFlexAxes(weight: Int, width: Int) = "'wght' $weight, 'wdth' $width"
}
