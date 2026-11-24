// SPDX-FileCopyrightText: The LineageOS Project
// SPDX-License-Identifier: Apache-2.0

package org.lineageos.pcclocks

import android.content.Context
import com.android.systemui.customization.clocks.view.DefaultClockFaceLayout
import com.android.systemui.plugins.keyguard.VRect
import com.android.systemui.plugins.keyguard.ui.clocks.ClockAnimations
import com.android.systemui.plugins.keyguard.ui.clocks.ClockAxisStyle
import com.android.systemui.plugins.keyguard.ui.clocks.ClockFaceConfig
import com.android.systemui.plugins.keyguard.ui.clocks.ClockFaceController
import com.android.systemui.plugins.keyguard.ui.clocks.ClockFaceEvents
import com.android.systemui.plugins.keyguard.ui.clocks.ClockPositionAnimationArgs
import com.android.systemui.plugins.keyguard.ui.clocks.ClockSettings
import com.android.systemui.plugins.keyguard.ui.clocks.ClockViewIds
import com.android.systemui.plugins.keyguard.ui.clocks.ThemeConfig
import org.lineageos.pcclocks.face.ClockFaceView
import org.lineageos.pcclocks.face.ClockPainter
import org.lineageos.pcclocks.face.FaceState
import org.lineageos.pcclocks.model.ClockStyle

/** One face of a clock (large or small): keeps its colors, size and doze level and redraws. */
class PcClockFaceController(
    private val pluginCtx: Context,
    private val style: ClockStyle,
    painter: ClockPainter,
    isLarge: Boolean,
    private val shared: SharedClockState,
    settings: ClockSettings,
) : ClockFaceController {
    override var theme = ThemeConfig(isDarkTheme = true, settings.seedColor)
    private var palette = ClockColors.paletteOf(pluginCtx, theme)
    private var aodColor = theme.getAodColor(pluginCtx)
    private var dozeFraction = 0f
    private var fontSizePx = pluginCtx.resources.getDimension(FaceSize.of(isLarge))

    @Suppress("OVERRIDE_DEPRECATION")
    override val view = ClockFaceView(pluginCtx, painter, state()).apply { id = ViewId.of(isLarge) }
    override val layout = DefaultClockFaceLayout(view)
    override val config =
        ClockFaceConfig(hasCustomWeatherDataDisplay = isLarge && style.showsWeather)

    fun refresh() {
        view.state = state()
    }

    private fun state() =
        FaceState(
            time = shared.time(),
            date = shared.date(),
            locale = shared.locale,
            palette = palette.towards(aodColor, dozeFraction),
            fontSizePx = fontSizePx,
            weather = shared.weather?.takeIf { style.showsWeather },
        )

    override val events =
        object : ClockFaceEvents {
            override fun onTimeTick() = refresh()

            override fun onThemeChanged(theme: ThemeConfig) {
                this@PcClockFaceController.theme = theme
                palette = ClockColors.paletteOf(pluginCtx, theme)
                aodColor = theme.getAodColor(pluginCtx)
                refresh()
            }

            override fun onFontSettingChanged(fontSizePx: Float) {
                this@PcClockFaceController.fontSizePx = fontSizePx
                refresh()
            }

            @Deprecated("No longer necessary, pending removal")
            override fun onTargetRegionChanged(targetRegion: VRect) {}

            override fun onSecondaryDisplayChanged(onSecondaryDisplay: Boolean) {}
        }

    override val animations =
        object : ClockAnimations {
            override fun enter() {}

            override fun doze(fraction: Float) {
                dozeFraction = fraction
                refresh()
            }

            override fun fold(fraction: Float) {}

            override fun charge() {}

            override fun onPositionAnimated(anim: ClockPositionAnimationArgs) {}

            override fun onPickerCarouselSwiping(swipingFraction: Float) {}

            override fun onFidgetTap(x: Float, y: Float) {}

            override fun onFontAxesChanged(style: ClockAxisStyle) {}
        }

    private object FaceSize {
        private val SIZES =
            mapOf(true to R.dimen.pc_clock_large_size, false to R.dimen.pc_clock_small_size)

        fun of(isLarge: Boolean) = SIZES.getValue(isLarge)
    }

    private object ViewId {
        fun of(isLarge: Boolean): Int =
            mapOf(
                    true to ClockViewIds.LOCKSCREEN_CLOCK_VIEW_LARGE,
                    false to ClockViewIds.LOCKSCREEN_CLOCK_VIEW_SMALL,
                )
                .getValue(isLarge)
    }
}
