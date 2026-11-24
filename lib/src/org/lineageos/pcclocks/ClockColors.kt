// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks

import android.content.Context
import com.android.systemui.plugins.keyguard.ui.clocks.ThemeConfig
import org.lineageos.pcclocks.model.TonePalette

/** The palette a face paints with: the picked seed color, or the wallpaper's system colors. */
object ClockColors {

    // Four tones of the system palette per theme: main, then the supporting tones.
    private val SYSTEM_TONES =
        mapOf(
            true to
                listOf(
                    android.R.color.system_accent1_100,
                    android.R.color.system_accent2_300,
                    android.R.color.system_accent3_200,
                    android.R.color.system_accent1_400,
                ),
            false to
                listOf(
                    android.R.color.system_accent1_600,
                    android.R.color.system_accent2_400,
                    android.R.color.system_accent3_500,
                    android.R.color.system_accent1_300,
                ),
        )

    fun paletteOf(context: Context, theme: ThemeConfig): TonePalette =
        theme.seedColor?.let { TonePalette.fromSeed(it, theme.isDarkTheme) }
            ?: systemPalette(context, theme.isDarkTheme)

    private fun systemPalette(context: Context, isDark: Boolean) =
        TonePalette(SYSTEM_TONES.getValue(isDark).map(context::getColor))
}
