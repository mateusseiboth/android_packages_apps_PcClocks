// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks

import android.content.Context
import android.icu.util.TimeZone
import com.android.systemui.customization.clocks.TimeKeeper
import com.android.systemui.plugins.keyguard.data.model.AlarmData
import com.android.systemui.plugins.keyguard.data.model.WeatherData
import com.android.systemui.plugins.keyguard.data.model.ZenData
import com.android.systemui.plugins.keyguard.ui.clocks.ClockConfig
import com.android.systemui.plugins.keyguard.ui.clocks.ClockController
import com.android.systemui.plugins.keyguard.ui.clocks.ClockEventListeners
import com.android.systemui.plugins.keyguard.ui.clocks.ClockEvents
import com.android.systemui.plugins.keyguard.ui.clocks.ClockSettings
import com.android.systemui.plugins.keyguard.ui.clocks.TimeFormatKind
import java.io.PrintWriter
import java.util.Locale
import org.lineageos.pcclocks.face.ClockPainters
import org.lineageos.pcclocks.face.WeatherInfo
import org.lineageos.pcclocks.model.ClockStyle
import org.lineageos.pcclocks.model.WeatherIconKind
import org.lineageos.pcclocks.model.WeatherText

/** A clock of one style: its large and small faces and the events SystemUI sends both. */
class PcClockController(
    hostCtx: Context,
    pluginCtx: Context,
    private val style: ClockStyle,
    settings: ClockSettings,
    override val config: ClockConfig,
    timeKeeper: TimeKeeper,
) : ClockController {
    private val shared =
        SharedClockState(
            timeKeeper = timeKeeper,
            is24Hour = TimeFormatKind.getFromContext(hostCtx) == TimeFormatKind.FULL_DAY,
            locale = Locale.getDefault(),
        )
    private val painters = ClockPainters.of(style)

    override val largeClock =
        PcClockFaceController(pluginCtx, style, painters.large(), true, shared, settings)

    override val smallClock =
        PcClockFaceController(pluginCtx, style, painters.small(), false, shared, settings)

    override val eventListeners = ClockEventListeners()

    override val events =
        object : ClockEvents {
            override fun onTimeZoneChanged(timeZone: TimeZone) {
                shared.timeKeeper.timeZone = timeZone
                refreshFaces()
            }

            override fun onTimeFormatChanged(formatKind: TimeFormatKind) {
                shared.is24Hour = formatKind == TimeFormatKind.FULL_DAY
                refreshFaces()
            }

            override fun onLocaleChanged(locale: Locale) {
                shared.locale = locale
                refreshFaces()
            }

            override fun onWeatherDataChanged(data: WeatherData) {
                shared.weather =
                    WeatherIconKind.of(data.state.id)?.let {
                        WeatherInfo(it, WeatherText.temperature(data.temperature))
                    }
                refreshFaces()
            }

            override fun onAlarmDataChanged(data: AlarmData) {}

            override fun onZenDataChanged(data: ZenData) {}
        }

    override fun initialize(isDarkTheme: Boolean, dozeFraction: Float, foldFraction: Float) {
        listOf(smallClock, largeClock).forEach { face ->
            face.events.onThemeChanged(face.theme.copy(isDarkTheme = isDarkTheme))
            face.animations.doze(dozeFraction)
            face.animations.fold(foldFraction)
            face.events.onTimeTick()
        }
    }

    override fun dump(pw: PrintWriter) {
        pw.println("PcClockController ${style.clockId}")
    }

    private fun refreshFaces() {
        smallClock.refresh()
        largeClock.refresh()
    }
}
