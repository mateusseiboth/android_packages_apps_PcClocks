// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks

import android.icu.util.Calendar
import com.android.systemui.customization.clocks.TimeKeeper
import java.time.LocalDate
import java.util.Locale
import org.lineageos.pcclocks.face.WeatherInfo
import org.lineageos.pcclocks.model.ClockTime

/** What both faces of a clock share: the time source, format, locale and weather. */
class SharedClockState(val timeKeeper: TimeKeeper, var is24Hour: Boolean, var locale: Locale) {
    var weather: WeatherInfo? = null

    fun time(): ClockTime {
        timeKeeper.updateTime()
        return ClockTime(
            hourOfDay = field(Calendar.HOUR_OF_DAY),
            minute = field(Calendar.MINUTE),
            second = field(Calendar.SECOND),
            is24Hour = is24Hour,
        )
    }

    fun date(): LocalDate =
        LocalDate.of(field(Calendar.YEAR), field(Calendar.MONTH) + 1, field(Calendar.DAY_OF_MONTH))

    private fun field(calendarField: Int) =
        timeKeeper.queryCalendar(calendarField) { value, _, _ -> value }
}
