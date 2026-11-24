// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks

import com.android.internal.annotations.Keep
import com.android.systemui.plugins.annotations.Requires
import com.android.systemui.plugins.keyguard.ui.clocks.ClockProviderPlugin
import org.lineageos.pcclocks.model.ClockStyles

// One plugin service per clock APK. SystemUI and ThemePicker look the clocks up by package, so
// every style ships as its own package; each manifest names one of these classes.

@Keep
@Requires(target = ClockProviderPlugin::class, version = ClockProviderPlugin.VERSION)
class BigNumClockProvider :
    PcClockProvider(
        ClockStyles.BIG_NUM,
        R.string.clock_bignum_name,
        R.string.clock_bignum_description,
    )

@Keep
@Requires(target = ClockProviderPlugin::class, version = ClockProviderPlugin.VERSION)
class CalligraphyClockProvider :
    PcClockProvider(
        ClockStyles.CALLIGRAPHY,
        R.string.clock_calligraphy_name,
        R.string.clock_calligraphy_description,
    )

@Keep
@Requires(target = ClockProviderPlugin::class, version = ClockProviderPlugin.VERSION)
class GrowthClockProvider :
    PcClockProvider(
        ClockStyles.GROWTH,
        R.string.clock_growth_name,
        R.string.clock_growth_description,
    )

@Keep
@Requires(target = ClockProviderPlugin::class, version = ClockProviderPlugin.VERSION)
class InflateClockProvider :
    PcClockProvider(
        ClockStyles.INFLATE,
        R.string.clock_inflate_name,
        R.string.clock_inflate_description,
    )

@Keep
@Requires(target = ClockProviderPlugin::class, version = ClockProviderPlugin.VERSION)
class MetroClockProvider :
    PcClockProvider(ClockStyles.METRO, R.string.clock_metro_name, R.string.clock_metro_description)

@Keep
@Requires(target = ClockProviderPlugin::class, version = ClockProviderPlugin.VERSION)
class NumOverlapClockProvider :
    PcClockProvider(
        ClockStyles.NUM_OVERLAP,
        R.string.clock_numoverlap_name,
        R.string.clock_numoverlap_description,
    )

@Keep
@Requires(target = ClockProviderPlugin::class, version = ClockProviderPlugin.VERSION)
class WeatherClockProvider :
    PcClockProvider(
        ClockStyles.WEATHER,
        R.string.clock_weather_name,
        R.string.clock_weather_description,
    )
