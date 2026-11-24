// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks.face

import java.time.LocalDate
import java.util.Locale
import org.lineageos.pcclocks.model.ClockTime
import org.lineageos.pcclocks.model.TonePalette
import org.lineageos.pcclocks.model.WeatherIconKind

/** Weather as the weather face shows it. */
data class WeatherInfo(val icon: WeatherIconKind, val temperature: String)

/** Everything a face needs to draw itself. */
data class FaceState(
    val time: ClockTime,
    val date: LocalDate,
    val locale: Locale,
    val palette: TonePalette,
    val fontSizePx: Float,
    val weather: WeatherInfo? = null,
)
